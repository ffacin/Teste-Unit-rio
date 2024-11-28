package br.com.fatec.donationhaapi.entity;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.fatec.donationhaapi.enums.DayOfWeek;
import jakarta.persistence.*;


@Entity
public class DeliveryRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delivery_rules")
    private Long deliveryRulesId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_institution", foreignKey = @ForeignKey(name = "FK_institution_id_institution_delivery_rules"))
    private Institution institution;

    @Column(name = "day_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "start_hour", nullable = false)
    private LocalTime startHour;

    @Column(name = "end_hour", nullable = false)
    private LocalTime endHour;

    @JsonIgnore
    @OneToMany(mappedBy = "deliveryRules", cascade = CascadeType.ALL)
    private List<Donation> donations;

    public DeliveryRules(Long deliveryRulesId, Institution institution, DayOfWeek dayOfWeek, LocalTime startHour, LocalTime endHour, List<Donation> donations) {
        this.deliveryRulesId = deliveryRulesId;
        this.institution = institution;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.donations = donations;
    }

    public DeliveryRules() {
    }

    public Long getDeliveryRulesId() {
        return deliveryRulesId;
    }

    public void setDeliveryRulesId(Long deliveryRulesId) {
        this.deliveryRulesId = deliveryRulesId;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
}


