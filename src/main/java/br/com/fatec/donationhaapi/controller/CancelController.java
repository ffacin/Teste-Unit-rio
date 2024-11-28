package br.com.fatec.donationhaapi.controller;

import br.com.fatec.donationhaapi.dto.cancel.CancelDto;
import br.com.fatec.donationhaapi.entity.Cancel;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import br.com.fatec.donationhaapi.service.CancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/cancel")
public class CancelController {

    @Autowired
    private CancelService cancelService;

    @PostMapping(value = "/cancelDonationRequest")
    public ResponseEntity<Object> cancelDonationRequest(@RequestBody CancelDto cancelDto) {
        Cancel result = cancelService.cancelDonationRequest(cancelDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }
}
