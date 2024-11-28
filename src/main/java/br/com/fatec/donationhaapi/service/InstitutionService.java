package br.com.fatec.donationhaapi.service;

import java.time.LocalDate;
import java.util.*;

import br.com.fatec.donationhaapi.dto.FileUploadResponse;
import br.com.fatec.donationhaapi.dto.institution.UpdateInstitutionDto;
import br.com.fatec.donationhaapi.exception.BadRequestException;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.fatec.donationhaapi.dto.institution.CreateInstitutionDto;
import br.com.fatec.donationhaapi.dto.institution.CreateInstitutionDtoSimple;
import br.com.fatec.donationhaapi.entity.Institution;
import br.com.fatec.donationhaapi.entity.Users;
import br.com.fatec.donationhaapi.repository.InstitutionRepository;
import br.com.fatec.donationhaapi.utils.FileService;

@Service
public class InstitutionService {
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FileService fileService;

    public List<Institution> getInfoInstitutions() {
        try {
            List<Institution> institutions = institutionRepository.findByDisabledFalse();
            for (Institution institution : institutions) {
                fileService.personalizePhotosUrl(institution);
            }
            return institutions;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar instituições, entre em contato com um Administrador");
        }
    }

    public List<Institution> findByUsers(String idUser) {
        try {
            if (!usersRepository.existsById(UUID.fromString(idUser)))
                throw new NotFoundException("Usuário não encontrado. Id:" + idUser);
            List<Institution> institutions = institutionRepository.findByUsers_UsersId(UUID.fromString(idUser));
            for (Institution institution : institutions) {
                fileService.personalizePhotosUrl(institution);
            }
            return institutions;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar instituições com este usuário, entre em contato com um Administrador");
        }
    }

    public Institution saveInstitutionSimple(CreateInstitutionDtoSimple createInstitutionDtoSimple) {
        try {
            Institution newInstitution = new Institution();
            newInstitution.setComercialName(createInstitutionDtoSimple.comercialName());
            newInstitution.setInstitutionType("Filantrópica");
            Users user = usersRepository.findById(createInstitutionDtoSimple.usersId())
                    .orElseThrow(() -> new NotFoundException(
                            "Usuário não encontrado. Id " + createInstitutionDtoSimple.usersId()));
            Set<Users> userList = new HashSet<Users>();
            userList.add(user);
            newInstitution.setUsers(userList);
            if (validateInstitution(newInstitution)) {
                institutionRepository.saveAndFlush(newInstitution);
                return newInstitution;
            } else {
                throw new BadRequestException("Dados da instituições invalidos");
            }
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar instituição simples, entre em contato com um Administrador");
        }
    }

    public Optional<Institution> findInstitutionById(Long idInstitution) {
        try {
            return Optional.ofNullable(institutionRepository.findById(idInstitution)
                    .map(institution -> fileService.personalizePhotosUrl(institution))
                    .orElseThrow(() -> new NotFoundException("Instituição não encontrada")));
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao encontrar instituição, entre em contato com um Administrador");
        }
    }

    public Institution saveInstitution(CreateInstitutionDto createInstitutionDto, MultipartFile iconFile,
                                       MultipartFile backgroundFile) {
        try {
            Institution newInstitution = extractInstitutionFromDto(createInstitutionDto);

            FileUploadResponse uploadedIcon = fileService.uploadFile(iconFile);
            FileUploadResponse uploadedBackground = fileService.uploadFile(backgroundFile);

            newInstitution.setIconUrl(uploadedIcon.getDownloadUrl());
            newInstitution.setBackgroundUrl(uploadedBackground.getDownloadUrl());

            Users user = usersRepository.findById(createInstitutionDto.usersId()).orElseThrow(
                    () -> new NotFoundException("Usuário não encontrado. Id " + createInstitutionDto.usersId()));
            newInstitution.setUsers(Set.of(user));
            if (validateInstitution(newInstitution)) {
                return institutionRepository.saveAndFlush(newInstitution);
            } else {
                throw new BadRequestException("Dados da instituição inválidos");
            }
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar instituição, entre em contato com um Administrador");
        }
    }

    public Institution updateInstitution(UpdateInstitutionDto updateInstitutionDto, MultipartFile iconFile,
                                         MultipartFile backgroundFile) {
        try {
            Institution oldInstitution = institutionRepository.findById(updateInstitutionDto.institutionId())
                    .orElseThrow(() -> new NotFoundException("Instituição não encontrada."));

            Institution newInstitution = extractInstitutionFromDto(updateInstitutionDto);
            newInstitution.setUsers(oldInstitution.getUsers());
            newInstitution.setCreatedDate(oldInstitution.getCreatedDate());
            newInstitution.setUpdatedDate(LocalDate.now());
            newInstitution.setDisabled(oldInstitution.getDisabled());
            FileUploadResponse uploadedIcon = fileService.uploadFile(iconFile);
            FileUploadResponse uploadedBackground = fileService.uploadFile(backgroundFile);

            newInstitution.setIconUrl(uploadedIcon.getDownloadUrl());
            newInstitution.setBackgroundUrl(uploadedBackground.getDownloadUrl());
            if (validateInstitution(newInstitution)) {
                return institutionRepository.saveAndFlush(newInstitution);
            } else {
                throw new BadRequestException("Dados da instituição inválidos");
            }
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao atualizar instituição, entre em contato com um Administrador");
        }
    }

    private Institution extractInstitutionFromDto(CreateInstitutionDto createInstitutionDto) {
        Institution newInstitution = new Institution();
        newInstitution.setComercialName(createInstitutionDto.comercialName());
        newInstitution.setCpfCnpj(createInstitutionDto.cpfCnpj());
        newInstitution.setInstitutionType(createInstitutionDto.institutionType());
        return newInstitution;
    }

    private Institution extractInstitutionFromDto(UpdateInstitutionDto updateInstitutionDto) {
        Institution newInstitution = new Institution();
        newInstitution.setInstitutionId(updateInstitutionDto.institutionId());
        newInstitution.setComercialName(updateInstitutionDto.comercialName());
        newInstitution.setCpfCnpj(updateInstitutionDto.cpfCnpj());
        newInstitution.setInstitutionType(updateInstitutionDto.institutionType());
        newInstitution.setDescription(updateInstitutionDto.description());
        return newInstitution;
    }

    public HashMap<String, Object> inactivateInstitution(Long idInstitution) {
        try {
            Optional<Institution> opt = Optional.ofNullable(institutionRepository.findById(idInstitution)
                    .orElseThrow(() -> new BadRequestException("Instituição não encontrada")));
            Institution institution = opt.get();
            institution.setDisabled(true);
            institutionRepository.save(institution);

            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("message", "Instituição: " + institution.getComercialName() + " foi inativado");
            return result;
            /*
             * Optional<Customer> customer =
             * Optional.ofNullable(customerRepository.findById(idCustomer).
             * orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
             * "Cliente não encontrado!")));
             *
             * customerRepository.delete(customer.get());
             * HashMap<String, Object> result = new HashMap<String, Object>();
             * result.put("message", "Cliente: " + customer.get().getFirstNameCustomer() +
             * " "
             * + customer.get().getLastNameCustomer() + " excluído com sucesso!");
             * return result;
             */
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao desativar instituição, entre em contato com um Administrador");
        }
    }

    public Boolean validateInstitution(Institution institution) {
        return true;
    }

}