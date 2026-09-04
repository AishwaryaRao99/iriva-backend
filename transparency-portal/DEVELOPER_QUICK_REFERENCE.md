# JWT Authentication - Developer Quick Reference Card

## 🔑 API Endpoints

### Login Endpoint
```http
POST /auth/login
Content-Type: application/json

Request Body:
{
  "username": "john_doe",
  "password": "password123"
}

Success Response (200):
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "john_doe",
  "userId": 1,
  "expiresIn": 1655192820000
}

Error Response (401):
{
  "errorCode": "AUTH_001",
  "message": "Invalid credentials provided",
  "status": 401,
  "timestamp": "2023-06-20T10:30:45.123"
}
```

---

## 🛠️ Using the Token

### Include in Request Header
```http
Authorization: Bearer <token>
```

### Example Request
```bash
curl -X GET http://localhost:8080/transparency-portal/user/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## 💻 Java Code Examples

### 1. Inject AuthenticationService
```java
@Autowired
private AuthenticationService authenticationService;
```

### 2. Authenticate User
```java
LoginRequest request = new LoginRequest("john_doe", "password123");
LoginResponse response = authenticationService.authenticate(request);

String token = response.getToken();
Long userId = response.getUserId();
String username = response.getUsername();
```

### 3. Get Current User
```java
@GetMapping("/profile")
public ResponseEntity<?> getProfile(Principal principal) {
    String currentUsername = principal.getName();
    return ResponseEntity.ok(userService.findByUsername(currentUsername));
}
```

### 4. Role-Based Access
```java
@PostMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> adminOnly() {
    return ResponseEntity.ok("Admin access granted");
}
```

### 5. Verify Password
```java
@Autowired
private UserService userService;

boolean isValid = userService.verifyPassword(
    "password123", 
    "$2a$12$nnGhL10KSLiVKZaS0.ktgeJfFcCRLfcvQMU5XfnJDxLF4PLZQSN2e"
);
```

### 6. Encode Password
```java
@Autowired
private PasswordEncoder passwordEncoder;

String hashedPassword = passwordEncoder.encode("password123");
```

### 7. Find User
```java
// By username
UserModel user = userService.findByUsername("john_doe");

// By email
UserModel user = userService.findByEmail("john@example.com");

// By username or email
UserModel user = userService.findByUsernameOrEmail("john_doe");
```

---

## 📋 Package Structure

```
modules/auth/
├── controller/
│   └── AuthenticationController        REST endpoints
├── dto/
│   ├── LoginRequest                    Request DTO
│   └── LoginResponse                   Response DTO
└── service/
    └── AuthenticationService           Business logic

modules/user/
├── service/
│   └── UserService                     Enhanced with auth methods
├── repository/
│   └── UserRepository                  Enhanced with query methods
└── model/
    └── UserModel                       Enhanced with new fields
```

---

## 🔐 Authentication Flow

```
1. Client submits username + password
                    ↓
2. AuthenticationController receives request
                    ↓
3. AuthenticationService.authenticate() called
                    ↓
4. UserService finds user by username or email
                    ↓
5. UserService verifies password with BCrypt
                    ↓
6. JWTUtil generates token
                    ↓
7. LoginResponse returned with token
                    ↓
8. Client stores token (localStorage/sessionStorage)
                    ↓
9. Client includes token in Authorization header
                    ↓
