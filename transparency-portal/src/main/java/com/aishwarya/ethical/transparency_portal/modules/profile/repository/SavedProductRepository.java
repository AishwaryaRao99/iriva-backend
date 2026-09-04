package com.aishwarya.ethical.transparency_portal.modules.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aishwarya.ethical.transparency_portal.modules.profile.model.SavedProduct;

public interface SavedProductRepository extends JpaRepository<SavedProduct, Long> {
    List<SavedProduct> findByUserIdOrderBySavedAtDesc(Long userId);
    Optional<SavedProduct> findByUserIdAndProductId(Long userId, Long productId);
    long countByUserId(Long userId);
}