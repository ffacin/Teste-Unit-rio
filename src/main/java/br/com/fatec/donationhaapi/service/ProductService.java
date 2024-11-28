package br.com.fatec.donationhaapi.service;

import br.com.fatec.donationhaapi.dto.product.CreateProductDto;
import br.com.fatec.donationhaapi.dto.product.UpdateProductDto;
import br.com.fatec.donationhaapi.entity.Category;
import br.com.fatec.donationhaapi.entity.Product;
import br.com.fatec.donationhaapi.exception.BadRequestException;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.CampaignProductRepository;
import br.com.fatec.donationhaapi.repository.CategoryRepository;
import br.com.fatec.donationhaapi.repository.ProductRepository;
import br.com.fatec.donationhaapi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CampaignProductRepository campaignProductRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileService fileService;

    public List<Product> getInfoProducts(Pageable pageable) {
        try {
            pageable.getPageNumber();
            // var s = productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
            Page<Product> productPage = productRepository.findAll(pageable);
            for (Product product : productPage) {
                fileService.personalizePhotosUrl(product);
            }
            return productPage.getContent(); // productRepository.findAll();
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao listar os produtos, entre em contato com um Administrador");
        }
    }

    public Product saveProduct(MultipartFile imgFile, CreateProductDto createProductDto) {
        try {
            Product newProduct = new Product();
            newProduct.setName(createProductDto.getName());
            newProduct.setDescription(createProductDto.getDescription());
            newProduct.setUnit(createProductDto.getUnit());
            
            Set<Category> categories = new HashSet<Category>();
                for (Long ids : createProductDto.getCategories()) {
                    Category category = new Category();
                    category.setCategoryId(ids);
                    categories.add(category );
                }
            newProduct.setCategories(categories);
            newProduct.setImgUrl(fileService.uploadFile(imgFile).getDownloadUrl());

            if (validateProduct(newProduct)) {
                return productRepository.saveAndFlush(newProduct);
            } else {
                throw new BadRequestException("O produto não é valido.");
            }
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar produto, entre em contato com um Administrador");
        }
    }

    public HashMap<String, Object> deleteProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
            var donationResult = campaignProductRepository.findByProduct(product);
            if (donationResult == null) {
                productRepository.delete(product);
                HashMap<String, Object> result = new HashMap<>();
                result.put("message", "Produto " + product.getName() + " excluído com sucesso!");
                return result;
            } else {
                product.setDisabled(true);
                productRepository.saveAndFlush(product);

                HashMap<String, Object> result = new HashMap<>();
                result.put("message", "Produto " + product.getName()
                        + " poussui doações, não é possível excluir, ele foi desativado.");
                return result;
            }
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao excluir produto, entre em contato com um Administrador ");
        }
    }

    public Optional<Product> findProductById(Long productId) {
        try {
            return Optional.ofNullable(
                    productRepository.findById(productId).map(product -> fileService.personalizePhotosUrl(product))
                            .orElseThrow(() -> new NotFoundException("Produto não encontrado.")));
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao encontrar produto, entre em contato com um Administrador");
        }
    }

    public List<Product> findProductByCategory(Long categoryId) {
        try {
            if (!categoryRepository.existsById(categoryId))
                throw new NotFoundException("Categoria não encontrada. Id: " + categoryId);
            List<Product> products =  Optional.ofNullable(productRepository.listProductByCategory(categoryId))
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
            for(Product product : products){
                fileService.personalizePhotosUrl(product);
            }
            return products;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao encontrar produto, entre em contato com um Administrador");
        }
    }

    public Product updateProduct(MultipartFile imgFile, UpdateProductDto updateProductDto) {
        try {
            Product oldProduct = productRepository.findById(updateProductDto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
            oldProduct.setName(updateProductDto.getName());
            oldProduct.setDescription(updateProductDto.getDescription());
            oldProduct.setUnit(updateProductDto.getUnit());
            oldProduct.setCategories(updateProductDto.getCategories());
            oldProduct.setUpdatedDate(LocalDate.now());
            oldProduct.setImgUrl(fileService.uploadFile(imgFile).getDownloadUrl());
            if (validateProduct(oldProduct)) {
                return productRepository.saveAndFlush(oldProduct);
            }
            return null;
        } catch (BadRequestException badRequestException) {
            throw badRequestException;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao atualizar o produto, entre em contato com um Administrador");
        }
    }

    public Boolean validateProduct(Product product) {
        try {
            if (product.getName() != null &&
                    product.getDescription() != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            throw new BadRequestException("Erro ao validar produto, entre em contato com um Administrador");
        }
    }

}
