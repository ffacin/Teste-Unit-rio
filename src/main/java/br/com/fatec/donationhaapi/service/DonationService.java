package br.com.fatec.donationhaapi.service;

import br.com.fatec.donationhaapi.dto.donation.CreateDonationDto;
import br.com.fatec.donationhaapi.dto.donation.UpdateDonationDto;
import br.com.fatec.donationhaapi.entity.CampaignProduct;
import br.com.fatec.donationhaapi.entity.Donation;
import br.com.fatec.donationhaapi.entity.Institution;
import br.com.fatec.donationhaapi.enums.StatusDonation;
import br.com.fatec.donationhaapi.exception.BadRequestException;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.*;
import br.com.fatec.donationhaapi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DonationService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CampaignProductRepository campaignProductRepository;
    @Autowired
    private DeliveryRulesRepository deliveryRulesRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private FileService fileService;

    public List<Donation> findActiveNonCancelledDonations() {
        try {
            List<Donation> donations = donationRepository.findActiveNonCancelledDonations();
            for (Donation donation : donations) {
                fileService.personalizePhotosUrl(donation);
            }
            return donations;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar doações ativas, entre em contato com um Administrador");
        }
    }

    public List<Donation> findDonationByInstitution(Long idInstitution) {
        try {
            if (!institutionRepository.existsById(idInstitution))
                throw new NotFoundException("Instituição não encontrada. Id:" + idInstitution);
            List<Donation> donations = donationRepository
                    .findByCampaignProduct_Campaign_Institution_InstitutionId(idInstitution);
            for (Donation donation : donations) {
                fileService.personalizePhotosUrl(donation);
            }
            return donations;
        } catch (NotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar doações da instituição, entre em contato com um Administrador");
        }
    }

    public List<Donation> findDonationByCampaignProduct(Long idCampaignProduct) {
        try {
            if (!campaignProductRepository.existsById(idCampaignProduct))
                throw new NotFoundException("Campanha não encontrada. Id:" + idCampaignProduct);
            List<Donation> donations = donationRepository.findByCampaignProduct_CampaignProductId(idCampaignProduct);
            for (Donation donation : donations) {
                fileService.personalizePhotosUrl(donation);
            }
            return donations;
        } catch (NotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar doações pelos produtos da campanha, entre em contato com um Administrador");
        }
    }

    public List<Donation> findDonationByUser(String idUser) {
        try {
            if (!usersRepository.existsById(UUID.fromString(idUser)))
                throw new NotFoundException("Usuário não encontrado. Id:" + idUser);
            List<Donation> donations = donationRepository.findByUsers_UsersId(UUID.fromString(idUser));
            for (Donation donation : donations) {
                fileService.personalizePhotosUrl(donation);
            }
            return donations;
        } catch (NotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar doações do usuário, entre em contato com um Administrador");
        }
    }

    public List<Donation> findCompletedDonations() {
        try {
            List<Donation> donations = donationRepository.findCompletedDonations();
            for (Donation donation : donations) {
                fileService.personalizePhotosUrl(donation);
            }
            return donations;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar doações completas, entre em contato com um Administrador");
        }
    }

    public Donation findById(Long idDonation) {
        try {
            Donation donation = donationRepository.findById(idDonation)
                    .orElseThrow(() -> new NotFoundException("Doação não encontrada."));
            fileService.personalizePhotosUrl(donation);
            return donation;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao procurar dados da doação, entre em contato com um Administrador");
        }
    }

    public Donation createDonationRequest(CreateDonationDto createDonationDto) {
        try {
            Donation newDonation = donationDTOtoDonation(createDonationDto);
            validDonation(newDonation);
            Donation donationResult = donationRepository.saveAndFlush(newDonation);
            return donationResult;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar doação, entre em contato com um Administrador");
        }
    }

    public Donation updateDonation(UpdateDonationDto updateDonationDto) {
        try {
            Optional<Donation> oldDonation = donationRepository.findById(updateDonationDto.getDonationId());
            if (oldDonation.isEmpty())
                throw new NotFoundException("Doação não encontrada.");
            if (oldDonation.get().getDisabled() || oldDonation.get().getStatusDonation().equals(StatusDonation.CANCELED))
                throw new BadRequestException("Doação cancelada.");
            if (oldDonation.get().getStatusDonation() == StatusDonation.COMPLETE)
                throw new BadRequestException("Doação já realizada.");
            deliveryRulesRepository.findById(updateDonationDto.getDeliveryRulesId())
                    .ifPresentOrElse(deliveryRulesResult -> {
                                oldDonation.get().setDeliveryRules(deliveryRulesResult);
                            },
                            () -> {
                                throw new NotFoundException("Regras de entrega não encontrado.");
                            });
            if (updateDonationDto.getQuantity() != oldDonation.get().getQuantity())
                campaignProductRepository.findById(oldDonation.get().getCampaignProduct().getCampaignProductId())
                        .ifPresentOrElse(campaignProductResult -> {
                                    campaignProductResult.setReceived(
                                            campaignProductResult.getReceived()
                                                    .subtract(oldDonation.get().getQuantity()));
                                    campaignProductResult.setReceived(
                                            campaignProductResult.getReceived()
                                                    .add(updateDonationDto.getQuantity()));
                                    campaignProductRepository.save(campaignProductResult);
                                    oldDonation.get().setCampaignProduct(campaignProductResult);
                                },
                                () -> {
                                    throw new NotFoundException("Campanha Produto não encontrado.");
                                });
            oldDonation.get().setAnonymous(updateDonationDto.getAnonymous());
            oldDonation.get().setQuantity(updateDonationDto.getQuantity());
            return donationRepository.saveAndFlush(oldDonation.get());
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao atualizar dados da doação, entre em contato com um Administrador");
        }
    }

    public Donation donationReceipt(Long idDonation, MultipartFile file, String idUser) {
        try {
            Donation donation = donationRepository.findById(idDonation)
                    .orElseThrow(() -> new NotFoundException("Doação não encontrada."));
            if (!validUserInInstitution(donation.getCampaignProduct().getCampaign().getInstitution(), idUser))
                throw new BadRequestException("Este usuário não pertence a esta instituição.");
            validStatusDonation(true, donation);
            donation.setImgUrl(fileService.uploadFile(file).getDownloadUrl());
            donation.setStatusDonation(StatusDonation.COMPLETE);
            return updateValueDonationReceipt(donationRepository.saveAndFlush(donation));
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao receber dados da doação, entre em contato com um Administrador");
        }
    }

    public Donation donationConfirmAppointment(Long idDonation, String idUser) {
        try {
            Donation donation = donationRepository.findById(idDonation)
                    .orElseThrow(() -> new NotFoundException("Doação não encontrada."));
            if (!validUserInInstitution(donation.getCampaignProduct().getCampaign().getInstitution(), idUser))
                throw new BadRequestException("Este usuário não pertence a esta instituição.");
            validStatusDonation(false, donation);
            donation.setStatusDonation(StatusDonation.PENDINGDEVIVERY);
            return donationRepository.saveAndFlush(donation);
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao confirmar agendamento da doação, contate o administrador.");
        }
    }

    public HashMap<String, Object> deleteDonationRequest(Long donationId) {
        try {
            donationRepository.findById(donationId).ifPresentOrElse(
                    donationResult -> {
                        campaignProductRepository.findById(donationResult.getCampaignProduct().getCampaignProductId())
                                .ifPresentOrElse(campaignProductResult -> {
                                            campaignProductResult.setReceived(
                                                    campaignProductResult.getReceived()
                                                            .subtract(donationResult.getQuantity()));
                                            campaignProductRepository.save(campaignProductResult);
                                            donationResult.setCampaignProduct(campaignProductResult);
                                        },
                                        () -> {
                                            throw new NotFoundException("Campanha Produto não encontrado.");
                                        });
                        if (donationResult.getStatusDonation() != StatusDonation.PENDINGDEVIVERY
                                || donationResult.getStatusDonation() != StatusDonation.PENDINGSCHEDULE)
                            throw new BadRequestException("Esta doação já foi finalizada, não pode ser alterada.");
                        donationResult.setDisabled(true);
                        donationResult.setStatusDonation(StatusDonation.CANCELED);
                        donationRepository.saveAndFlush(donationResult);
                    },
                    () -> {
                        new NotFoundException("Doação não encontrada.");
                    });
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("mensagem", "Doação eliminada.");
            return result;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao excluir doação, entre em contato com um Administrador");
        }
    }

    public Boolean validUserInInstitution(Institution institution, String idUser) {
        try {
            if (!usersRepository.existsById(UUID.fromString(idUser)))
                throw new BadRequestException("Usuário inexistente. Id: " + idUser);
            List<Institution> institutions = institutionRepository.findByUsers_UsersId(UUID.fromString(idUser));
            return institutions.contains(institution);
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao validar instituição com usuário, contate o administrador.");
        }
    }

    public Donation donationDTOtoDonation(CreateDonationDto createDonationDto) {
        Donation newDonation = new Donation();
        CampaignProduct campaignProduct = new CampaignProduct();
        newDonation.setAnonymous(createDonationDto.getAnonymous());
        newDonation.setQuantity(createDonationDto.getQuantity());
        newDonation.setStatusDonation(StatusDonation.PENDINGSCHEDULE);
        newDonation.setComment(createDonationDto.getComment());
        campaignProductRepository.findById(createDonationDto.getCampaignProductId())
                .ifPresentOrElse(campaignProductResult -> {
                            campaignProduct.setCampaignProductId(campaignProductResult.getCampaignProductId());
                            campaignProduct.setCampaign(campaignProductResult.getCampaign());
                            campaignProduct.setProduct(campaignProductResult.getProduct());
                            campaignProduct.setQuantity(campaignProductResult.getQuantity());
                            campaignProduct.setReceived(campaignProductResult.getReceived());
                            campaignProduct.setCreatedDate(campaignProductResult.getCreatedDate());
                            campaignProduct.setUpdatedDate(campaignProductResult.getUpdatedDate());
                            campaignProduct.setDisabled(false);
                            newDonation.setCampaignProduct(campaignProduct);
                        },
                        () -> {
                            throw new NotFoundException("Campanha Produto não encontrado.");
                        });
        deliveryRulesRepository.findById(createDonationDto.getDeliveryRulesId())
                .ifPresentOrElse(newDonation::setDeliveryRules,
                        () -> {
                            throw new NotFoundException("Regras de entrega não encontrado.");
                        });
        if (!createDonationDto.getAnonymous())
            usersRepository.findById(createDonationDto.getUserId()).ifPresentOrElse(newDonation::setUsers, () -> {
                throw new NotFoundException("Usuário não encontrado.");
            });
        return newDonation;
    }

    public void validDonation(Donation donation) {
        if (donation.getQuantity().compareTo(BigDecimal.valueOf(0.0)) <= 0)
            throw new BadRequestException(
                    "Uma doação precisa ter uma quantidade maior que zero. Quantidade enviada: "
                            + donation.getQuantity());
    }

    public Donation updateValueDonationReceipt(Donation donation) {
        if (donation.getDonationId() != null)
            if (donation.getCampaignProduct().getReceived() != BigDecimal.valueOf(0.0)) {
                donation.getCampaignProduct().setReceived(
                        donation.getCampaignProduct().getReceived()
                                .add(donation.getQuantity()));
                campaignProductRepository.saveAndFlush(donation.getCampaignProduct());
            }
        return donation;
    }

    public void validStatusDonation(Boolean willReceived, Donation donation) {
        if (donation.getStatusDonation() == StatusDonation.COMPLETE)
            throw new BadRequestException("Doação já recebida.");
        if (donation.getStatusDonation() == StatusDonation.CANCELED)
            throw new BadRequestException("Essa doação foi cancelada.");
        if (donation.getStatusDonation() == StatusDonation.PENDINGDEVIVERY)
            if (!willReceived) throw new BadRequestException("Agendamento já confirmado para esta doação.");
        if (donation.getStatusDonation() == StatusDonation.PENDINGSCHEDULE)
            if (willReceived) throw new BadRequestException("Doação ainda não confirmada em agendamento.");
    }
}
