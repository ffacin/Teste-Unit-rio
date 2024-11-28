package br.com.fatec.donationhaapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private long cityId;

    @Column(name = "name", nullable = false, length = 30)
    @NotBlank(message = "the name field is mandatory")
    private String name;

    @Column(name = "uf", nullable = false, length = 2)
    @NotBlank(message = "the uf field is mandatory")
    private String uf;

    @JsonIgnore
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Address> address;

    public City(long cityId, String name, String uf, List<Address> address) {
        this.cityId = cityId;
        this.name = name;
        this.uf = uf;
        this.address = address;
    }

    public City() {
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
