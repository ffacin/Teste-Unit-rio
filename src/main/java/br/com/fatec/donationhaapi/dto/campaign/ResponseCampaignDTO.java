package br.com.fatec.donationhaapi.dto.campaign;

import br.com.fatec.donationhaapi.entity.Campaign;
import br.com.fatec.donationhaapi.entity.CampaignProduct;

import java.util.List;

public record ResponseCampaignDTO(Campaign campaign, List<CampaignProduct> campaignProducts) {
}
