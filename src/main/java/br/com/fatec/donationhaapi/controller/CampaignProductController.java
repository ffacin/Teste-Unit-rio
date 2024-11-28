package br.com.fatec.donationhaapi.controller;

import br.com.fatec.donationhaapi.dto.campaign.CampaignProductHistoryDTO;
import br.com.fatec.donationhaapi.dto.campaign.UpdateCampaignProductDTO;
import br.com.fatec.donationhaapi.entity.CampaignProduct;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import br.com.fatec.donationhaapi.service.CampaignProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/campaignProduct")
public class CampaignProductController {

    @Autowired
    private CampaignProductService campaignProductService;

    @GetMapping("/findCampaignProductById/{campaignProductId}")
    public ResponseEntity<Object> findDonationProductById(@PathVariable Long campaignProductId) {
        CampaignProduct result = campaignProductService.findCampaignProductById(campaignProductId);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping("/findCampaignProductByCampaignId/{campaignId}")
    public ResponseEntity<Object> findByIdCampaignCampaignsProducts(@PathVariable Long campaignId) {
        List<CampaignProduct> result = campaignProductService.findByIdCampaignCampaignsProducts(campaignId);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping("/list/by-institution/{institutionId}")
    public ResponseEntity<Object> getCampaignProductsByInstitutionId(@PathVariable Long institutionId) {
        List<CampaignProduct> campaignProducts = campaignProductService.getCampaignProductsByInstitutionId(institutionId);
        return ResponseEntity.ok().body(ResponseGeneric.response(campaignProducts));
    }

    @GetMapping("/history-campaign-product/{campaignProductId}")
    public ResponseEntity<Object> findHistoryCampaignProduct(@PathVariable Long campaignProductId) {
        CampaignProductHistoryDTO result = campaignProductService.findHistoryCampaignProduct(campaignProductId);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Object> getCampaignsProducts(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "endDate") String sortMethod,
            @RequestParam(name = "search", required = false, defaultValue = "") String search) {
        Page<CampaignProduct> resultCampaign = campaignProductService.getInfoCampaignsProducts(page, size, sortMethod, search);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultCampaign));
    }


    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateCampaignProduct(@RequestBody UpdateCampaignProductDTO updateCampaignProductDTO) {
        CampaignProduct campaignProduct = campaignProductService.saveCampaignProduct(updateCampaignProductDTO.saveCampaignProductDTO(), updateCampaignProductDTO.idCampaign());
        return ResponseEntity.ok().body(ResponseGeneric.response(campaignProduct));
    }

    @DeleteMapping(value = "/disable/{idCampaignProduct}")
    public ResponseEntity<Object> disableCampaignProduct(@PathVariable Long idCampaignProduct) {
        Boolean result = campaignProductService.disabled(idCampaignProduct);
        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }
}
