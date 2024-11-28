package br.com.fatec.donationhaapi.dto.campaign;

import br.com.fatec.donationhaapi.entity.Campaign;

import java.math.BigDecimal;
import java.util.List;

public record CampaignHistoryDTO(Integer DonationPendentAppointment, Integer DonationPendentDelivery,
                                 Integer DonationDone, Integer DonationCanceled, BigDecimal TotalItems,
                                 Integer TotalDonation, Campaign campaign, List<CampaignProductHistoryDTO> campaignProduct) {
}

