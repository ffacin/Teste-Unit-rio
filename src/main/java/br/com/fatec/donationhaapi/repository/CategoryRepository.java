package br.com.fatec.donationhaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatec.donationhaapi.entity.Category;

public interface CategoryRepository  extends JpaRepository<Category,Long>{
    
}
