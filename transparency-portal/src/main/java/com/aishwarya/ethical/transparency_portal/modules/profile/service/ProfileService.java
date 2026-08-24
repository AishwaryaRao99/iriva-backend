package com.aishwarya.ethical.transparency_portal.modules.profile.service;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aishwarya.ethical.transparency_portal.exception_handling.ConflictException;
import com.aishwarya.ethical.transparency_portal.exception_handling.ErrorCode;
import com.aishwarya.ethical.transparency_portal.exception_handling.ProductNotFoundException;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductModel;
import com.aishwarya.ethical.transparency_portal.modules.product.repository.ProductRepository;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ActivityDTO;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ProfileDTO;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ReviewDTO;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ReviewRequest;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.UserProfileDTO;
import com.aishwarya.ethical.transparency_portal.modules.profile.model.Review;
import com.aishwarya.ethical.transparency_portal.modules.profile.model.SavedProduct;
import com.aishwarya.ethical.transparency_portal.modules.profile.repository.ReviewRepository;
import com.aishwarya.ethical.transparency_portal.modules.profile.repository.SavedProductRepository;
import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final DateTimeFormatter MEMBER_SINCE_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    private final UserService userService;
    private final ProductRepository productRepository;
    private final SavedProductRepository savedProductRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public ProfileDTO getProfile(String loginIdentifier) {
        UserModel user = currentUser(loginIdentifier);
        List<ReviewDTO> reviews = getReviews(loginIdentifier);
        return new ProfileDTO(
            new UserProfileDTO(displayName(user), user.getEmail(), user.getCreatedAt().format(MEMBER_SINCE_FORMAT),
                initials(displayName(user))),
            new ActivityDTO(reviewRepository.countByUserId(user.getId()), savedProductRepository.countByUserId(user.getId())),
            reviews);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviews(String loginIdentifier) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(currentUser(loginIdentifier).getId()).stream()
                .map(this::toReviewDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getProductReviews(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(this::toReviewDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductModel> getSavedProducts(String loginIdentifier) {
        return savedProductRepository.findByUserIdOrderBySavedAtDesc(currentUser(loginIdentifier).getId()).stream()
                .map(SavedProduct::getProduct).toList();
    }

    @Transactional
    public boolean saveProduct(String loginIdentifier, Long productId) {
        UserModel user = currentUser(loginIdentifier);
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        if (savedProductRepository.findByUserIdAndProductId(user.getId(), productId).isEmpty()) {
            SavedProduct savedProduct = new SavedProduct();
            savedProduct.setUser(user);
            savedProduct.setProduct(product);
            savedProduct.setSavedAt(LocalDateTime.now());
            savedProductRepository.save(savedProduct);
        }
        return true;
    }

    @Transactional
    public void removeSavedProduct(String loginIdentifier, Long productId) {
        savedProductRepository.findByUserIdAndProductId(currentUser(loginIdentifier).getId(), productId)
                .ifPresent(savedProductRepository::delete);
    }

    @Transactional
    public ReviewDTO addReview(String loginIdentifier, Long productId, ReviewRequest request) {
        UserModel user = currentUser(loginIdentifier);
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        if (reviewRepository.findByUserIdAndProductId(user.getId(), productId).isPresent()) {
            throw new ConflictException(ErrorCode.CONFLICT, "You have already reviewed this product");
        }
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.rating());
        review.setComment(request.comment().trim());
        review.setTags(request.tags() == null ? List.of() : request.tags().stream()
            .map(String::trim).filter(tag -> !tag.isEmpty()).distinct().toList());
        review.setCreatedAt(LocalDateTime.now());
        return toReviewDTO(reviewRepository.save(review));
    }

    private UserModel currentUser(String loginIdentifier) {
        return userService.getUserDetailsByUsernameOrEmail(loginIdentifier);
    }

    private ReviewDTO toReviewDTO(Review review) {
        ProductModel product = review.getProduct();
        return new ReviewDTO(review.getId(), product.getProductName(), product.getBrand(), review.getRating(),
                timeAgo(review.getCreatedAt()), review.getComment(), review.getTags(), product.getImageUrl());
    }

    private String displayName(UserModel user) {
        return user.getDisplayName() == null || user.getDisplayName().isBlank() ? user.getUsername() : user.getDisplayName();
    }

    private String initials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length > 1) {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private String timeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(createdAt.toLocalDate(), now.toLocalDate());
        long months = period.toTotalMonths();
        if (months > 0) return months + " " + plural(months, "month") + " ago";
        long weeks = ChronoUnit.WEEKS.between(createdAt, now);
        if (weeks > 0) return weeks + " " + plural(weeks, "week") + " ago";
        long days = ChronoUnit.DAYS.between(createdAt, now);
        return days + " " + plural(days, "day") + " ago";
    }

    private String plural(long count, String unit) {
        return count == 1 ? unit : unit + "s";
    }
}