package br.com.fatec.donationhaapi.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.fatec.donationhaapi.enums.Unit;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long productId;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    @NotBlank(message = "the name field is mandatory")
    @Length(max = 300, message = "The name must be a maximum of 100 characters")
    private String name;

    @Column(name = "description", nullable = true, length = 300)
    @Length(max = 300, message = "The description must be a maximum of 300 characters")
    private String description;

    @Column(name = "unit", nullable = false)
    private Unit unit;

    @Column(name = "imgUrl", nullable = false)
    private String imgUrl;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "FK_product_id_product")), inverseJoinColumns = @JoinColumn(name = "id_category", foreignKey = @ForeignKey(name = "FK_category_id_category")))
    private Set<Category> categories;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "updated_date", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedDate;

    @Column(name = "disabled")
    private Boolean disabled;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CampaignProduct> campaignProducts;

    public Product(Long productId, String name, String description, Unit unit, String imgUrl, Set<Category> categories, LocalDate createdDate, LocalDate updatedDate, Boolean disabled, List<CampaignProduct> campaignProducts) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.unit = unit;
        this.imgUrl = imgUrl;
        this.categories = categories;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.disabled = disabled;
        this.campaignProducts = campaignProducts;
    }

    public Product() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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

    public List<CampaignProduct> getCampaignProducts() {
        return campaignProducts;
    }

    public void setCampaignProducts(List<CampaignProduct> campaignProducts) {
        this.campaignProducts = campaignProducts;
    }

    @PrePersist
    private void prePersist() {
        this.setUpdatedDate(LocalDate.now());
        this.setCreatedDate(LocalDate.now());
        this.setDisabled(false);
    }
}
