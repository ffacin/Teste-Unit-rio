package br.com.fatec.donationhaapi.dto.deliveryRule;

import br.com.fatec.donationhaapi.enums.DayOfWeek;

import java.time.LocalTime;

public record UpdateDeliveryRuleDto(Long deliveryRuleId, DayOfWeek dayOfWeek, LocalTime startHour, LocalTime endHour) {
}