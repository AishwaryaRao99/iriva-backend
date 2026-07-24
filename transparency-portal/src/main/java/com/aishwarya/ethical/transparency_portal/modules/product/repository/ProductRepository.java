package com.aishwarya.ethical.transparency_portal.modules.product.repository;

import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductCategory;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductModel;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    
    // ==================== SCORE-BASED FILTERING ====================
    
    // Find products by ethical score range
    @Query("SELECT p FROM ProductModel p WHERE p.ethicalScore >= :minScore AND p.ethicalScore <= :maxScore")
    Page<ProductModel> findByEthicalScoreRange(@Param("minScore") double minScore, @Param("maxScore") double maxScore, Pageable pageable);
    
    // Find products by transparency score range
    @Query("SELECT p FROM ProductModel p WHERE p.transparencyScore >= :minScore AND p.transparencyScore <= :maxScore")
    Page<ProductModel> findByTransparencyScoreRange(@Param("minScore") double minScore, @Param("maxScore") double maxScore, Pageable pageable);
    
    // Find products by ethical score above threshold
    Page<ProductModel> findByEthicalScoreGreaterThanEqual(Double minScore, Pageable pageable);
    
    // Find products by transparency score above threshold
    Page<ProductModel> findByTransparencyScoreGreaterThanEqual(Double minScore, Pageable pageable);
    
    // ==================== COMBINED FILTERING ====================
    
    // Find products by both ethical and transparency score
    @Query("SELECT p FROM ProductModel p WHERE p.ethicalScore >= :minEthicalScore AND p.ethicalScore <= :maxEthicalScore " +
           "AND p.transparencyScore >= :minTransparencyScore AND p.transparencyScore <= :maxTransparencyScore")
    Page<ProductModel> findByScoreRange(
        @Param("minEthicalScore") double minEthicalScore,
        @Param("maxEthicalScore") double maxEthicalScore,
        @Param("minTransparencyScore") double minTransparencyScore,
        @Param("maxTransparencyScore") double maxTransparencyScore,
        Pageable pageable
    );
    
    // Find products by category and score
    @Query("SELECT p FROM ProductModel p WHERE p.category = :category AND p.ethicalScore >= :minScore")
    Page<ProductModel> findByCategoryAndEthicalScoreAbove(
        @Param("category") ProductCategory category,
        @Param("minScore") double minScore,
        Pageable pageable
    );
}