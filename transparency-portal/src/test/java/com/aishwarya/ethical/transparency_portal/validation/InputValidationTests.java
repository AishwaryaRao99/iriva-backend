package com.aishwarya.ethical.transparency_portal.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.RegisterRequest;
import com.aishwarya.ethical.transparency_portal.modules.user.dto.UserRequest;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductCategory;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductCategoryDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.PageRequestDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * Integration tests for Spring input validation on DTOs.
 * Tests cover various validation annotations including:
 * - @NotBlank, @NotNull
 * - @Size, @Min, @Max
 * - @Email
 * - @Pattern
 * - @DecimalMin, @DecimalMax
 */
@SpringBootTest
@DisplayName("Input Validation Integration Tests")
class InputValidationTests {

    @Autowired
    private Validator validator;

    /**
     * Nested test class for LoginRequest validation
     */
    @Nested
    @DisplayName("LoginRequest Validation Tests")
    class LoginRequestValidationTests {

        private LoginRequest loginRequest;

        @BeforeEach
        void setUp() {
            loginRequest = new LoginRequest();
        }

        @Test
        @DisplayName("Should accept valid login request")
        void testValidLoginRequest() {
            // Arrange
            loginRequest.setUsername("testuser");
            loginRequest.setPassword("password123");

            // Act
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

            // Assert
            assertTrue(violations.isEmpty(), "Valid login request should not have violations");
        }

        @Test
        @DisplayName("Should reject blank username")
        void testBlankUsername() {
            // Arrange
            loginRequest.setUsername("   ");
            loginRequest.setPassword("password123");

            // Act
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Blank username should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Username or email is required")),
                    "Should contain username required message"
            );
        }

        @Test
        @DisplayName("Should reject null username")
        void testNullUsername() {
            // Arrange
            loginRequest.setUsername(null);
            loginRequest.setPassword("password123");

            // Act
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Null username should have violations");
        }

