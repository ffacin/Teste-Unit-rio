package br.com.fatec.donationhaapi.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.com.fatec.donationhaapi.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
public class Users {

    @Id
    @GeneratedValue
    @Column(name = "id_users")
    private UUID usersId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Email
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 3000)
    @JsonIgnore
    private String password;

    @Column(name = "telephone", length = 15)
    private String telephone;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "updated_date", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedDate;
    
    @Column(name = "role", nullable = false)
    private Role role;
    
    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Donation> donations;
   
    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Institution> institutions = new HashSet<>();

    public Users(UUID usersId, String name, String email, String password, String telephone, Boolean disabled, LocalDate createdDate, LocalDate updatedDate, Role role, List<Donation> donations, Set<Institution> institutions) {
        this.usersId = usersId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.disabled = disabled;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.role = role;
        this.donations = donations;
        this.institutions = institutions;
    }

    public Users() {
    }

    public UUID getUsersId() {
        return usersId;
    }

    public void setUsersId(UUID usersId) {
        this.usersId = usersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    public Set<Institution> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(Set<Institution> institutions) {
        this.institutions = institutions;
    }

    @PrePersist
    private void prePersist() {
        this.setUpdatedDate(LocalDate.now());
        this.setCreatedDate(LocalDate.now());
        this.setDisabled(false);
    }
}
