package br.com.fatec.donationhaapi.controller;

import br.com.fatec.donationhaapi.dto.product.CreateProductDto;
import br.com.fatec.donationhaapi.dto.product.UpdateProductDto;
import br.com.fatec.donationhaapi.entity.Product;
import br.com.fatec.donationhaapi.service.ProductService;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/list")
    public ResponseEntity<Object> listProducts(Pageable pageable) {
        List<Product> result = productService.getInfoProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> saveProduct(
            @RequestParam MultipartFile imgFile, @RequestParam CreateProductDto createproductdto) {
        Product result = productService.saveProduct(imgFile, createproductdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGeneric.response(result));
    }

    @DeleteMapping(value = "/delete/{idProduct}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long idProduct) {
        HashMap<String, Object> result = productService.deleteProduct(idProduct);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/findProduct/{idProduct}")
    public ResponseEntity<Object> getProductById(@PathVariable Long idProduct) {
        Optional<Product> result = productService.findProductById(idProduct);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }

    @GetMapping(value = "/findProduct/byCategory/{idCategory}")
    public ResponseEntity<Object> getProductByCategory(@PathVariable Long idCategory) {
        List<Product> result = productService.findProductByCategory(idCategory);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateProduct(@RequestParam MultipartFile imgFile, @RequestParam UpdateProductDto updateProductDto) {
        Product result = productService.updateProduct(imgFile, updateProductDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(result));
    }
}

