package br.com.fatec.donationhaapi.service;

import br.com.fatec.donationhaapi.dto.campaign.CampaignProductHistoryDTO;
import br.com.fatec.donationhaapi.dto.campaign.SaveCampaignProductDTO;
import br.com.fatec.donationhaapi.entity.CampaignProduct;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.CampaignProductRepository;
import br.com.fatec.donationhaapi.repository.CampaignRepository;
import br.com.fatec.donationhaapi.repository.InstitutionRepository;
import br.com.fatec.donationhaapi.repository.ProductRepository;
import br.com.fatec.donationhaapi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CampaignProductService {
    @Autowired
    private CampaignProductRepository campaignProductRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private InstitutionRepository institutionRepository;

    public CampaignProduct findCampaignProductById(Long campaignProductId) {
        try {
            Optional<CampaignProduct> campaignProduct = Optional
                    .ofNullable(campaignProductRepository.findById(campaignProductId)
                            .orElseThrow(() -> new NotFoundException("Campaign Product não encontrada")));
            return fileService.personalizePhotosUrl(campaignProduct.get());
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao encontrar produto(s) da campanha, entre em contato com um Administrador");
        }
    }

    public List<CampaignProduct> findByIdCampaignCampaignsProducts(Long idCampaign) {
        try {
            List<CampaignProduct> campaignProducts = campaignProductRepository.findByCampaign_CampaignId(idCampaign);
            Optional.ofNullable(campaignRepository.findById(idCampaign)
                    .orElseThrow(() -> new NotFoundException("Campanha não encontrada.")));
            for (CampaignProduct campaignProduct : campaignProducts) {
                fileService.personalizePhotosUrl(campaignProduct);
            }
            return campaignProducts;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar produto(s) de campanha para a campanha, entre em contato com um Administrador");
        }
    }

    public CampaignProductHistoryDTO findHistoryCampaignProduct(Long idCampaignProduct) {
        try {
            CampaignProduct campaignProduct = campaignProductRepository.findById(idCampaignProduct)
                    .orElseThrow(() -> new NotFoundException("Campanha não encontrada."));
            fileService.personalizePhotosUrl(campaignProduct);

            Object[] campaignProductHistory = campaignProductRepository.findHistoryCampaignProduct(idCampaignProduct);

            if (campaignProductHistory == null || campaignProductHistory.length == 0)
                return new CampaignProductHistoryDTO(0, 0, 0, 0, BigDecimal.ZERO, 0, campaignProduct);

            Object[] innerArray = (Object[]) campaignProductHistory[0];
            if (innerArray.length == 6) {
                Integer totalDonation = ((Number) innerArray[0]).intValue(); // Index 0
                Integer donationDone = ((Number) innerArray[1]).intValue(); // Index 1
                Integer donationPendentAppointment = ((Number) innerArray[2]).intValue(); // Index 2
                Integer donationPendentDelivery = ((Number) innerArray[3]).intValue(); // Index 3
                Integer donationCanceled = ((Number) innerArray[4]).intValue(); // Index 4
                BigDecimal totalItems = (BigDecimal) innerArray[5]; // Index 5

                return new CampaignProductHistoryDTO(donationPendentAppointment, donationPendentDelivery,
                        donationDone, donationCanceled, totalItems,
                        totalDonation, campaignProduct);
            } else {
                throw new InternalServerException("Dados de histórico de campanha não encontrados.");
            }
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar CampaignProduct: " + exception.getMessage());
        }
    }


    public List<CampaignProduct> getCampaignProductsByInstitutionId(Long institutionId) {
        try {
            if (!institutionRepository.existsById(institutionId)) {
                throw new NotFoundException("Instituição com ID " + institutionId + " não encontrada.");
            }
            return campaignProductRepository.findByCampaign_Institution_InstitutionIdAndDisabledFalse(institutionId);
        } catch (Exception exception) {
            throw new InternalServerException(
                    "Erro ao buscar produto(s) de campanha para a instituição, entre em contato com um Administrador");
        }
    }

    public Page<CampaignProduct> getInfoCampaignsProducts(int page, int size, String sortMethod, String search) {
        try {
            PageRequest pageable = createPageRequest(page, size, sortMethod);
            Page<CampaignProduct> result = search.isBlank()
                    ? findCampaignProducts(pageable)
                    : findCampaignProductsWithSearch(pageable, search);

            result.getContent().forEach(fileService::personalizePhotosUrl);
            return result;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar produto(s) de campanha, entre em contato com um Administrador");
        }
    }

    public List<CampaignProduct> saveCampaignProducts(List<SaveCampaignProductDTO> saveCampaignProductDTOLists,
                                                      Long idCampaign) {
        try {
            List<CampaignProduct> newsCampaignProducts = new ArrayList<CampaignProduct>();
            for (SaveCampaignProductDTO saveCampaignProductDTO : saveCampaignProductDTOLists) {
                CampaignProduct campaignProduct = saveCampaignProduct(saveCampaignProductDTO, idCampaign);
                newsCampaignProducts.add(campaignProduct);
            }
            return campaignProductRepository.saveAllAndFlush(newsCampaignProducts);
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar lista de produto(s) de campanha, entre em contato com um Administrador");
        }
    }

    public CampaignProduct saveCampaignProduct(SaveCampaignProductDTO saveCampaignProductDTO, Long idCampaign) {
        try {
            CampaignProduct campaignProduct = new CampaignProduct();
            campaignProduct.setReceived(BigDecimal.valueOf(0));
            if (saveCampaignProductDTO.idCampaignProduct() != null)
                if (!saveCampaignProductDTO.idCampaignProduct().toString().isBlank())
                    campaignProductRepository.findById(saveCampaignProductDTO.idCampaignProduct())
                            .ifPresentOrElse((result) -> {
                                        campaignProduct.setCampaignProductId(result.getCampaignProductId());
                                        campaignProduct.setUpdatedDate(LocalDate.now());
                                        campaignProduct.setReceived(result.getReceived());
                                    },
                                    () -> {
                                        throw new NotFoundException("Campanha Produto não encontrado. ID: "
                                                + saveCampaignProductDTO.idProduct());
                                    });
            productRepository.findById(saveCampaignProductDTO.idProduct())
                    .ifPresentOrElse(campaignProduct::setProduct,
                            () -> {
                                throw new NotFoundException(
                                        "Produto não encontrado. ID: " + saveCampaignProductDTO.idProduct());
                            });
            campaignRepository.findById(idCampaign)
                    .ifPresentOrElse(campaignProduct::setCampaign,
                            () -> {
                                throw new NotFoundException("Campanha não encontrada. ID: " + idCampaign);
                            });
            campaignProduct.setQuantity(saveCampaignProductDTO.quantity());
            return campaignProduct;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar produto(s) de campanha, entre em contato com um Administrador");
        }
    }

    public Boolean disabled(Long idCampaignProduct) {
        try {
            CampaignProduct campaignProductResult = campaignProductRepository.findById(idCampaignProduct)
                    .orElseThrow(
                            () -> new NotFoundException("Campanha Produto não encontrada. ID: " + idCampaignProduct));
            if (campaignProductResult != null) {
                campaignProductResult.setDisabled(true);
                campaignProductRepository.saveAndFlush(campaignProductResult);
                return true;
            } else {
                return false;
            }
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao desativar produto(s) de campanha, entre em contato com um Administrador");
        }
    }

    private PageRequest createPageRequest(int page, int size, String sortMethod) {
        Sort sort = switch (sortMethod) {
            case "mostRecent" -> Sort.by("campaignProductId").descending();
            case "oldest" -> Sort.by("campaignProductId").ascending();
            case "smallestRemainder" -> Sort.by("received").ascending();
            default -> Sort.by("campaign.campaignEnding").ascending();
        };
        return PageRequest.of(page, size, sort);
    }

    private Page<CampaignProduct> findCampaignProducts(PageRequest pageable) {
        return campaignProductRepository.findByDisabledFalse(pageable);
    }

    private Page<CampaignProduct> findCampaignProductsWithSearch(PageRequest pageable, String search) {
        return campaignProductRepository.findByDisabledFalseAndCampaign_Institution_ComercialNameContainsIgnoreCase(pageable, search);
    }
}