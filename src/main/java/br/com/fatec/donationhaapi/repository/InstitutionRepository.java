package br.com.fatec.donationhaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatec.donationhaapi.entity.Institution;

import java.util.List;
import java.util.UUID;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    List<Institution> findByDisabledFalse();

    List<Institution> findByUsers_UsersId(UUID idUser);
}