10. Spring Security validates token on protected endpoints
```

---

## ⚙️ Configuration

### JWT Settings (application.yaml)
```yaml
jwt:
  secret: ${JWT_SECRET:sdjjfb84rk94njsdf02-ddfsdbq3#$^%3bdhfskksdhjfbKB76HFSAKf86320%)*#d}
  expiration: ${JWT_EXPIRATION:3600000}  # 1 hour
  header: Authorization
  prefix: Bearer
```

### Endpoints Access Control
```
PUBLIC (✓ No auth required):
  ✓ POST /auth/login
  ✓ GET  /api/v1/productsapi/**
  ✓ GET  /api/test/**

PROTECTED (✗ Auth required):
  ✗ GET  /admin/**                      (Role: ADMIN)
  ✗ GET  /client/**                     (Role: CLIENT)
  ✗ GET  /user/**                       (Role: USER)
  ✗ GET  /other/**                      (Auth required)
```

---

## 🧪 Testing Checklist

### Postman Collection Template

**Test 1: Valid Login**
```
POST /auth/login
{
  "username": "john_doe",
  "password": "password123"
}
Expected: 200 OK with token
```

**Test 2: Invalid Password**
```
POST /auth/login
{
  "username": "john_doe",
  "password": "wrongpassword"
}
Expected: 401 Unauthorized
```

**Test 3: User Not Found**
```
POST /auth/login
{
  "username": "nonexistent",
  "password": "password123"
}
Expected: 401 Unauthorized
```

**Test 4: Missing Fields**
```
POST /auth/login
{
  "username": "john_doe"
}
Expected: 400 Bad Request
```

**Test 5: Use Token in Protected Endpoint**
```
GET /user/profile
Headers: Authorization: Bearer <token>
Expected: 200 OK
```

---

## 🐛 Common Issues & Solutions

| Error | Cause | Fix |
|-------|-------|-----|
| 401 Unauthorized | Wrong password | Verify credentials |
| 400 Bad Request | Empty username | Provide username |
| 500 Internal Error | PasswordEncoder not found | Restart application |
| Invalid token | Expired or malformed | Generate new token |
| CORS error | Cross-origin issue | Check @CrossOrigin |

---

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL (BCrypt hashed),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Insert Test User
```sql
-- Password: "password123"
-- BCrypt Hash: $2a$12$nnGhL10KSLiVKZaS0.ktgeJfFcCRLfcvQMU5XfnJDxLF4PLZQSN2e
INSERT INTO users (username, email, password, created_at, updated_at) 
VALUES ('john_doe', 'john@example.com', '$2a$12$nnGhL10KSLiVKZaS0.ktgeJfFcCRLfcvQMU5XfnJDxLF4PLZQSN2e', NOW(), NOW());
```

---

## 🎯 Key Classes & Methods

### AuthenticationService
```java
public LoginResponse authenticate(LoginRequest loginRequest)
    // Main authentication method
    // Validates credentials and returns token
```

### AuthenticationController
```java
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest)
    // REST endpoint for login
```

### UserService
```java
public UserModel findByUsername(String username)
public UserModel findByEmail(String email)
public UserModel findByUsernameOrEmail(String usernameOrEmail)
public boolean verifyPassword(String rawPassword, String encodedPassword)
public String encodePassword(String rawPassword)
```

### UserRepository
```java
Optional<UserModel> findByUsername(String username)
Optional<UserModel> findByEmail(String email)
boolean existsByUsername(String username)
boolean existsByEmail(String email)
```

---

## 🔒 Security Notes

✅ **Implemented:**
- BCrypt password hashing (strength 12)
- JWT token signing with HMAC SHA-256
- Password verification with secure comparison
- Error messages don't leak user existence
- All auth attempts logged
- CSRF disabled for REST API

⚠️ **Remember:**
- Store JWT_SECRET as environment variable
- Use HTTPS in production
- Set appropriate token expiration
- Implement refresh tokens for production
- Add rate limiting to prevent brute force
- Regularly review security logs

---

## 📚 File References

| File | Purpose |
|------|---------|
| `AuthenticationController.java` | REST login endpoint |
| `AuthenticationService.java` | Authentication logic |
| `LoginRequest.java` | Login request DTO |
| `LoginResponse.java` | Login response DTO |
| `UserService.java` | User management & auth methods |
| `UserRepository.java` | User database queries |
| `UserModel.java` | User entity |
| `SecurityConfig.java` | Security configuration |
| `JWTUtil.java` | Token generation (existing) |

---

## 🚀 Quick Start

1. **Add test user to database**
   ```sql
   INSERT INTO users VALUES (1, 'john_doe', 'john@example.com', '$2a$12$..hash..', NOW(), NOW());
   ```

2. **Start application**
   ```bash
   mvn spring-boot:run
   ```

3. **Login in Postman**
   ```
   POST http://localhost:8080/transparency-portal/auth/login
   Body: {"username": "john_doe", "password": "password123"}
   ```

4. **Copy token from response**

5. **Use token in headers**
   ```
   Authorization: Bearer <token>
   ```

---

## 📞 Support

- **Full Guide:** JWT_AUTHENTICATION_GUIDE.md
- **Quick Tips:** JWT_LOGIN_QUICK_START.md
- **Summary:** IMPLEMENTATION_SUMMARY.md

---

**Version:** 1.0  
**Last Updated:** 2023-06-20  
**Status:** Production Ready
