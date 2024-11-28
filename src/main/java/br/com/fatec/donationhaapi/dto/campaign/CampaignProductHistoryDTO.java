package br.com.fatec.donationhaapi.dto.campaign;

import br.com.fatec.donationhaapi.entity.CampaignProduct;

import java.math.BigDecimal;

public record CampaignProductHistoryDTO(Integer DonationPendentAppointment, Integer DonationPendentDelivery,
                                        Integer DonationDone, Integer DonationCanceled, BigDecimal TotalItems,
                                        Integer TotalDonation, CampaignProduct campaignProduct) {
}
