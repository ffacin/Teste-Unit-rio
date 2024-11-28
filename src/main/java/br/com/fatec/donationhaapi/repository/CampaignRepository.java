package br.com.fatec.donationhaapi.repository;

import br.com.fatec.donationhaapi.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByDisabledFalse();

    List<Campaign> findByInstitution_InstitutionIdAndDisabledFalse(Long institutionId);

    @Query("SELECT " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaign.campaignId = :campaignId) as TotalDonation, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaign.campaignId = :campaignId AND d.statusDonation = 0) as DonationDone, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaign.campaignId = :campaignId AND d.statusDonation = 1) as DonationPendentAppointment, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaign.campaignId = :campaignId AND d.statusDonation = 2) as DonationPendentDelivery, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaign.campaignId = :campaignId AND d.statusDonation = 3) as DonationCanceled, " +
            "(SELECT SUM(d.quantity) FROM Donation d WHERE d.campaignProduct.campaign.campaignId = :campaignId) as TotalItems " +
            "FROM Donation d WHERE d.campaignProduct.campaign.campaignId = :campaignId GROUP BY d.campaignProduct.campaign")
    Object[] findHistoryCampaign(@Param("campaignId") Long campaignId);

}