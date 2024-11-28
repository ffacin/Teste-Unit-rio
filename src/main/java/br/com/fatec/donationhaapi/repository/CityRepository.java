package br.com.fatec.donationhaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatec.donationhaapi.entity.City;

public interface CityRepository extends JpaRepository<City,Long> {
    
}
