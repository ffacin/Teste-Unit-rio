package br.com.fatec.donationhaapi.repository;

import br.com.fatec.donationhaapi.entity.Cancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelRepository extends JpaRepository<Cancel, Long> {
}
