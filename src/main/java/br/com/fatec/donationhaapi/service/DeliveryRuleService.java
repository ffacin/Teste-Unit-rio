package br.com.fatec.donationhaapi.service;

import br.com.fatec.donationhaapi.dto.deliveryRule.CreateDeliveryRuleDto;
import br.com.fatec.donationhaapi.dto.deliveryRule.UpdateDeliveryRuleDto;
import br.com.fatec.donationhaapi.entity.DeliveryRules;
import br.com.fatec.donationhaapi.exception.BadRequestException;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.DeliveryRulesRepository;
import br.com.fatec.donationhaapi.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryRuleService {

    @Autowired
    private DeliveryRulesRepository deliveryRulesRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    public List<DeliveryRules> getDeliveryRulesInstitution(Long idInstitution) {
        try {
            return deliveryRulesRepository.findByInstitutionInstitutionId(idInstitution);
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar endereço(s), entre em contato com um Administrador");
        }
    }

    public DeliveryRules saveDeliveryRules(CreateDeliveryRuleDto createDeliveryRuleDto) {
        try {
            DeliveryRules newDeliveryRules = extractDeliveryRuleFromDto(createDeliveryRuleDto);
            institutionRepository.findById(createDeliveryRuleDto.institutionId())
                    .ifPresentOrElse(newDeliveryRules::setInstitution,
                            () -> {
                                throw new NotFoundException("Instituição não encontrada.");
                            });
            return deliveryRulesRepository.saveAndFlush(newDeliveryRules);
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar endereço(s), entre em contato com um Administrador");
        }
    }

    public DeliveryRules updateDeliveryRules(UpdateDeliveryRuleDto updateDeliveryRuleDto) {
        try {
            DeliveryRules updateDeliveryRules = extractDeliveryRuleFromDto(updateDeliveryRuleDto);
            Optional.ofNullable(deliveryRulesRepository.findById(updateDeliveryRuleDto.deliveryRuleId())).ifPresentOrElse(
                    deliveryRulesResult -> {
                        updateDeliveryRules.setDeliveryRulesId(deliveryRulesResult.get().getDeliveryRulesId());
                        updateDeliveryRules.setInstitution(deliveryRulesResult.get().getInstitution());
                    }, () -> {
                        throw new NotFoundException("Endereço não encontrado");
                    });
            return deliveryRulesRepository.saveAndFlush(updateDeliveryRules);
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao atualizar endereço(s), entre em contato com um Administrador");
        }
    }

    public HashMap<String, Object> disableDeliveryRule(Long idDeliveryRule) {
        try {
            Optional.ofNullable(deliveryRulesRepository.findById(idDeliveryRule)
                    .orElseThrow(() -> new NotFoundException("Endereço não encontrado.")));
            deliveryRulesRepository.disableDeliveryRule(true, idDeliveryRule);
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("mensagem", "Endereço desabilitado.");
            return result;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao desativar endereço(s), entre em contato com um Administrador");
        }
    }

    private DeliveryRules extractDeliveryRuleFromDto(CreateDeliveryRuleDto createDeliveryRuleDto) {
        DeliveryRules deliveryRules = new DeliveryRules();
        deliveryRules.setDayOfWeek(createDeliveryRuleDto.dayOfWeek());
        deliveryRules.setStartHour(createDeliveryRuleDto.startHour());
        deliveryRules.setEndHour(createDeliveryRuleDto.endHour());
        return deliveryRules;
    }

    private DeliveryRules extractDeliveryRuleFromDto(UpdateDeliveryRuleDto updateDeliveryRuleDto) {
        DeliveryRules deliveryRules = new DeliveryRules();
        deliveryRules.setDayOfWeek(updateDeliveryRuleDto.dayOfWeek());
        deliveryRules.setStartHour(updateDeliveryRuleDto.startHour());
        deliveryRules.setEndHour(updateDeliveryRuleDto.endHour());
        return deliveryRules;
    }
}
