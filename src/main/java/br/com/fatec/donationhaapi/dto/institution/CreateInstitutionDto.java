package br.com.fatec.donationhaapi.dto.institution;

import java.util.UUID;

public record CreateInstitutionDto(String comercialName, String cpfCnpj, String institutionType,
                                   String description, UUID usersId) {
}
