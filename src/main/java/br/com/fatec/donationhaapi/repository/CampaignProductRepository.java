package br.com.fatec.donationhaapi.repository;

import java.util.List;

import br.com.fatec.donationhaapi.dto.campaign.CampaignProductHistoryDTO;
import br.com.fatec.donationhaapi.entity.CampaignProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatec.donationhaapi.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CampaignProductRepository extends JpaRepository<CampaignProduct, Long>, PagingAndSortingRepository<CampaignProduct, Long> {

    CampaignProduct findByProduct(Product product);

    // Page<DonationProduct> AllDonationProducts(Pageable pageable);
    List<CampaignProduct> findByCampaign_CampaignId(Long campaignId);

    Page<CampaignProduct> findByDisabledFalse(Pageable pageable);

    Page<CampaignProduct> findByDisabledFalseAndCampaign_Institution_ComercialNameContainsIgnoreCase(Pageable pageable, String institutionName);

    List<CampaignProduct> findByCampaign_Institution_InstitutionIdAndDisabledFalse(Long institutionId);

    @Query("SELECT " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaignProductId = :campaignProductId) as TotalDonation, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaignProductId = :campaignProductId AND d.statusDonation = 0) as DonationDone, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaignProductId = :campaignProductId AND d.statusDonation = 1) as DonationPendentAppointment, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaignProductId = :campaignProductId AND d.statusDonation = 2) as DonationPendentDelivery, " +
            "(SELECT COUNT(d) FROM Donation d WHERE d.campaignProduct.campaignProductId = :campaignProductId AND d.statusDonation = 3) as DonationCanceled, " +
            "(SELECT SUM(d.quantity) FROM Donation d WHERE d.campaignProduct.campaignProductId = :campaignProductId) as TotalItems " +
            "FROM Donation d WHERE d.campaignProduct.campaignProductId = :campaignProductId GROUP BY d.campaignProduct")
    Object[] findHistoryCampaignProduct(@Param("campaignProductId") Long campaignProductId);
}
