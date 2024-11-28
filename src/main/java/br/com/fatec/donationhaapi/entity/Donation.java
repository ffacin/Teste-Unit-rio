package br.com.fatec.donationhaapi.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.fatec.donationhaapi.enums.StatusDonation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;


@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donation")
    private Long donationId;

    @ManyToOne
    @JoinColumn(name = "id_campaign_product", foreignKey = @ForeignKey(name = "FK_campaign_product_id_campaign_product_donation"))
    private CampaignProduct campaignProduct;

    @ManyToOne
    @JoinColumn(name = "id_delivery_rules", foreignKey = @ForeignKey(name = "FK_delivery_rules_id_delivery_rules_donation"))
    private DeliveryRules deliveryRules;

    @ManyToOne
    @JoinColumn(name = "id_users", foreignKey = @ForeignKey(name = "FK_users_id_users_donation"))
    private Users users;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "updated_date", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedDate;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "comment")
    private String comment;

    @Column(name = "status_donation", nullable = false)
    private StatusDonation statusDonation;

    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous;

    public Donation(Long donationId, CampaignProduct campaignProduct, DeliveryRules deliveryRules, Users users, BigDecimal quantity, LocalDate createdDate, LocalDate updatedDate, Boolean disabled, String imgUrl, String comment, StatusDonation statusDonation, Boolean anonymous) {
        this.donationId = donationId;
        this.campaignProduct = campaignProduct;
        this.deliveryRules = deliveryRules;
        this.users = users;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.disabled = disabled;
        this.imgUrl = imgUrl;
        this.comment = comment;
        this.statusDonation = statusDonation;
        this.anonymous = anonymous;
    }

    public Donation() {
    }

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public CampaignProduct getCampaignProduct() {
        return campaignProduct;
    }

    public void setCampaignProduct(CampaignProduct campaignProduct) {
        this.campaignProduct = campaignProduct;
    }

    public DeliveryRules getDeliveryRules() {
        return deliveryRules;
    }

    public void setDeliveryRules(DeliveryRules deliveryRules) {
        this.deliveryRules = deliveryRules;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public StatusDonation getStatusDonation() {
        return statusDonation;
    }

    public void setStatusDonation(StatusDonation statusDonation) {
        this.statusDonation = statusDonation;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    @PrePersist
    private void prePersist() {
        this.setDisabled(false);
        this.setUpdatedDate(LocalDate.now());
        this.setCreatedDate(LocalDate.now());
    }
}

