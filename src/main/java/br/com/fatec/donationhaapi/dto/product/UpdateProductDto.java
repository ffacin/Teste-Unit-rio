package br.com.fatec.donationhaapi.dto.product;

import java.util.Set;
import br.com.fatec.donationhaapi.entity.Category;
import br.com.fatec.donationhaapi.enums.Unit;

public class UpdateProductDto {
     private Long productId;
     private String name;
     private String description;
     private Unit unit;
     private Set<Category> categories;

     public void setProductId(Long productId) {
          this.productId = productId;
     }

     public Long getProductId() {
          return productId;
     }

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

     public void setCategories(Set<Category> categories) {
          this.categories = categories;
     }

     public Set<Category> getCategories() {
          return categories;
     }

     public UpdateProductDto() {

     }

     public UpdateProductDto(Long ProductId, String name,
               String description, Unit unit, Set<Category> categories) {
          this.productId = ProductId;
          this.name = name;
          this.description = description;
          this.unit = unit;
          this.categories = categories;
     }

}
