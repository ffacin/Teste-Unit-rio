package br.com.fatec.donationhaapi.dto.adress;

import br.com.fatec.donationhaapi.enums.AddressType;

public record CreateAddressDto(Long institutionId, Long cityId, AddressType addressType, String street, String zipCode,
                               String number, String complements, String district
) {
}
