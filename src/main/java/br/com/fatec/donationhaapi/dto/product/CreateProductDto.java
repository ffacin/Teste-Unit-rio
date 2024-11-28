package br.com.fatec.donationhaapi.dto.product;

import java.util.Set;
import br.com.fatec.donationhaapi.enums.Unit;

public class CreateProductDto {
    private String name;
    private String description;
    private Unit unit;
    private Set<Long> categories;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public CreateProductDto() {
    }
    public Set<Long> getCategories() {
        return categories;
    }
    public void setCategories(Set<Long> categories) {
        this.categories = categories;
    }

}
