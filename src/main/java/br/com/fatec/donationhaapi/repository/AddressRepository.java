package br.com.fatec.donationhaapi.repository;

import br.com.fatec.donationhaapi.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(value = "SELECT * FROM address WHERE id_institution = ?1", nativeQuery = true)
    List<Address> findByIdInstitution(Long idInstitution);

    @Transactional
    @Modifying
    @Query(value = "update address set disabled =:disabled where id_address =:id_address", nativeQuery = true)
    void disableAddress(@Param("disabled") Boolean disabled, @Param("id_address") Long addressId);

}
