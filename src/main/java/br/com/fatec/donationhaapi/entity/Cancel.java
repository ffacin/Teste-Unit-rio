package br.com.fatec.donationhaapi.entity;

import br.com.fatec.donationhaapi.enums.ReasonCanceled;
import jakarta.persistence.*;

@Entity
public class Cancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancel")
    private Long cancelId;

    @Column(name = "reason")
    private ReasonCanceled reason;

    @Column(name = "description")
    private String description;

    @OneToOne
    private Donation donation;

    public Cancel() {
    }

    public Cancel(Long cancelId, ReasonCanceled reason, String description, Donation donation) {
        this.cancelId = cancelId;
        this.reason = reason;
        this.description = description;
        this.donation = donation;
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

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }
}
