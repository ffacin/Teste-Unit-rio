package br.com.fatec.donationhaapi.repository;

import br.com.fatec.donationhaapi.entity.Category;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.fatec.donationhaapi.entity.Product;
import org.springframework.data.repository.query.Param;

public interface ProductRepository  extends JpaRepository<Product,Long>, PagingAndSortingRepository<Product,Long>{

    List<Product> findAllByCategoriesIn(Set<Category> category);

     Page<Product> findAll(Pageable pageable);

    @Query("SELECT pr FROM Product pr JOIN pr.categories pc WHERE pc.id = :categoryId")
    List<Product> listProductByCategory(@Param("categoryId") Long categoryId);
}
