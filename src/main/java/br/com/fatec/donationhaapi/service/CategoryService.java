package br.com.fatec.donationhaapi.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.fatec.donationhaapi.entity.Product;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.fatec.donationhaapi.dto.category.CreateCategoryDto;
import br.com.fatec.donationhaapi.dto.category.UpdateCategoryDto;
import br.com.fatec.donationhaapi.entity.Category;
import br.com.fatec.donationhaapi.repository.CategoryRepository;
import br.com.fatec.donationhaapi.repository.ProductRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Category> getInfoCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar categorias, entre em contato com um Administrador");
        }
    }

    public Category saveCategory(CreateCategoryDto createCategoryDto) {
        try {
            Category newCategory = new Category();
            newCategory.setCategoryName(createCategoryDto.getName());
            return categoryRepository.saveAndFlush(newCategory);
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar dados da categoria, entre em contato com um Administrador");
        }
    }

    public Category updateCategory(UpdateCategoryDto updateCategoryDto) {
        try {
            Category oldCategory = categoryRepository.findById(updateCategoryDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
            oldCategory.setCategoryName(updateCategoryDto.getName());
            return categoryRepository.saveAndFlush(oldCategory);
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao atualizar dados da categoria, entre em contato com um Administrador");
        }
    }

    public HashMap<String, Object> deleteCategory(Long categoryId) {
        try {
            Category oldCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
            Set<Category> categories = new HashSet<>();
            categories.add(oldCategory);
            List<Product> productResult = productRepository.findAllByCategoriesIn(categories);
            HashMap<String, Object> result = new HashMap<>();
            if (productResult.size() == 0) {
                categoryRepository.delete(oldCategory);
                result.put("mensagem", "Categoria " + oldCategory.getCategoryName() + " excluído com sucesso.");
            } else {
                result.put("mensagem",
                        "Categoria " + oldCategory.getCategoryName() + " esta sendo usado, não é possível deletar.");
            }
            return result;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao excluir categoria, entre em contato com um Administrador");
        }
    }
}
