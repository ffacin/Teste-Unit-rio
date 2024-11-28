package br.com.fatec.donationhaapi.controller;

import br.com.fatec.donationhaapi.dto.institution.CreateInstitutionDto;
import br.com.fatec.donationhaapi.dto.institution.CreateInstitutionDtoSimple;
import br.com.fatec.donationhaapi.dto.institution.UpdateInstitutionDto;
import br.com.fatec.donationhaapi.entity.Institution;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import br.com.fatec.donationhaapi.service.InstitutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/institution")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @GetMapping("/list")
    public ResponseEntity<Object> list() {
        List<Institution> result = institutionService.getInfoInstitutions();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }

    @GetMapping("/list/byUser")
    public ResponseEntity<Object> listByUser(@RequestHeader("idUser") String idUser) {
        List<Institution> result = institutionService.findByUsers(idUser);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }

    @GetMapping("/find/{idInstitution}")
    public ResponseEntity<Object> findInstitutionById(@PathVariable Long idInstitution) {
        Optional<Institution> result = institutionService.findInstitutionById(idInstitution);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGeneric.response(result));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> saveInstitution(@RequestParam("createInstitutionDto") String createInstitutionDtoJson,
                                                  @RequestParam MultipartFile iconFile, @RequestParam MultipartFile backgroundFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CreateInstitutionDto createInstitutionDto = objectMapper.readValue(createInstitutionDtoJson, CreateInstitutionDto.class);
            Institution result = institutionService.saveInstitution(createInstitutionDto, iconFile, backgroundFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGeneric.response(result));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao converter JSON para CreateInstitutionDto");
        }
    }

    @PostMapping("/simpleCreate")
    public ResponseEntity<Object> saveInstitution(
            @RequestBody CreateInstitutionDtoSimple createInstitutionDtoSimple
    ) {
        Institution result = institutionService.saveInstitutionSimple(createInstitutionDtoSimple);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGeneric.response(result));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateInstitution(@RequestParam("updateInstitutionDto") String updateInstitutionDtoJson,
                                                    @RequestParam MultipartFile iconFile, @RequestParam MultipartFile backgroundFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UpdateInstitutionDto updateInstitutionDto = objectMapper.readValue(updateInstitutionDtoJson, UpdateInstitutionDto.class);
            Institution result = institutionService.updateInstitution(updateInstitutionDto, iconFile, backgroundFile);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao converter JSON para CreateInstitutionDto");
        }
    }

    @DeleteMapping("/inactive/{idInstitution}")
    public ResponseEntity<Object> inactiveInstitution(@PathVariable Long idInstitution) {
        HashMap<String, Object> result = institutionService.inactivateInstitution(idInstitution);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }
}
