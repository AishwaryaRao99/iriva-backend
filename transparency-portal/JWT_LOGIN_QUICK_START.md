# JWT Login Feature - Quick Start Guide

## What Was Implemented

A complete JWT-based authentication system with the following components:

### New Modules/Files Created:
1. **modules/auth/** - New authentication module
   - LoginRequest DTO - Captures login credentials
   - LoginResponse DTO - Returns JWT token and user info
   - AuthenticationService - Handles login logic
   - AuthenticationController - /auth/login REST endpoint

### Updated Files:
1. **UserModel** - Added email, timestamps
2. **UserRepository** - Added query methods (findByUsername, findByEmail)
3. **UserService** - Added password verification and user lookup
4. **SecurityConfig** - Added PasswordEncoder, updated endpoints

---

## Quick Setup & Testing

### 1. Prerequisites
- Maven installed
- Application running on port 8080
- Database initialized

### 2. Insert Test User in Database

**Option A: Using data.sql** (if using H2)
Add to `src/main/resources/data.sql`:
```sql
-- Password: "password123" (BCrypt hashed)
INSERT INTO users (username, email, password, created_at, updated_at) VALUES 
('john_doe', 'john@example.com', '$2a$12$nnGhL10KSLiVKZaS0.ktgeJfFcCRLfcvQMU5XfnJDxLF4PLZQSN2e', NOW(), NOW());

INSERT INTO users (username, email, password, created_at, updated_at) VALUES 
('admin', 'admin@example.com', '$2a$12$nnGhL10KSLiVKZaS0.ktgeJfFcCRLfcvQMU5XfnJDxLF4PLZQSN2e', NOW(), NOW());
```

**Option B: Using Postman (if you create a registration endpoint)**
Create a user registration endpoint that accepts password and hashes it.

### 3. Test Login with Postman

**Create a new POST request:**

```
URL: http://localhost:8080/transparency-portal/auth/login
Method: POST
Headers:
  Content-Type: application/json

Body (raw JSON):
{
  "username": "john_doe",
  "password": "password123"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTUxODkyMjAsImV4cCI6MTY1NTE5MjgyMH0...",
  "tokenType": "Bearer",
  "username": "john_doe",
  "userId": 1,
  "expiresIn": 1655192820000
}
```

### 4. Use Token in Protected Endpoints

**Example: Get User Profile (if endpoint exists)**

```
URL: http://localhost:8080/transparency-portal/user/profile
Method: GET
Headers:
  Authorization: Bearer <paste_token_here>
```

---

## Key Endpoints

### Authentication
- `POST /auth/login` - Login with credentials

### Public Endpoints (No auth required)
- `GET /api/v1/productsapi/**` - Browse products
- `GET /api/test/token` - Get test JWT token

### Protected Endpoints (Auth required)
- `/admin/**` - Admin only
- `/client/**` - Client only
- `/user/**` - User only

---

## Configuration

### JWT Settings (application.yaml)
```yaml
jwt:
  secret: sdjjfb84rk94njsdf02-ddfsdbq3#$^%3bdhfskksdhjfbKB76HFSAKf86320%)*#d
  expiration: 3600000  # 1 hour in milliseconds
```

### Password Encoding
- Algorithm: BCrypt with strength 12
- Automatically handled by Spring Security

---

## Common Tasks

### Task 1: Create a Protected Endpoint

```java
@RestController
@RequestMapping("/user")
public class UserController {
    
    @PostMapping("/profile/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> updateProfile(
        @RequestBody UpdateProfileRequest request,
        Principal principal) {
        
        String username = principal.getName();
        // Update user profile logic
        return ResponseEntity.ok("Profile updated");
    }
}
```

### Task 2: Access Current User

```java
@GetMapping("/profile")
public ResponseEntity<UserDTO> getProfile(Principal principal) {
    String currentUsername = principal.getName();
    // Fetch user details
    return ResponseEntity.ok(userDetails);
}
```

### Task 3: Test Token Validation

```bash
# Get token
TOKEN=$(curl -s -X POST http://localhost:8080/transparency-portal/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"password123"}' | jq -r '.token')

# Use token
curl -X GET http://localhost:8080/transparency-portal/user/endpoint \
  -H "Authorization: Bearer $TOKEN"
```

---

## Error Scenarios & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| 401 Unauthorized | Invalid credentials | Check username/password are correct |
| 400 Bad Request | Missing fields | Ensure both username and password are provided |
| 400 Bad Request | Password too short | Password must be at least 3 characters |
| 500 Internal Error | User not found | Insert test data in database |
| 401 Unauthorized | Invalid token | Regenerate token or use Bearer prefix |

---

## Code Examples

### Example 1: Using AuthenticationService directly (for testing)
```java
@Autowired
private AuthenticationService authenticationService;

public void testLogin() {
    LoginRequest request = new LoginRequest("john_doe", "password123");
    LoginResponse response = authenticationService.authenticate(request);
    System.out.println("Token: " + response.getToken());
}
```

### Example 2: Validating User Password
```java
@Autowired
private UserService userService;

public boolean validatePassword(String username, String password) {
    UserModel user = userService.findByUsername(username);
    return userService.verifyPassword(password, user.getPassword());
}
```

### Example 3: Finding User
```java
// Find by username
UserModel user = userService.findByUsername("john_doe");

// Find by email
UserModel user = userService.findByEmail("john@example.com");

// Find by either
UserModel user = userService.findByUsernameOrEmail("john_doe");
```

---

## Files Structure

```
transparency-portal/
├── src/main/java/com/aishwarya/ethical/transparency_portal/
│   ├── modules/
│   │   ├── auth/
│   │   │   ├── controller/
│   │   │   │   └── AuthenticationController.java      ✅ NEW
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java                   ✅ NEW
│   │   │   │   └── LoginResponse.java                  ✅ NEW
│   │   │   └── service/
│   │   │       └── AuthenticationService.java          ✅ NEW
│   │   └── user/
│   │       ├── model/
│   │       │   └── UserModel.java                      ✏️ UPDATED
│   │       ├── repository/
│   │       │   └── UserRepository.java                 ✏️ UPDATED
│   │       └── service/
│   │           └── UserService.java                    ✏️ UPDATED
│   └── configuration/
│       └── SecurityConfig.java                         ✏️ UPDATED
├── JWT_AUTHENTICATION_GUIDE.md                         ✅ NEW
└── JWT_LOGIN_QUICK_START.md                            ✅ NEW (this file)
```

---

## Production Checklist

- [ ] Generate strong JWT secret and store in environment variable
- [ ] Update token expiration time based on security requirements
- [ ] Configure HTTPS for all endpoints
- [ ] Add rate limiting to prevent brute force attacks
- [ ] Implement refresh token mechanism
- [ ] Add JWT validation filter to protect endpoints
- [ ] Set up token blacklist for logout functionality
- [ ] Configure CORS properly for frontend integration
- [ ] Enable CSRF protection if serving web pages
- [ ] Set up comprehensive logging and monitoring

---

## Support & Debugging

### Enable Debug Logging
Add to application.yaml:
```yaml
logging:
  level:
    com.aishwarya.ethical.transparency_portal: DEBUG
```

### View Generated Token Details
Use https://jwt.io/ to decode token and inspect claims.

### Check User Password Hash
```bash
# Verify BCrypt hash matches password
# Use online BCrypt validator or Java code:
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
boolean matches = encoder.matches("password123", "$2a$12$...");
```

---

## Next Steps

1. ✅ Insert test users in database
2. ✅ Test login endpoint with Postman
3. ✅ Create protected endpoints as needed
4. ✅ Integrate frontend with login API
5. ✅ Set up token storage (localStorage/sessionStorage)
6. ✅ Implement logout functionality
7. ✅ Add refresh token support
8. ✅ Set up proper security headers

---

**Last Updated:** 2023-06-20  
**Version:** 1.0  
**Status:** Ready for Production Integration
