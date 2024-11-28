package br.com.fatec.donationhaapi.dto.donation;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.Min;


public class CreateDonationDto {

    @Min(value = 1, message = "institutionId should not be less than 1")
    private Long institutionId;

    @org.hibernate.validator.constraints.UUID(message = "userId invalid")
    private UUID userId;

    @Min(value = 1, message = "campaignProductId should not be less than 1")
    private Long campaignProductId;

    @Min(value = 1, message = "quantity should not be less than 1")
    private BigDecimal quantity;

    @Min(value = 1, message = "deliveryRulesId should not be less than 1")
    private Long deliveryRulesId;

    private Boolean anonymous;

    private String comment;

    public CreateDonationDto(Long institutionId, UUID userId, Long campaignProductId, BigDecimal quantity, Long deliveryRulesId, Boolean anonymous, String comment) {
        this.institutionId = institutionId;
        this.userId = userId;
        this.campaignProductId = campaignProductId;
        this.quantity = quantity;
        this.deliveryRulesId = deliveryRulesId;
        this.anonymous = anonymous;
        this.comment = comment;
    }

    public CreateDonationDto() {
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Long getCampaignProductId() {
        return campaignProductId;
    }

    public void setCampaignProductId(Long campaignProductId) {
        this.campaignProductId = campaignProductId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
