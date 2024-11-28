package br.com.fatec.donationhaapi.dto.institution;

import br.com.fatec.donationhaapi.dto.adress.UpdateAddressDto;

import java.util.UUID;

public record UpdateInstitutionDto(Long institutionId, String comercialName, String cpfCnpj, String institutionType,
                                   String description, UUID usersId) {
}
