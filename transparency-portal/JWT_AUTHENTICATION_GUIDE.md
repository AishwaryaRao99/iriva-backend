# JWT Authentication Implementation Guide

## Overview
This document provides a comprehensive guide to the JWT-based authentication system implemented in the Transparency Portal application.

## Architecture

### Module Structure
```
modules/
├── auth/                          # NEW: Authentication module
│   ├── controller/
│   │   └── AuthenticationController.java    # Login REST endpoint
│   ├── dto/
│   │   ├── LoginRequest.java               # Request DTO for login
│   │   └── LoginResponse.java              # Response DTO with JWT token
│   └── service/
│       └── AuthenticationService.java       # Authentication business logic
└── user/                          # UPDATED: User module
    ├── controller/
    ├── dto/
    ├── model/
    │   └── UserModel.java                  # UPDATED: Added email, timestamps
    ├── repository/
    │   └── UserRepository.java             # UPDATED: Added query methods
    └── service/
        └── UserService.java                # UPDATED: Added auth methods
```

## Component Details

### 1. LoginRequest DTO
**File:** `modules/auth/dto/LoginRequest.java`

Captures user login credentials with validation:
- `username` (String): Username or email for login
- `password` (String): User password

**Validation Rules:**
- Both fields are required (NotBlank)
- Password minimum length: 3 characters

### 2. LoginResponse DTO
**File:** `modules/auth/dto/LoginResponse.java`

Returns authentication response with JWT token:
- `token` (String): JWT authentication token
- `tokenType` (String): "Bearer" (standard JWT type)
- `username` (String): Authenticated user's username
- `userId` (Long): User ID
- `expiresIn` (Long): Token expiration timestamp

**Factory Method:**
```java
LoginResponse.of(String token, String username, Long userId, Long expiresIn)
```

### 3. UserModel
**File:** `modules/user/model/UserModel.java`

**New Fields Added:**
- `email` (String): User's email address
- `createdAt` (LocalDateTime): Account creation timestamp
- `updatedAt` (LocalDateTime): Last update timestamp

**Usage:** Stored in database table "users"

### 4. UserRepository
**File:** `modules/user/repository/UserRepository.java`

**New Query Methods:**
- `Optional<UserModel> findByUsername(String username)`
- `Optional<UserModel> findByEmail(String email)`
- `boolean existsByUsername(String username)`
- `boolean existsByEmail(String email)`

### 5. UserService
**File:** `modules/user/service/UserService.java`

**New Methods:**
- `UserModel findByUsername(String username)` - Find user by username
- `UserModel findByEmail(String email)` - Find user by email
- `UserModel findByUsernameOrEmail(String usernameOrEmail)` - Find by either
- `boolean verifyPassword(String rawPassword, String encodedPassword)` - Verify password using BCrypt
- `String encodePassword(String rawPassword)` - Encode password using BCrypt

### 6. AuthenticationService
**File:** `modules/auth/service/AuthenticationService.java`

**Main Method:**
```java
LoginResponse authenticate(LoginRequest loginRequest)
```

**Authentication Flow:**
1. Load user by username or email
2. Verify password using UserService
3. Generate JWT token using JWTUtil
4. Return LoginResponse with token and user info
5. Throw UnAuthorizedException on failure

**Logging:** All authentication attempts and failures are logged

### 7. AuthenticationController
**File:** `modules/auth/controller/AuthenticationController.java`

**REST Endpoint:**

```http
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Success Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTUxODkyMjAsImV4cCI6MTY1NTE5MjgyMH0.signature...",
  "tokenType": "Bearer",
  "username": "john_doe",
  "userId": 1,
  "expiresIn": 1655192820000
}
```

**Error Responses:**
- `400 Bad Request`: Missing or invalid fields in request
- `401 Unauthorized`: Invalid credentials (user not found or wrong password)
- `500 Internal Server Error`: Server error during processing

## Configuration

### JWT Configuration (application.yaml)
```yaml
jwt:
  secret: ${JWT_SECRET:sdjjfb84rk94njsdf02-ddfsdbq3#$^%3bdhfskksdhjfbKB76HFSAKf86320%)*#d}
  expiration: ${JWT_EXPIRATION:3600000}  # 1 hour in milliseconds
  header: Authorization
  prefix: Bearer
```

### Security Configuration (SecurityConfig.java)

