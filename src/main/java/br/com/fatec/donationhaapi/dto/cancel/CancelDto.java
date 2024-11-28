package br.com.fatec.donationhaapi.dto.cancel;

import br.com.fatec.donationhaapi.entity.Donation;
import br.com.fatec.donationhaapi.enums.ReasonCanceled;

public class CancelDto {

    private Long cancelId;

    private ReasonCanceled reason;

    private String description;

    private Long donationId;

    public CancelDto() {
    }

    public CancelDto(Long cancelId, ReasonCanceled reason, String description, Long donationId) {
        this.cancelId = cancelId;
        this.reason = reason;
        this.description = description;
        this.donationId = donationId;
    }

    public Long getCancelId() {
        return cancelId;
    }

    public void setCancelId(Long cancelId) {
        this.cancelId = cancelId;
    }

    public ReasonCanceled getReason() {
        return reason;
    }

    public void setReason(ReasonCanceled reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }
}
