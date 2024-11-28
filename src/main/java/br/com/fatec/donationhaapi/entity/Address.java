package br.com.fatec.donationhaapi.entity;

import java.io.Serializable;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.fatec.donationhaapi.enums.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "address")
public class Address implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_address")
	private Long addressId;

	@JsonIgnore
	@ManyToOne // Indica que essa é uma relação muitos-para-um
	@JoinColumn(name = "id_institution", foreignKey = @ForeignKey(name = "FK_institution_id_institution_adress"))
	private Institution institution;

	@ManyToOne // Indica que essa é uma relação muitos-para-um
	@JoinColumn(name = "id_city", foreignKey = @ForeignKey(name = "FK_city_id_city_adress")) // Especifica o nome da // coluna na tabela// Endereco que// armazenará a chave// estrangeira
	private City city;

	@Column(name = "address_type", nullable = false)
	private AddressType addressType;

	@Column(name = "street", nullable = false, length = 100)
	@NotBlank(message = "the street field is mandatory")
	@Length(max = 100, message = "The street must be a maximum of 100 characters")
	private String street;

	@Column(name = "zipcode", nullable = false, length = 9)
	@NotBlank(message = "the street field is mandatory")
	@Length(max = 9, message = "The zipCode must be a maximum of 9 characters")
	private String zipCode;

	@Column(name = "number", nullable = false, length = 20)
	@NotBlank(message = "the number field is mandatory")
	@Length(max = 20, message = "The number must be a maximum of 20 characters")
	private String number;

	@Column(name = "complements", nullable = true, length = 100)
	@Length(max = 100, message = "The number must be a maximum of 100 characters")
	private String complements;

	@Column(name = "disabled", nullable = false)
	private boolean disabled;

	@Column(name = "district", nullable = false, length = 130)
	private String district;

	public Address(Long addressId, Institution institution, City city, AddressType addressType, String street, String zipCode, String number, String complements, boolean disabled, String district) {
		this.addressId = addressId;
		this.institution = institution;
		this.city = city;
		this.addressType = addressType;
		this.street = street;
		this.zipCode = zipCode;
		this.number = number;
		this.complements = complements;
		this.disabled = disabled;
		this.district = district;
	}

	public Address() {
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplements() {
		return complements;
	}

	public void setComplements(String complements) {
		this.complements = complements;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@PrePersist
	private void prePersist() {
		this.setDisabled(false);
	}
}