**Public Endpoints:**
- `/auth/**` - Authentication endpoints (no auth required)
- `/api/v1/productsapi/**` - Public product browsing
- `/api/test/**` - Test endpoints

**Protected Endpoints:**
- `/admin/**` - Requires ROLE_ADMIN
- `/client/**` - Requires ROLE_CLIENT
- `/user/**` - Requires ROLE_USER

### Password Encoding
**BCryptPasswordEncoder** with strength 12 is configured as the default PasswordEncoder in SecurityConfig.

```java
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
}
```

## Usage Guide

### For Frontend/Client Developers

#### 1. Login Request
```bash
curl -X POST http://localhost:8080/transparency-portal/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

#### 2. Store Token
After successful login, store the token returned in the response.

#### 3. Use Token in Requests
Include the token in the `Authorization` header for subsequent requests:
```bash
curl -X GET http://localhost:8080/transparency-portal/user/profile \
  -H "Authorization: Bearer <token>"
```

### For Backend Developers

#### Adding Protected Endpoints
1. Create controller methods with appropriate role requirements
2. Use Spring Security annotations if needed:
   ```java
   @PreAuthorize("hasRole('USER')")
   @GetMapping("/profile")
   public ResponseEntity<UserProfile> getProfile() {
       // Your code here
   }
   ```

#### Accessing Authenticated User
```java
@GetMapping("/profile")
public ResponseEntity<UserProfile> getProfile(Principal principal) {
    String username = principal.getName();
    // Your code here
}
```

#### Custom Authentication Logic
If you need to customize authentication:
1. Extend AuthenticationService
2. Add JWT filter if needed for token validation on requests
3. Update SecurityConfig to register the filter

## Testing with Postman

### Step 1: Create Test User (if using H2 database)
- Insert test data via data.sql (already encrypted passwords)
- Or create via an admin endpoint

### Step 2: Login
```
POST http://localhost:8080/transparency-portal/auth/login
Body (JSON):
{
  "username": "testuser",
  "password": "password123"
}
```

### Step 3: Copy Token
Copy the token from the response.

### Step 4: Use in Protected Requests
1. Open Postman
2. Add Authorization header: `Bearer <token>`
3. Send request to protected endpoint

## Error Handling

### Exception Types
- **UnAuthorizedException**: Thrown on invalid credentials or authentication failure
- **UserNotFoundException**: Thrown when user is not found
- **Validation Errors**: Thrown on invalid input data

All exceptions are handled by `GlobalExceptionHandler` which returns standardized error responses.

### Error Response Format
```json
{
  "errorCode": "AUTH_001",
  "message": "Unauthorized access",
  "status": 401,
  "path": "/auth/login",
  "timestamp": "2023-06-20T10:30:45.123",
  "details": null
}
```

## Security Best Practices Implemented

1. **Password Hashing**: BCrypt with strength 12 for secure password storage
2. **JWT Token Generation**: Using JJWT library with HMAC SHA-256
3. **Token Expiration**: 1 hour default expiration (configurable)
4. **Flexible Login**: Support both username and email for login
5. **Error Logging**: All authentication attempts are logged for security audit
6. **CSRF Disabled**: Safe for stateless REST API (consider enabling for web apps)

## Future Enhancements

1. **JWT Token Validation Filter**: Add filter to validate JWT on protected endpoints
2. **Refresh Token**: Implement refresh token for extended sessions
3. **Two-Factor Authentication**: Add 2FA support
4. **Role-Based Access Control**: Fetch roles from database
5. **Token Blacklist**: Implement logout with token blacklisting
6. **Rate Limiting**: Prevent brute force login attacks
7. **OAuth2 Integration**: Support third-party authentication

## Troubleshooting

### Issue: Login returns 401 Unauthorized
**Solution:** Check if password is correct and user exists in database

### Issue: Token expires too quickly
**Solution:** Adjust `jwt.expiration` in application.yaml

### Issue: Invalid token error on protected endpoints
**Solution:** Ensure token is not expired and properly formatted in Authorization header

### Issue: PasswordEncoder not found
**Solution:** Verify SecurityConfig has `@Bean PasswordEncoder passwordEncoder()` method

## Development Notes

- All authentication components are documented with JavaDoc comments
- Follow the existing patterns for consistency
- Test all changes with Postman before integration
- Log all authentication operations for security auditing
- Keep JWT secret secure - use environment variables in production

---

**Last Updated:** 2023-06-20
**Version:** 1.0
**Author:** Development Team