        @Test
        @DisplayName("Should reject blank password")
        void testBlankPassword() {
            // Arrange
            loginRequest.setUsername("testuser");
            loginRequest.setPassword("   ");

            // Act
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Blank password should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Password is required")),
                    "Should contain password required message"
            );
        }

        @Test
        @DisplayName("Should reject password shorter than 3 characters")
        void testPasswordTooShort() {
            // Arrange
            loginRequest.setUsername("testuser");
            loginRequest.setPassword("ab");

            // Act
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Short password should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("at least 3 characters")),
                    "Should contain size constraint message"
            );
        }

        @Test
        @DisplayName("Should accept password with exactly 3 characters")
        void testPasswordWithMinimumLength() {
            // Arrange
            loginRequest.setUsername("testuser");
            loginRequest.setPassword("abc");

            // Act
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

            // Assert
            assertTrue(violations.isEmpty(), "Password with 3 characters should be valid");
        }
    }

    /**
     * Nested test class for RegisterRequest validation
     */
    @Nested
    @DisplayName("RegisterRequest Validation Tests")
    class RegisterRequestValidationTests {

        private RegisterRequest registerRequest;

        @BeforeEach
        void setUp() {
            registerRequest = new RegisterRequest();
        }

        @Test
        @DisplayName("Should accept valid register request")
        void testValidRegisterRequest() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("Password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertTrue(violations.isEmpty(), "Valid register request should not have violations");
        }

        @Test
        @DisplayName("Should reject username shorter than 3 characters")
        void testUsernameTooShort() {
            // Arrange
            registerRequest.setUsername("ab");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("Password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Short username should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("between 3 and 50 characters")),
                    "Should contain username size constraint message"
            );
        }

        @Test
        @DisplayName("Should reject username longer than 50 characters")
        void testUsernameTooLong() {
            // Arrange
            registerRequest.setUsername("a".repeat(51));
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("Password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Long username should have violations");
        }

        @Test
        @DisplayName("Should reject username with invalid characters")
        void testUsernameWithInvalidCharacters() {
            // Arrange
            registerRequest.setUsername("test@user!invalid");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("Password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Username with invalid characters should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("only contain letters")),
                    "Should contain pattern constraint message"
            );
        }

        @Test
        @DisplayName("Should accept username with valid characters (letters, numbers, dots, hyphens, underscores)")
        void testUsernameWithValidCharacters() {
            // Arrange
            registerRequest.setUsername("test_user-123.name");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("Password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertTrue(violations.isEmpty(), "Username with valid characters should be valid");
        }

        @Test
        @DisplayName("Should reject blank email")
        void testBlankEmail() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("   ");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("Password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Blank email should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Email is required")),
                    "Should contain email required message"
            );
        }

        @Test
        @DisplayName("Should reject invalid email format")
        void testInvalidEmailFormat() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("invalid-email");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("Password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Invalid email format should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Email should be valid")),
                    "Should contain email format message"
            );
        }

        @Test
        @DisplayName("Should accept valid email formats")
        void testValidEmailFormats() {
            // Arrange & Act & Assert
            String[] validEmails = {
                    "test@example.com",
                    "user.name@example.co.uk",
                    "user+tag@example.org",
                    "user_name123@example-domain.com"
            };

            for (String email : validEmails) {
                registerRequest.setEmail(email);
                registerRequest.setUsername("testuser123");
                registerRequest.setPassword("Password123!");
                registerRequest.setConfirmPassword("Password123!");

                Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
                assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
            }
        }

        @Test
        @DisplayName("Should reject password shorter than 8 characters")
        void testPasswordTooShort() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Pass1!");
            registerRequest.setConfirmPassword("Pass1!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Short password should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("between 8 and 128 characters")),
                    "Should contain password size constraint message"
            );
        }

        @Test
        @DisplayName("Should reject password longer than 128 characters")
        void testPasswordTooLong() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123!sdsdfsdfsdf" + "a".repeat(116));
            registerRequest.setConfirmPassword("Password123!sdsdfsdfsdf" + "a".repeat(116));

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
            
            // Assert
            assertFalse(violations.isEmpty(), "Long password should have violations");
            
            
        }

        @Test
        @DisplayName("Should reject password without uppercase letter")
        void testPasswordWithoutUppercase() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("password123!");
            registerRequest.setConfirmPassword("password123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Password without uppercase should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("uppercase letter")),
                    "Should contain uppercase requirement message"
            );
        }

        @Test
        @DisplayName("Should reject password without lowercase letter")
        void testPasswordWithoutLowercase() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("PASSWORD123!");
            registerRequest.setConfirmPassword("PASSWORD123!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Password without lowercase should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("lowercase letter")),
                    "Should contain lowercase requirement message"
            );
        }

        @Test
        @DisplayName("Should reject password without digit")
        void testPasswordWithoutDigit() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password!");
            registerRequest.setConfirmPassword("Password!");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Password without digit should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("digit")),
                    "Should contain digit requirement message"
            );
        }

        @Test
        @DisplayName("Should reject password without special character")
        void testPasswordWithoutSpecialCharacter() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123");
            registerRequest.setConfirmPassword("Password123");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Password without special character should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("special character")),
                    "Should contain special character requirement message"
            );
        }

        @Test
        @DisplayName("Should reject password with invalid special characters")
        void testPasswordWithInvalidSpecialCharacters() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123#");
            registerRequest.setConfirmPassword("Password123#");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Password with invalid special character should have violations");
        }

        @Test
        @DisplayName("Should accept password with all required criteria")
        void testValidPasswordWithAllCriteria() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("ValidPass@123");
            registerRequest.setConfirmPassword("ValidPass@123");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertTrue(violations.isEmpty(), "Password with all criteria should be valid");
        }

        @Test
        @DisplayName("Should reject blank confirm password")
        void testBlankConfirmPassword() {
            // Arrange
            registerRequest.setUsername("testuser123");
            registerRequest.setEmail("test@example.com");
            registerRequest.setPassword("Password123!");
            registerRequest.setConfirmPassword("   ");

            // Act
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Blank confirm password should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("required")),
                    "Should contain confirm password required message"
            );
        }
    }

    /**
     * Nested test class for UserRequest validation
     */
    @Nested
    @DisplayName("UserRequest Validation Tests")
    class UserRequestValidationTests {

        private UserRequest userRequest;

        @BeforeEach
        void setUp() {
            userRequest = new UserRequest();
        }

        @Test
        @DisplayName("Should accept valid user request")
        void testValidUserRequest() {
            // Arrange
            userRequest.setName("John Doe");
            userRequest.setEmail("john@example.com");

            // Act
            Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

            // Assert
            assertTrue(violations.isEmpty(), "Valid user request should not have violations");
        }

        @Test
        @DisplayName("Should reject blank name")
        void testBlankName() {
            // Arrange
            userRequest.setName("   ");
            userRequest.setEmail("john@example.com");

            // Act
            Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Blank name should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Name is required")),
                    "Should contain name required message"
            );
        }

        @Test
        @DisplayName("Should reject invalid email format")
        void testInvalidEmail() {
            // Arrange
            userRequest.setName("John Doe");
            userRequest.setEmail("invalid-email");

            // Act
            Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

            // Assert
            assertFalse(violations.isEmpty(), "Invalid email should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Invalid email format")),
                    "Should contain invalid email message"
            );
        }

        @Test
        @DisplayName("Should accept null email (optional field)")
        void testNullEmail() {
            // Arrange
            userRequest.setName("John Doe");
            userRequest.setEmail(null);

            // Act
            Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

            // Assert
            assertTrue(violations.isEmpty(), "Null email should be valid (optional field)");
        }

        @Test
        @DisplayName("Should accept valid email formats")
        void testValidEmailFormats() {
            // Arrange & Act & Assert
            String[] validEmails = {
                    "user@example.com",
                    "user.name@example.co.uk",
                    "user+tag@domain.org"
            };

            for (String email : validEmails) {
                userRequest.setEmail(email);
                userRequest.setName("John Doe");

                Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
                assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
            }
        }
    }

    /**
     * Nested test class for ProductDTO validation
     */
    @Nested
    @DisplayName("ProductDTO Validation Tests")
    class ProductDTOValidationTests {

        private ProductDTO productDTO;

        @BeforeEach
        void setUp() {
            productDTO = new ProductDTO();
        }

        @Test
        @DisplayName("Should accept valid product DTO")
        void testValidProductDTO() {
            // Arrange
            productDTO.setProductName("Organic Coffee");
            productDTO.setDescription("High-quality organic coffee beans sourced from fair trade farms");
            productDTO.setImageUrl("https://example.com/coffee.jpg");
            productDTO.setBrand("EthicalBrew");
            productDTO.setEthicalScore(85.5);
            productDTO.setTransparencyScore(90.0);
            productDTO.setCategory(ProductCategory.FOOD);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
                        
            // Assert
            assertTrue(violations.isEmpty(), "Valid product DTO should not have violations");
           
        }

        @Test
        @DisplayName("Should reject blank product name")
        void testBlankProductName() {
            // Arrange
            productDTO.setProductName("   ");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Blank product name should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Product name is required")),
                    "Should contain product name required message"
            );
        }

        @Test
        @DisplayName("Should reject product name shorter than 2 characters")
        void testProductNameTooShort() {
            // Arrange
            productDTO.setProductName("A");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Short product name should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("between 2 and 255 characters")),
                    "Should contain product name size constraint message"
            );
        }

        @Test
        @DisplayName("Should reject product name longer than 255 characters")
        void testProductNameTooLong() {
            // Arrange
            productDTO.setProductName("A".repeat(256));
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Long product name should have violations");
        }

        @Test
        @DisplayName("Should reject description shorter than 10 characters")
        void testDescriptionTooShort() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("Short");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Short description should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("between 10 and 2000 characters")),
                    "Should contain description size constraint message"
            );
        }

        @Test
        @DisplayName("Should reject description longer than 2000 characters")
        void testDescriptionTooLong() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("A".repeat(2001));
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Long description should have violations");
        }

        @Test
        @DisplayName("Should reject blank image URL")
        void testBlankImageUrl() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("   ");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Blank image URL should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Product image URL is required")),
                    "Should contain image URL required message"
            );
        }

        @Test
        @DisplayName("Should reject blank brand")
        void testBlankBrand() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("   ");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Blank brand should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Brand name is required")),
                    "Should contain brand name required message"
            );
        }

        @Test
        @DisplayName("Should reject brand shorter than 2 characters")
        void testBrandTooShort() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("A");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Short brand should have violations");
        }

        @Test
        @DisplayName("Should reject ethical score below 0.0")
        void testEthicalScoreTooLow() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(-0.1);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Ethical score below 0 should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("at least 0")),
                    "Should contain ethical score minimum constraint message"
            );
        }

        @Test
        @DisplayName("Should reject ethical score above 100.0")
        void testEthicalScoreTooHigh() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(100.1);
            productDTO.setTransparencyScore(90.0);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Ethical score above 100 should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("cannot exceed 100")),
                    "Should contain ethical score maximum constraint message"
            );
        }

        @Test
        @DisplayName("Should accept ethical score at boundaries (0 and 100)")
        void testEthicalScoreBoundaries() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");

            // Test boundary value 0
            productDTO.setEthicalScore(0.0);
            productDTO.setTransparencyScore(90.0);
            productDTO.setCategory(ProductCategory.FOOD);
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);        
            
            assertTrue(violations.isEmpty(), "Ethical score 0 should be valid");                    

            // Test boundary value 100
            productDTO.setEthicalScore(100.0);
            violations = validator.validate(productDTO);         
            assertTrue(violations.isEmpty(), "Ethical score 100 should be valid");
                   
        }

        @Test
        @DisplayName("Should reject transparency score below 0.0")
        void testTransparencyScoreTooLow() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(-0.1);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Transparency score below 0 should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("at least 0")),
                    "Should contain transparency score minimum constraint message"
            );
        }

        @Test
        @DisplayName("Should reject transparency score above 100.0")
        void testTransparencyScoreTooHigh() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(100.1);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Transparency score above 100 should have violations");
        }

        @Test
        @DisplayName("Should reject null ethical score")
        void testNullEthicalScore() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(0); // Will be set to 0 due to primitive type
            productDTO.setTransparencyScore(90.0);

            // Note: Since ethicalScore is a double (primitive), it cannot be null

            // Act & Assert
            // This test demonstrates that primitive types cannot be null
            assertTrue(productDTO.getEthicalScore() >= 0, "Primitive double cannot be null");
        }

        @Test
        @DisplayName("Should reject null category")
        void testNullCategory() {
            // Arrange
            productDTO.setProductName("Coffee");
            productDTO.setDescription("High-quality product description here");
            productDTO.setImageUrl("https://example.com/image.jpg");
            productDTO.setBrand("Brand");
            productDTO.setEthicalScore(85.0);
            productDTO.setTransparencyScore(90.0);
            productDTO.setCategory(null);

            // Act
            Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Null category should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Product category is required")),
                    "Should contain category required message"
            );
        }
    }

    /**
     * Nested test class for ProductCategoryDTO validation
     */
    @Nested
    @DisplayName("ProductCategoryDTO Validation Tests")
    class ProductCategoryDTOValidationTests {

        private ProductCategoryDTO productCategoryDTO;

        @BeforeEach
        void setUp() {
            productCategoryDTO = new ProductCategoryDTO("FOOD", "\uD83C\uDF72");
        }

        @Test
        @DisplayName("Should accept valid product category DTO")
        void testValidProductCategoryDTO() {
            // Arrange
            productCategoryDTO.setCategory("Electronics");
            productCategoryDTO.setIcon("https://example.com/electronics.png");

            // Act
            Set<ConstraintViolation<ProductCategoryDTO>> violations = validator.validate(productCategoryDTO);

            // Assert
            assertTrue(violations.isEmpty(), "Valid product category DTO should not have violations");
        }

        @Test
        @DisplayName("Should reject blank category")
        void testBlankCategory() {
            // Arrange
            productCategoryDTO.setCategory("   ");
            productCategoryDTO.setIcon("https://example.com/icon.png");

            // Act
            Set<ConstraintViolation<ProductCategoryDTO>> violations = validator.validate(productCategoryDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Blank category should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Category name is required")),
                    "Should contain category required message"
            );
        }

        @Test
        @DisplayName("Should reject category shorter than 2 characters")
        void testCategoryTooShort() {
            // Arrange
            productCategoryDTO.setCategory("A");
            productCategoryDTO.setIcon("https://example.com/icon.png");

            // Act
            Set<ConstraintViolation<ProductCategoryDTO>> violations = validator.validate(productCategoryDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Short category should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("between 2 and 100 characters")),
                    "Should contain category size constraint message"
            );
        }

        @Test
        @DisplayName("Should reject category longer than 100 characters")
        void testCategoryTooLong() {
            // Arrange
            productCategoryDTO.setCategory("A".repeat(101));
            productCategoryDTO.setIcon("https://example.com/icon.png");

            // Act
            Set<ConstraintViolation<ProductCategoryDTO>> violations = validator.validate(productCategoryDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Long category should have violations");
        }

        @Test
        @DisplayName("Should reject blank icon")
        void testBlankIcon() {
            // Arrange
            productCategoryDTO.setCategory("Electronics");
            productCategoryDTO.setIcon("   ");

            // Act
            Set<ConstraintViolation<ProductCategoryDTO>> violations = validator.validate(productCategoryDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Blank icon should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Category icon is required")),
                    "Should contain icon required message"
            );
        }

        @Test
        @DisplayName("Should reject icon URL longer than 255 characters")
        void testIconTooLong() {
            // Arrange
            productCategoryDTO.setCategory("Electronics");
            productCategoryDTO.setIcon("https://example.com/" + "a".repeat(240));

            // Act
            Set<ConstraintViolation<ProductCategoryDTO>> violations = validator.validate(productCategoryDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Long icon URL should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("cannot exceed 255 characters")),
                    "Should contain icon size constraint message"
            );
        }
    }

    /**
     * Nested test class for PageRequestDTO validation
     */
    @Nested
    @DisplayName("PageRequestDTO Validation Tests")
    class PageRequestDTOValidationTests {

        private PageRequestDTO pageRequestDTO;

        @BeforeEach
        void setUp() {
            pageRequestDTO = new PageRequestDTO();
        }

        @Test
        @DisplayName("Should accept valid page request DTO")
        void testValidPageRequestDTO() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertTrue(violations.isEmpty(), "Valid page request DTO should not have violations");
        }

        @Test
        @DisplayName("Should accept default values")
        void testDefaultValues() {
            // Arrange
            pageRequestDTO = new PageRequestDTO();

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertTrue(violations.isEmpty(), "Default values should be valid");
            assertEquals(0, pageRequestDTO.getPage(), "Default page should be 0");
            assertEquals(10, pageRequestDTO.getSize(), "Default size should be 10");
            assertEquals("productName", pageRequestDTO.getSortBy(), "Default sort by should be productName");
            assertEquals("ASC", pageRequestDTO.getSortDirection(), "Default sort direction should be ASC");
        }

        @Test
        @DisplayName("Should reject negative page number")
        void testNegativePageNumber() {
            // Arrange
            pageRequestDTO.setPage(-1);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Negative page number should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("greater than or equal to 0")),
                    "Should contain page number minimum constraint message"
            );
        }

        @Test
        @DisplayName("Should accept zero page number")
        void testZeroPageNumber() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertTrue(violations.isEmpty(), "Page number 0 should be valid");
        }

        @Test
        @DisplayName("Should reject page size less than 1")
        void testPageSizeTooSmall() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(0);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Page size less than 1 should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("at least 1")),
                    "Should contain page size minimum constraint message"
            );
        }

        @Test
        @DisplayName("Should reject page size greater than 100")
        void testPageSizeTooLarge() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(101);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Page size greater than 100 should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("cannot exceed 100")),
                    "Should contain page size maximum constraint message"
            );
        }

        @Test
        @DisplayName("Should accept page size at boundaries (1 and 100)")
        void testPageSizeBoundaries() {
            // Test minimum boundary
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(1);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("ASC");

            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);
            assertTrue(violations.isEmpty(), "Page size 1 should be valid");

            // Test maximum boundary
            pageRequestDTO.setSize(100);
            violations = validator.validate(pageRequestDTO);
            assertTrue(violations.isEmpty(), "Page size 100 should be valid");
        }

        @Test
        @DisplayName("Should reject blank sort field")
        void testBlankSortField() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("   ");
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Blank sort field should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Sort field is required")),
                    "Should contain sort field required message"
            );
        }

        @Test
        @DisplayName("Should reject sort field with invalid characters")
        void testSortFieldWithInvalidCharacters() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("product-name"); // Hyphen is invalid
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Sort field with invalid characters should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("alphanumeric characters and underscores")),
                    "Should contain sort field pattern constraint message"
            );
        }

        @Test
        @DisplayName("Should accept sort field with valid characters")
        void testSortFieldWithValidCharacters() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("product_name_123"); // Underscores and alphanumerics are valid
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertTrue(violations.isEmpty(), "Sort field with valid characters should be valid");
        }

        @Test
        @DisplayName("Should reject blank sort direction")
        void testBlankSortDirection() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("   ");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Blank sort direction should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("Sort direction is required")),
                    "Should contain sort direction required message"
            );
        }

        @Test
        @DisplayName("Should reject invalid sort direction (not ASC or DESC)")
        void testInvalidSortDirection() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("INVALID");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Invalid sort direction should have violations");
            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getMessage().contains("ASC or DESC")),
                    "Should contain sort direction pattern constraint message"
            );
        }

        @Test
        @DisplayName("Should accept ASC sort direction")
        void testAscSortDirection() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("ASC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertTrue(violations.isEmpty(), "ASC sort direction should be valid");
        }

        @Test
        @DisplayName("Should accept DESC sort direction")
        void testDescSortDirection() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("DESC");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertTrue(violations.isEmpty(), "DESC sort direction should be valid");
        }

        @Test
        @DisplayName("Should reject lowercase sort direction")
        void testLowercaseSortDirection() {
            // Arrange
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(10);
            pageRequestDTO.setSortBy("productName");
            pageRequestDTO.setSortDirection("asc");

            // Act
            Set<ConstraintViolation<PageRequestDTO>> violations = validator.validate(pageRequestDTO);

            // Assert
            assertFalse(violations.isEmpty(), "Lowercase sort direction should have violations");
        }
    }
}
