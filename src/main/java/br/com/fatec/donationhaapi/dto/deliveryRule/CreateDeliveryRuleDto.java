package br.com.fatec.donationhaapi.dto.deliveryRule;

import br.com.fatec.donationhaapi.enums.DayOfWeek;

import java.time.LocalTime;

public record CreateDeliveryRuleDto(Long institutionId, DayOfWeek dayOfWeek, LocalTime startHour, LocalTime endHour) {
}
