package br.com.fatec.donationhaapi.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.fatec.donationhaapi.dto.adress.CreateAddressDto;
import br.com.fatec.donationhaapi.dto.adress.UpdateAddressDto;
import br.com.fatec.donationhaapi.entity.Address;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import br.com.fatec.donationhaapi.service.AddressService;

@RestController
@RequestMapping(value = "api/v1/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping(value = "/list")
    public ResponseEntity<Object> listAddress() {
        List<Address> resultAddress = addressService.getAddresses();
        return ResponseEntity.ok().body(ResponseGeneric.response(resultAddress));
    }

    @GetMapping(value = "/list-by-institution/{idInstitution}")
    public ResponseEntity<Object> listAddressByInstitution(@PathVariable Long idInstitution) {
        List<Address> resultAddress = addressService.getAddressesByInstitution(idInstitution);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultAddress));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> saveAddress(@RequestBody CreateAddressDto createAddressDto) {
        Address resultAddress = addressService.saveAddress(createAddressDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultAddress));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateAddress(@RequestBody UpdateAddressDto updateAddressDto) {
        Address resultAddress = addressService.updateAddress(updateAddressDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultAddress));
    }

    @DeleteMapping(value = "/disable")
    public ResponseEntity<Object> disableAddress(@RequestParam Long addressId) {
        HashMap<String, Object> addressDisabled = addressService.disableAddress(addressId);

        return ResponseEntity.ok().body(ResponseGeneric.response(addressDisabled));
    }

}
