package br.com.fatec.donationhaapi.service;

import br.com.fatec.donationhaapi.dto.adress.CreateAddressDto;
import br.com.fatec.donationhaapi.dto.adress.UpdateAddressDto;
import br.com.fatec.donationhaapi.dto.institution.CreateInstitutionDto;
import br.com.fatec.donationhaapi.entity.Address;
import br.com.fatec.donationhaapi.entity.Institution;
import br.com.fatec.donationhaapi.exception.BadRequestException;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.AddressRepository;
import br.com.fatec.donationhaapi.repository.CityRepository;
import br.com.fatec.donationhaapi.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    public List<Address> getAddresses() {
        try {
            return addressRepository.findAll();
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar endereço(s), entre em contato com um Administrador");
        }
    }

    public List<Address> getAddressesByInstitution(Long idInstitution) {
        try {
            Optional.ofNullable(institutionRepository.findById(idInstitution)
                    .orElseThrow(() -> new NotFoundException("Instituição não encontrada.")));
            return addressRepository.findByIdInstitution(idInstitution);
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar endereço(s) para instituição, entre em contato com um Administrador");
        }
    }

    public Address saveAddress(CreateAddressDto createAddressDto) {
        try {
            Address newAddress = extractAddressFromDto(createAddressDto);
            cityRepository.findById(createAddressDto.cityId())
                    .ifPresentOrElse(
                            newAddress::setCity,
                            () -> {
                                throw new NotFoundException("Cidade não encontrada.");
                            });
            institutionRepository.findById(createAddressDto.institutionId())
                    .ifPresentOrElse(newAddress::setInstitution,
                            () -> {
                                throw new NotFoundException("Instituição não encontrada.");
                            });
            if (validateAddress(newAddress)) {
                return addressRepository.saveAndFlush(newAddress);
            } else {
                throw new BadRequestException("Dados de endereço inválidos.");
            }
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar endereço(s), entre em contato com um Administrador");
        }
    }

    public Address updateAddress(UpdateAddressDto updateAddressDto) {
        try {
            Address updateAddress = extractAddressFromDto(updateAddressDto);
            Optional.ofNullable(addressRepository.findById(updateAddressDto.addressId())).ifPresentOrElse(
                    addressResult -> {
                        updateAddress.setAddressId(addressResult.get().getAddressId());
                        updateAddress.setInstitution(addressResult.get().getInstitution());
                    }, () -> {
                        throw new NotFoundException("Endereço não encontrado");
                    });
            cityRepository.findById(updateAddressDto.cityId())
                    .ifPresentOrElse(
                            updateAddress::setCity,
                            () -> {
                                throw new NotFoundException("Cidade não encontrada.");
                            });
            if (validateAddress(updateAddress)) {
                return addressRepository.saveAndFlush(updateAddress);
            } else {
                throw new BadRequestException("Dados de endereço inválidos.");
            }
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao atualizar endereço(s), entre em contato com um Administrador");
        }
    }

    public HashMap<String, Object> disableAddress(Long idAddress) {
        try {
            Optional.ofNullable(addressRepository.findById(idAddress)
                    .orElseThrow(() -> new NotFoundException("Endereço não encontrado.")));
            addressRepository.disableAddress(true, idAddress);
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("mensagem", "Endereço desabilitado.");
            return result;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao desativar endereço(s), entre em contato com um Administrador");
        }
    }

    public boolean validateAddress(Address address) {
        return true;
    }

    private Address extractAddressFromDto(CreateAddressDto createAddressDto) {
        Address newAddress = new Address();
        newAddress.setComplements(createAddressDto.complements());
        newAddress.setDistrict(createAddressDto.district());
        newAddress.setNumber(createAddressDto.number());
        newAddress.setStreet(createAddressDto.street());
        newAddress.setZipCode(createAddressDto.zipCode());
        newAddress.setAddressType(createAddressDto.addressType());
        return newAddress;
    }

    private Address extractAddressFromDto(UpdateAddressDto updateAddressDto) {
        Address newAddress = new Address();
        newAddress.setAddressId(updateAddressDto.addressId());
        newAddress.setComplements(updateAddressDto.complements());
        newAddress.setDistrict(updateAddressDto.district());
        newAddress.setNumber(updateAddressDto.number());
        newAddress.setStreet(updateAddressDto.street());
        newAddress.setZipCode(updateAddressDto.zipCode());
        newAddress.setAddressType(updateAddressDto.addressType());
        return newAddress;
    }
}
