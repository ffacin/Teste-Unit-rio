package br.com.fatec.donationhaapi.repository;

import br.com.fatec.donationhaapi.entity.DeliveryRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeliveryRulesRepository extends JpaRepository<DeliveryRules, Long> {
    List<DeliveryRules> findByInstitutionInstitutionId(Long institutionId);
    @Transactional
    @Modifying
    @Query(value = "update delivery_rules set disabled =:disabled where id_delivery_rules =:id_delivery_rules", nativeQuery = true)
    void disableDeliveryRule(@Param("disabled") Boolean disabled, @Param("id_delivery_rules") Long deliveryRulesId);
}
