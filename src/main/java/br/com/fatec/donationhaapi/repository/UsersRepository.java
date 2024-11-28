package br.com.fatec.donationhaapi.repository;


import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatec.donationhaapi.entity.Users;

public interface UsersRepository  extends JpaRepository<Users,UUID>{

}
