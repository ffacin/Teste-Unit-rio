package br.com.fatec.donationhaapi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import br.com.fatec.donationhaapi.dto.campaign.CampaignHistoryDTO;
import br.com.fatec.donationhaapi.dto.campaign.CampaignProductHistoryDTO;
import br.com.fatec.donationhaapi.dto.campaign.ResponseCampaignDTO;
import br.com.fatec.donationhaapi.dto.campaign.SaveCampaignDTO;
import br.com.fatec.donationhaapi.entity.Campaign;
import br.com.fatec.donationhaapi.entity.CampaignProduct;
import br.com.fatec.donationhaapi.entity.Donation;
import br.com.fatec.donationhaapi.exception.BadRequestException;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.*;
import br.com.fatec.donationhaapi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private CampaignProductService campaignProductService;
    @Autowired
    private FileService fileService;

    public ResponseCampaignDTO saveCampaignRequest(SaveCampaignDTO saveCampaignDTO) {
        try {
            Campaign campaign = toCampaign(saveCampaignDTO);
            validCampaign(campaign);
            AtomicReference<Campaign> newCampaign = new AtomicReference<>(new Campaign());
            if (campaign.getCampaignId() == null) {
                newCampaign.set(campaignRepository.saveAndFlush(campaign));
            } else {
                campaignRepository.findById(campaign.getCampaignId())
                        .ifPresentOrElse(newCampaign::set, () -> {
                            throw new NotFoundException("Camapanha não encontrada");
                        });
                newCampaign.get().setUpdatedDate(LocalDate.now());
            }
            return new ResponseCampaignDTO(newCampaign.get(), campaignProductService.saveCampaignProducts(
                    saveCampaignDTO.saveCampaignProductDTOS(), newCampaign.get().getCampaignId()));
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        }catch(BadRequestException badRequestException){
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar campanha, entre em contato com um Administrador");
        }
    }

    public CampaignHistoryDTO findHistoryCampaign(Long idCampaign) {
        try {
            Campaign campaign = campaignRepository.findById(idCampaign)
                    .orElseThrow(() -> new NotFoundException("Campanha não encontrada."));
            fileService.personalizePhotosUrl(campaign);

            List<CampaignProduct> campaignProducts = campaignProductService.findByIdCampaignCampaignsProducts(idCampaign);
            List<CampaignProductHistoryDTO> campaignHistoryDTOList = new ArrayList<>();
            for (CampaignProduct campaignProduct : campaignProducts) {
                campaignHistoryDTOList.add(campaignProductService.findHistoryCampaignProduct(campaignProduct.getCampaignProductId()));
            }
            Object[] campaignHistory = campaignRepository.findHistoryCampaign(idCampaign);
            if (campaignHistory == null || campaignHistory.length == 0)
                return new CampaignHistoryDTO(0, 0, 0, 0, BigDecimal.ZERO, 0, campaign, campaignHistoryDTOList);
            Object[] innerArray = (Object[]) campaignHistory[0];
            if (innerArray.length == 6) {
                Integer totalDonation = ((Number) innerArray[0]).intValue(); // Index 0
                Integer donationDone = ((Number) innerArray[1]).intValue(); // Index 1
                Integer donationPendentAppointment = ((Number) innerArray[2]).intValue(); // Index 2
                Integer donationPendentDelivery = ((Number) innerArray[3]).intValue(); // Index 3
                Integer donationCanceled = ((Number) innerArray[4]).intValue(); // Index 4
                BigDecimal totalItems = (BigDecimal) innerArray[5]; // Index 5

                return new CampaignHistoryDTO(donationPendentAppointment, donationPendentDelivery,
                        donationDone, donationCanceled, totalItems,
                        totalDonation, campaign, campaignHistoryDTOList);
            } else {
                throw new InternalServerException("Dados de histórico de campanha não encontrados.");
            }
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar CampaignProduct: " + exception.getMessage());
        }
    }

    public List<Campaign> getCampaignsByInstitutionId(Long institutionId) {
        try {
            if (!institutionRepository.existsById(institutionId)) {
                throw new NotFoundException("Instituição com ID " + institutionId + " não encontrada.");
            }
            return campaignRepository.findByInstitution_InstitutionIdAndDisabledFalse(institutionId);
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao buscar campanhas para a instituição, entre em contato com um Administrador");
        }
    }

    public List<Campaign> getInfoCampaigns() {
        try {
            List<Campaign> campaigns = campaignRepository.findByDisabledFalse();
            for (Campaign campaign : campaigns) {
                fileService.personalizePhotosUrl(campaign);
            }
            return campaigns;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar informações das campanhas, entre em contato com um Administrador");
        }
    }

    public Campaign findByIdCampaign(Long idCampaign) {
        try {
            return campaignRepository.findById(idCampaign)
                    .map(campaign -> fileService.personalizePhotosUrl(campaign))
                    .orElseThrow(() -> new NotFoundException("Campanha não encontrada. ID: " + idCampaign));
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao buscar campanha, entre em contato com um Administrador");
        }
    }

    public HashMap<String, Object> disabledCampaign(Long idCampaign) {
        try {
            HashMap<String, Object> resultDisable = new HashMap<String, Object>();
            Campaign campaignResult = campaignRepository.findById(idCampaign)
                    .orElseThrow(() -> new NotFoundException("Campanha não encontrada. ID: " + idCampaign));
            List<CampaignProduct> campaignProducts = campaignProductService
                    .findByIdCampaignCampaignsProducts(idCampaign);
            Boolean result = false;
            for (CampaignProduct campaignProduct : campaignProducts) {
                result = campaignProductService.disabled(campaignProduct.getCampaignProductId());
            }
            if (result) {
                campaignResult.setDisabled(true);
                campaignRepository.saveAndFlush(campaignResult);
                resultDisable.put("mensagem", "Camapanha desabilitada.");
            } else {
                resultDisable.put("mensagem", "Camapanha não desabilitada.");
            }
            return resultDisable;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao desativar campanha, entre em contato com um Administrador");
        }
    }

    public Campaign toCampaign(SaveCampaignDTO dto) {
        try {
            Campaign campaign = new Campaign();
            usersRepository.findById(dto.idUser())
                    .ifPresentOrElse(campaign::setUser,
                            () -> {
                                throw new NotFoundException("Usuário não encontrado.");
                            });
            institutionRepository.findById(dto.idInstitution())
                    .ifPresentOrElse(campaign::setInstitution,
                            () -> {
                                throw new NotFoundException("Instituição não encontrado.");
                            });
            campaign.setCampaignDescription(dto.campaignDescription());
            campaign.setCampaignEnding(dto.campaignEnding());
            campaign.setCampaignId(dto.idCampaign());
            return campaign;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao converter campanha, entre em contato com um Administrador");
        }
    }

    public void validCampaign(Campaign campaign) {
        if (campaign.getCampaignEnding().isBefore(LocalDate.now()))
            throw new BadRequestException(
                    "Uma campanha não pode terminar com uma data anterior a hoje. Data de término enviada: "
                            + campaign.getCampaignEnding());
    }
}
