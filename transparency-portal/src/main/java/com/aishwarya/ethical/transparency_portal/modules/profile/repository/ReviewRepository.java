package com.aishwarya.ethical.transparency_portal.modules.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aishwarya.ethical.transparency_portal.modules.profile.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
    long countByUserId(Long userId);
}