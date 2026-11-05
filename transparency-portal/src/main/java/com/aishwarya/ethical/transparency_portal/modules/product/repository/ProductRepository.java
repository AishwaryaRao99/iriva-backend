package com.aishwarya.ethical.transparency_portal.modules.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductCategory;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long>, JpaSpecificationExecutor<ProductModel> {
    
    // Find products by name containing (case-insensitive) - non-paginated
    List<ProductModel> findByProductNameContainingIgnoreCase(String name);

    // Find products by category - non-paginated
    List<ProductModel> findByCategory(ProductCategory category);
    
    // ==================== PAGINATION METHODS ====================
    
    // Find all products with pagination and sorting
    Page<ProductModel> findAll(Pageable pageable);
    
    // Find products by name with pagination and sorting
    Page<ProductModel> findByProductNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Find products by category with pagination and sorting
    Page<ProductModel> findByCategory(ProductCategory category, Pageable pageable);
    
    // Find products by brand with pagination and sorting
    Page<ProductModel> findByBrandContainingIgnoreCase(String brand, Pageable pageable);
    
}