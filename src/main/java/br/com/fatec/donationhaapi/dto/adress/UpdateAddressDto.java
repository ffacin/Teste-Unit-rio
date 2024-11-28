package br.com.fatec.donationhaapi.dto.adress;

import br.com.fatec.donationhaapi.enums.AddressType;

public record UpdateAddressDto(Long addressId, Long cityId, AddressType addressType, String street,
                               String zipCode, String number, String complements, String district) {
}
