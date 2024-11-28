package br.com.fatec.donationhaapi.controller;

import java.util.HashMap;
import java.util.List;

import br.com.fatec.donationhaapi.dto.deliveryRule.CreateDeliveryRuleDto;
import br.com.fatec.donationhaapi.dto.deliveryRule.UpdateDeliveryRuleDto;
import br.com.fatec.donationhaapi.entity.DeliveryRules;
import br.com.fatec.donationhaapi.service.DeliveryRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;

@RestController
@RequestMapping(value = "api/v1/deliveryRule")
public class DeliveryRuleController {

    @Autowired
    private DeliveryRuleService deliveryRuleService;

    @GetMapping(value = "/list-by-institution/{idInstitution}")
    public ResponseEntity<Object> listDeliveryRulesByInstitution(@PathVariable Long idInstitution) {
        List<DeliveryRules> resultDeliveryRule = deliveryRuleService.getDeliveryRulesInstitution(idInstitution);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultDeliveryRule));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> saveDeliveryRule(@RequestBody CreateDeliveryRuleDto createDeliveryRuleDto) {
        DeliveryRules resultDeliveryRule = deliveryRuleService.saveDeliveryRules(createDeliveryRuleDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultDeliveryRule));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateDeliveryRule(@RequestBody UpdateDeliveryRuleDto updateDeliveryRuleDto) {
        DeliveryRules resultDeliveryRule = deliveryRuleService.updateDeliveryRules(updateDeliveryRuleDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultDeliveryRule));
    }

    @DeleteMapping(value = "/disable")
    public ResponseEntity<Object> disableDeliveryRule(@RequestParam Long deliveryRuleId) {
        HashMap<String, Object> deliveryRuleDisabled = deliveryRuleService.disableDeliveryRule(deliveryRuleId);
        return ResponseEntity.ok().body(ResponseGeneric.response(deliveryRuleDisabled));
    }

    @GetMapping("/find-by-institution/{idInstitution}")
    public ResponseEntity<Object> findDeliveryRuleByInstitution(@PathVariable Long idInstitution) {
        List<DeliveryRules> result = deliveryRuleService.getDeliveryRulesInstitution(idInstitution);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }

}
