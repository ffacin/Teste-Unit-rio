package br.com.fatec.donationhaapi.repository;

import br.com.fatec.donationhaapi.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query(value = "select * from donation e where e.id_campaign_product = ?", nativeQuery = true)
    Donation findByCampaignProductId(Long campaignProductId);

    @Query("select d from Donation d where d.disabled = false and d.statusDonation <> 3")
    List<Donation> findActiveNonCancelledDonations();

    @Query("select d from Donation d where d.statusDonation = 0")
    List<Donation> findCompletedDonations();

    List<Donation> findByCampaignProduct_Campaign_Institution_InstitutionId(Long idInstitution);
    List<Donation> findByCampaignProduct_CampaignProductId(Long idCampaignProduct);
    List<Donation> findByUsers_UsersId(UUID idUser);
}