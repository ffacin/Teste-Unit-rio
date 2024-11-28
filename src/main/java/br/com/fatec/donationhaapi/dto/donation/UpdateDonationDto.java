package br.com.fatec.donationhaapi.dto.donation;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;


public class UpdateDonationDto {

    @Min(value = 1, message = "donationProductId should not be less than 1")
    private Long donationId;

    @Min(value = 1, message = "quantity should not be less than 1")
    private BigDecimal quantity;

    @Min(value = 1, message = "deliveryRulesId should not be less than 1")
    private Long deliveryRulesId;

    private Boolean anonymous;

    public UpdateDonationDto(Long donationId, BigDecimal quantity, Long deliveryRulesId, Boolean anonymous) {
        this.donationId = donationId;
        this.quantity = quantity;
        this.deliveryRulesId = deliveryRulesId;
        this.anonymous = anonymous;
    }

    public UpdateDonationDto() {
    }

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getDeliveryRulesId() {
        return deliveryRulesId;
    }

    public void setDeliveryRulesId(Long deliveryRulesId) {
        this.deliveryRulesId = deliveryRulesId;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }
}
