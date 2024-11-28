package br.com.fatec.donationhaapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DonationRepositoryTest {

    @Test
    void findByCampaignProductId() {
    }

    @Test
    void findActiveNonCancelledDonations() {
    }

    @Test
    void findCompletedDonations() {
    }

    @Test
    void findByCampaignProduct_Campaign_Institution_InstitutionId() {
    }

    @Test
    void findByCampaignProduct_CampaignProductId() {
    }

    @Test
    void findByUsers_UsersId() {
    }
}