package br.com.fatec.donationhaapi.dto.campaign;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SaveCampaignDTO(Long idCampaign, LocalDate campaignEnding, String campaignDescription, Long idInstitution,
                              UUID idUser,
                              List<SaveCampaignProductDTO> saveCampaignProductDTOS) {
}
