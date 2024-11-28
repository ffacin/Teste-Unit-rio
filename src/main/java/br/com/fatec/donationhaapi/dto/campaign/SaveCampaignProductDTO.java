package br.com.fatec.donationhaapi.dto.campaign;

import java.math.BigDecimal;

public record SaveCampaignProductDTO(Long idCampaignProduct, Long idProduct, BigDecimal quantity) {
}
