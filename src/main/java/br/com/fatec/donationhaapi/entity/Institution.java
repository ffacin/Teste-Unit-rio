package br.com.fatec.donationhaapi.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "institution")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_institution")
    private Long institutionId;

    @Column(name = "comercial_name", nullable = false)
    @NotBlank(message = "the comercialName field is mandatory")
    @Length(max = 100, message = "The comercialName must be a maximum of 100 characters")
    private String comercialName;

    @Column(name = "cpfCnpj", nullable = true, length = 20)
    private String cpfCnpj;

    @Column(name = "description", nullable = true, length = 2500)
    private String description;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "updated_date", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedDate;

    @Column(name = "institution_type", nullable = false)
    private String institutionType;

    @Column(name = "icon_url", nullable = true)
    private String iconUrl;

    @Column(name = "background_url", nullable = true)
    private String backgroundUrl;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Address> address;
    
    @JsonIgnore
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Campaign> campaigns;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<DeliveryRules> deliveryRules;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "institution_users", joinColumns = @JoinColumn(name = "id_institution", foreignKey = @ForeignKey(name = "FK_users_id_users")), inverseJoinColumns = @JoinColumn(name = "id_users", foreignKey = @ForeignKey(name = "FK_institution_id_institution")))
    private Set<Users> users;

    public Institution(Long institutionId, String comercialName, String cpfCnpj, String description, Boolean disabled, LocalDate createdDate, LocalDate updatedDate, String institutionType, String iconUrl, String backgroundUrl, List<Address> address, List<Campaign> campaigns, List<DeliveryRules> deliveryRules, Set<Users> users) {
        this.institutionId = institutionId;
        this.comercialName = comercialName;
        this.cpfCnpj = cpfCnpj;
        this.description = description;
        this.disabled = disabled;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.institutionType = institutionType;
        this.iconUrl = iconUrl;
        this.backgroundUrl = backgroundUrl;
        this.address = address;
        this.campaigns = campaigns;
        this.deliveryRules = deliveryRules;
        this.users = users;
    }

    public Institution() {
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public String getComercialName() {
        return comercialName;
    }

    public void setComercialName(String comercialName) {
        this.comercialName = comercialName;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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

    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public List<DeliveryRules> getDeliveryRules() {
        return deliveryRules;
    }

    public void setDeliveryRules(List<DeliveryRules> deliveryRules) {
        this.deliveryRules = deliveryRules;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    @PrePersist
    private void prePersist() {
        this.setUpdatedDate(LocalDate.now());
        this.setCreatedDate(LocalDate.now());
        this.setDisabled(false);
    }


}
