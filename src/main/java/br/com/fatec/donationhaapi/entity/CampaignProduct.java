package br.com.fatec.donationhaapi.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class CampaignProduct {

    @Id
    @Column(name = "id_campaign_product")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignProductId;

    @ManyToOne
    @JoinColumn(name = "id_campaign", foreignKey = @ForeignKey(name = "FK_campaign_id_campaign_campaign_product"), nullable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "FK_product_id_product_campaign_product"), nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "received", nullable = false, precision = 10, scale = 2)
    private BigDecimal received;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "updated_date", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedDate;

    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @JsonIgnore
    @OneToMany(mappedBy = "campaignProduct", cascade = CascadeType.ALL)
    private List<Donation> donations;

    public CampaignProduct(Long campaignProductId, Campaign campaign, Product product, BigDecimal quantity, BigDecimal received, LocalDate createdDate, LocalDate updatedDate, boolean disabled) {
        this.campaignProductId = campaignProductId;
        this.campaign = campaign;
        this.product = product;
        this.quantity = quantity;
        this.received = received;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.disabled = disabled;
    }

    public CampaignProduct() {
    }

    public Long getCampaignProductId() {
        return campaignProductId;
    }

    public void setCampaignProductId(Long campaignProductId) {
        this.campaignProductId = campaignProductId;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getReceived() {
        return received;
    }

    public void setReceived(BigDecimal received) {
        this.received = received;
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @PrePersist
    private void prePersist() {
        this.setDisabled(false);
        this.setCreatedDate(LocalDate.now());
        this.setUpdatedDate(LocalDate.now());
    }
}