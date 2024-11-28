package br.com.fatec.donationhaapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CampaignRepositoryTest {

    @Test
    void findByDisabledFalse() {
    }

    @Test
    void findByInstitution_InstitutionIdAndDisabledFalse() {
    }

    @Test
    void findHistoryCampaign() {
    }
}