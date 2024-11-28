package br.com.fatec.donationhaapi.controller;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.fatec.donationhaapi.dto.category.CreateCategoryDto;
import br.com.fatec.donationhaapi.dto.category.UpdateCategoryDto;
import br.com.fatec.donationhaapi.entity.Category;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import br.com.fatec.donationhaapi.service.CategoryService;

@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "/create")
    public ResponseEntity<Object> createCategory(@RequestBody CreateCategoryDto createCategoryDto) {

        Category categoryResult = categoryService.saveCategory(createCategoryDto);

        return ResponseEntity.ok().body(ResponseGeneric.response(categoryResult));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Object> listCategories() {

        List<Category> categories = categoryService.getInfoCategories();

        return ResponseEntity.ok().body(ResponseGeneric.response(categories));

    }

    @DeleteMapping(value = "/delete/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) {

        HashMap<String, Object> result = categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto) {

        Category result = categoryService.updateCategory(updateCategoryDto);

        return ResponseEntity.ok().body(ResponseGeneric.response(result));
    }
}
