package br.com.fatec.donationhaapi.dto.category;

public class UpdateCategoryDto {
    private Long categoryId;
    private String name;

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
