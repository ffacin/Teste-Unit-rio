package br.com.fatec.donationhaapi.controller;

import br.com.fatec.donationhaapi.dto.donation.CreateDonationDto;
import br.com.fatec.donationhaapi.dto.donation.UpdateDonationDto;
import br.com.fatec.donationhaapi.entity.Donation;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import br.com.fatec.donationhaapi.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/donation")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @DeleteMapping(value = "/delete/{donationId}")
    public ResponseEntity<Object> deleteDonationRequest(@PathVariable Long donationId) {
        HashMap<String, Object> result = donationService.deleteDonationRequest(donationId);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @PostMapping(value = "/donation-request")
    public ResponseEntity<Object> saveDonationReceipt(
            @RequestBody CreateDonationDto createDonationDto) {
        Donation result = donationService.createDonationRequest(createDonationDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/list/findNonCancelledDonations")
    public ResponseEntity<Object> findActiveNonCancelledDonations() {
        List<Donation> result = donationService.findActiveNonCancelledDonations();
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/list/findCompletedDonations")
    public ResponseEntity<Object> findCompletedDonations() {
        List<Donation> result = donationService.findCompletedDonations();
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/list/byInstitution/{idInstitution}")
    public ResponseEntity<Object> findDonationsByInstitution(@PathVariable Long idInstitution) {
        List<Donation> result = donationService.findDonationByInstitution(idInstitution);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/list/byCampaignProduct/{idCampaignProduct}")
    public ResponseEntity<Object> findDonationsByCampaignProduct(@PathVariable Long idCampaignProduct) {
        List<Donation> result = donationService.findDonationByCampaignProduct(idCampaignProduct);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/list/byUser")
    public ResponseEntity<Object> findDonationByUser(@RequestHeader("idUser") String idUser) {
        List<Donation> result = donationService.findDonationByUser(idUser);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/find/{idDonation}")
    public ResponseEntity<Object> findById(@PathVariable Long idDonation) {
        Donation result = donationService.findById(idDonation);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @PatchMapping(value = "/receipt", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> donationReceipt(@RequestParam Long idDonation, @RequestParam MultipartFile file,
            @RequestHeader("idUser") String idUser) {
        Donation result = donationService.donationReceipt(idDonation, file, idUser);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @PatchMapping(value = "/confirm-appointment")
    public ResponseEntity<Object> donationConfirmAppointment(@RequestParam Long idDonation, @RequestHeader("idUser") String idUser) {
        Donation result = donationService.donationConfirmAppointment(idDonation, idUser);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateDonation(@RequestBody UpdateDonationDto updateDonationDto) {
        Donation result = donationService.updateDonation(updateDonationDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }
}
