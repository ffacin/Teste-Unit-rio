package br.com.fatec.donationhaapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CampaignProductRepositoryTest {

    @Test
    void findByProduct() {
    }

    @Test
    void findByCampaign_CampaignId() {
    }

    @Test
    void findByDisabledFalse() {
    }

    @Test
    void findByDisabledFalseAndCampaign_Institution_ComercialNameContainsIgnoreCase() {
    }

    @Test
    void findByCampaign_Institution_InstitutionIdAndDisabledFalse() {
    }

    @Test
    void findHistoryCampaignProduct() {
    }
}