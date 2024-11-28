package br.com.fatec.donationhaapi.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Campaign {

    @Id
    @Column(name = "id_campaign")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    @Column(name = "campaign_ending", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate campaignEnding;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "updated_date", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedDate;

    @Column(name = "campaign_description", nullable = true, length = 250)
    private String campaignDescription;

    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @ManyToOne
    @JoinColumn(name = "id_institution", foreignKey = @ForeignKey(name = "FK_institution_id_institution_campaign"))
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users user;

    @JsonIgnore
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<CampaignProduct> campaignProducts;

    public Campaign(Long campaignId, LocalDate campaignEnding, LocalDate createdDate, LocalDate updatedDate, String campaignDescription, boolean disabled, Institution institution, Users user, List<CampaignProduct> campaignProducts, List<Donation> donations) {
        this.campaignId = campaignId;
        this.campaignEnding = campaignEnding;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.campaignDescription = campaignDescription;
        this.disabled = disabled;
        this.institution = institution;
        this.user = user;
        this.campaignProducts = campaignProducts;
    }

    public Campaign() {
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public LocalDate getCampaignEnding() {
        return campaignEnding;
    }

    public void setCampaignEnding(LocalDate campaignEnding) {
        this.campaignEnding = campaignEnding;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<CampaignProduct> getCampaignProducts() {
        return campaignProducts;
    }

    public void setCampaignProducts(List<CampaignProduct> campaignProducts) {
        this.campaignProducts = campaignProducts;
    }

    @PrePersist
    private void prePersist() {
        this.setDisabled(false);
        this.setCreatedDate(LocalDate.now());
        this.setUpdatedDate(LocalDate.now());
    }
}
