package br.com.fatec.donationhaapi.service;

import java.util.Optional;
import java.util.UUID;

import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.fatec.donationhaapi.dto.users.CreateUsersDto;
import br.com.fatec.donationhaapi.dto.users.UpdateUsersDto;
import br.com.fatec.donationhaapi.dto.users.DisableUsersDto;
import br.com.fatec.donationhaapi.entity.Users;
import br.com.fatec.donationhaapi.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Optional<Users> findUsersById(UUID usersId) {
        try {
            return Optional.ofNullable(usersRepository.findById(usersId)
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado!")));
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao buscar usuário, entre em contato com um Administrador");
        }
    }

    public Users saveUsers(CreateUsersDto createUsersDto) {
        try {
            Users newUser = new Users();
            newUser.setName(createUsersDto.getName());
            newUser.setEmail(createUsersDto.getEmail());
            newUser.setPassword(createUsersDto.getPassword());
            newUser.setTelephone(createUsersDto.getTelephone());

            usersRepository.saveAndFlush(newUser);

            return newUser;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar usuário, entre em contato com um Administrador");
        }
    }

    public Users updateUsers(UpdateUsersDto updateUsersDto) {
        try {
            Optional<Users> user = usersRepository.findById(updateUsersDto.getUsersId());
            user.get().setName(updateUsersDto.getName());
            user.get().setEmail(updateUsersDto.getEmail());
            user.get().setPassword(updateUsersDto.getPassword());

            return usersRepository.saveAndFlush(user.get());
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao atualizar usuário, entre em contato com um Administrador");
        }
    }

    public Users disableUsers(DisableUsersDto disableUsersDto) {
        try {
            Optional<Users> user = usersRepository.findById(disableUsersDto.getUsersId());
            user.get().setDisabled(disableUsersDto.getDisabled());

            return usersRepository.saveAndFlush(user.get());
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao desativar usuário, entre em contato com um Administrador");
        }
    }
}