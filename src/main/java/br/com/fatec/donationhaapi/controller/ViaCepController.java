package br.com.fatec.donationhaapi.controller;

import br.com.fatec.donationhaapi.entity.Address;
import br.com.fatec.donationhaapi.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/viacep")
public class ViaCepController {

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping("/{zipeCode}")
    public String getZipeCode(@PathVariable String zipeCode) {
        return viaCepService.getZipeCode(zipeCode);
    }

}
