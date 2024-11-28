package br.com.fatec.donationhaapi.controller;

import java.util.HashMap;
import java.util.List;

import br.com.fatec.donationhaapi.dto.campaign.CampaignHistoryDTO;
import br.com.fatec.donationhaapi.dto.campaign.ResponseCampaignDTO;
import br.com.fatec.donationhaapi.dto.campaign.SaveCampaignDTO;
import br.com.fatec.donationhaapi.entity.Campaign;
import br.com.fatec.donationhaapi.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;

@RestController
@RequestMapping(value = "/api/v1/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping(value = "/campaign-request")
    public ResponseEntity<Object> registerCampaignRequest(@RequestBody SaveCampaignDTO saveCampaignDTO) {
        ResponseCampaignDTO resultCampaignRequest = campaignService.saveCampaignRequest(saveCampaignDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGeneric.response(resultCampaignRequest));
    }

    @GetMapping("/history-campaign/{campaignId}")
    public ResponseEntity<Object> findHistoryCampaign(@PathVariable Long campaignId) {
        CampaignHistoryDTO result = campaignService.findHistoryCampaign(campaignId);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @PutMapping(value = "/campaign-update")
    public ResponseEntity<Object> updateCampaignRequest(@RequestBody SaveCampaignDTO saveCampaignDTO) {
        ResponseCampaignDTO resultCampaignRequest = campaignService.saveCampaignRequest(saveCampaignDTO);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultCampaignRequest));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Object> getInfoCampaigns() {
        List<Campaign> resultCampaign = campaignService.getInfoCampaigns();
        return ResponseEntity.ok().body(ResponseGeneric.response(resultCampaign));
    }

    @GetMapping("/list/by-institution/{institutionId}")
    public ResponseEntity<Object> getCampaignsByInstitutionId(@PathVariable Long institutionId) {
        List<Campaign> campaigns = campaignService.getCampaignsByInstitutionId(institutionId);
        return ResponseEntity.ok().body(ResponseGeneric.response(campaigns));
    }

    @GetMapping(value = "/findById/{idCampaign}")
    public ResponseEntity<Object> findCampaignById(@PathVariable Long idCampaign) {
        Campaign resultCampaign = campaignService.findByIdCampaign(idCampaign);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultCampaign));
    }

    @DeleteMapping(value = "/disable/{idCampaign}")
    public ResponseEntity<Object> disableCampaign(@PathVariable Long idCampaign) {
        HashMap<String, Object> resultCampaign = campaignService.disabledCampaign(idCampaign);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultCampaign));
    }

}