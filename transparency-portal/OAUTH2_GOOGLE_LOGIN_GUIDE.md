# OAuth2 Google Login Implementation Guide

## Overview
This document explains the OAuth2 Google authentication implementation integrated with your existing JWT-based manual login system.

## Architecture

### Flow Diagram
```
USER
 │
 ├─ Option 1: Manual Login (existing)
 │   └─> Username/Password → JWT Token → HTTP-only Cookie
 │
 └─ Option 2: Continue with Google (NEW)
     └─> Click "Continue with Google"
         │
         └─> Spring Security OAuth2
             │
             └─> Google Authentication
                 │
                 └─> OAuth2SuccessHandler
                     │
                     ├─ Check if user exists in DB
                     ├─ If no → Create new user
                     ├─ Generate JWT Token
                     └─ Set HTTP-only Cookie
                         │
                         └─> Redirect to Frontend
```

## Components Added

### 1. **SecurityConfig.java** (Modified)
**Location:** `src/main/java/com/aishwarya/ethical/transparency_portal/configuration/SecurityConfig.java`

**Changes:**
- Added `OAuth2SuccessHandler` injection
- Configured OAuth2 login in `SecurityFilterChain`:
```java
.oauth2Login(oauth2 -> oauth2
    .successHandler(oAuth2SuccessHandler))
```

**What it does:**
- Enables Spring Security to handle OAuth2 login flow
- Integrates OAuth2 with existing JWT authentication
- Routes OAuth2 authentication through custom success handler

### 2. **OAuth2SuccessHandler.java** (New)
**Location:** `src/main/java/com/aishwarya/ethical/transparency_portal/security/OAuth2SuccessHandler.java`

**What it does:**
1. Intercepts successful OAuth2 authentication from Google
2. Extracts user information (email, name, Google ID)
3. Checks if user exists in database
4. Creates new user if doesn't exist
5. Generates JWT token using your existing JWTUtil
6. Sets JWT as HTTP-only, secure cookie
7. Returns user info + token to frontend

**Key Features:**
- HTTP-only cookies prevent XSS attacks
- Secure flag enabled in production
- SameSite=Lax prevents CSRF attacks
- User roles extracted from database

### 3. **OAuth2UserService.java** (New)
**Location:** `src/main/java/com/aishwarya/ethical/transparency_portal/modules/user/service/OAuth2UserService.java`

**Methods:**
- `findOrCreateUser(email, name, providerId)` - Main method for OAuth2 login
- `createOAuth2User(email, name, providerId)` - Creates new user from OAuth2 data
- `findByEmail(email)` - Utility method
- `findByUsername(username)` - Utility method

**User Creation Logic:**
- OAuth2 users have `password = null` (no password stored)
- Username generated from email prefix
- Default role: "ROLE_USER"
- Timestamps automatically set

### 4. **Configuration Files** (Modified)

#### application.yaml
```yaml
oauth2:
  redirect-url: ${OAUTH2_REDIRECT_URL:http://localhost:5173}
```

#### application-dev.yaml
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID:your-google-client-id-here}
            client-secret: ${GOOGLE_CLIENT_SECRET:your-google-client-secret-here}
            scope: openid,profile,email
            redirect-uri: "http://localhost:8080/transparency-portal/login/oauth2/code/google"
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://openidconnect.googleapis.com/v1/userinfo
            user-name-attribute: email
```

#### application-prod.yaml
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope: openid,profile,email
            redirect-uri: ${OAUTH2_REDIRECT_URI}
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://openidconnect.googleapis.com/v1/userinfo
            user-name-attribute: email

oauth2:
  redirect-url: ${OAUTH2_REDIRECT_URL}
```

## Setup Instructions

### Step 1: Get Google OAuth2 Credentials
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project
3. Enable "Google+ API"
4. Create OAuth 2.0 credentials:
   - Type: Web Application
   - Authorized redirect URIs:
     - Development: `http://localhost:8080/transparency-portal/login/oauth2/code/google`
     - Production: `https://your-domain.com/transparency-portal/login/oauth2/code/google`
5. Copy your Client ID and Client Secret

### Step 2: Configure Environment Variables

**For Development (in application-dev.yaml or as env vars):**
```
GOOGLE_CLIENT_ID=your-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-client-secret
OAUTH2_REDIRECT_URL=http://localhost:5173
```

**For Production (set these in your deployment environment):**
```
GOOGLE_CLIENT_ID=your-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-client-secret
OAUTH2_REDIRECT_URI=https://your-domain.com/transparency-portal/login/oauth2/code/google
OAUTH2_REDIRECT_URL=https://your-domain.com
```

### Step 3: Update Frontend

Your frontend should have a "Continue with Google" button that redirects to:

```
http://localhost:8080/transparency-portal/oauth2/authorization/google
```

**For Production:**
```
https://your-domain.com/transparency-portal/oauth2/authorization/google
```

This is automatically provided by Spring Security's OAuth2 client.

## Frontend Implementation Example

### React Example
```jsx
function LoginPage() {
  const handleGoogleLogin = () => {
    // Redirect to Spring Security OAuth2 endpoint
    window.location.href = '/transparency-portal/oauth2/authorization/google';
  };

  return (
    <div className="login-container">
      {/* Manual Login */}
      <form onSubmit={handleManualLogin}>
        <input type="text" placeholder="Username" />
        <input type="password" placeholder="Password" />
        <button type="submit">Login</button>
      </form>

      <div className="divider">OR</div>

      {/* OAuth2 Google Login */}
      <button onClick={handleGoogleLogin} className="google-login-btn">
        Continue with Google
      </button>
    </div>
  );
}
```

## How It Works (Detailed Flow)

### Step 1: User Clicks "Continue with Google"
- Frontend redirects to `/oauth2/authorization/google`
- Spring Security intercepts this request

### Step 2: Google Authentication
- Spring Security redirects to Google's OAuth2 authorization endpoint
- User logs in with their Google account
- User grants permission to app

### Step 3: Authorization Code Exchange
- Google sends authorization code back to your app
- Spring Security exchanges code for access token

### Step 4: User Info Retrieval
- Spring Security calls Google's userinfo endpoint
- Gets user details: email, name, profile picture, etc.

### Step 5: OAuth2SuccessHandler Processing
1. Extracts email, name, and Google ID
2. Calls `OAuth2UserService.findOrCreateUser()`
   - Checks if user exists by email
   - If exists: update last login timestamp
   - If new: create user with role "ROLE_USER"
3. Generates JWT token using existing `JWTUtil`
4. Sets JWT as HTTP-only cookie
5. Returns success response with user info

### Step 6: Frontend Receives Response
- User is now authenticated
- JWT cookie is automatically sent with requests
- Frontend can access protected resources

## API Endpoints

### OAuth2 Authorization Flow
```
GET /transparency-portal/oauth2/authorization/google
```
- Redirects to Google's OAuth2 authorization endpoint
- Automatically handled by Spring Security

### OAuth2 Callback (Redirect URI)
```
GET /transparency-portal/login/oauth2/code/google?code=AUTH_CODE&state=STATE
```
- Google redirects here after user authorization
- Spring Security handles code exchange
- On success, executes `OAuth2SuccessHandler`
- Automatically handled by Spring Security

### Manual Logout
```
POST /auth/logout
```
- Clears the JWT cookie

## Database Considerations

### User Model Changes
OAuth2 users are stored in your existing `users` table with:
- `username`: Generated from email prefix
- `email`: From Google account
- `password`: NULL (since OAuth2 doesn't use passwords)
- `role`: "ROLE_USER" (default)
- `created_at`: Timestamp when user first logged in
- `updated_at`: Last login timestamp

### Sample User Records
```
| id | username    | email                | password | role      | created_at          | updated_at          |
|----|-------------|----------------------|----------|-----------|---------------------|---------------------|
| 1  | john        | john@gmail.com       | $2a$12$... | ROLE_USER | 2024-01-15 10:30:00 | 2024-01-15 10:30:00 |
| 2  | jane_123ab  | jane@gmail.com       | NULL     | ROLE_USER | 2024-01-16 14:22:00 | 2024-01-16 14:22:00 |
```

## Security Features

### 1. HTTP-Only Cookies
- JWT stored in HTTP-only cookie
- Prevents JavaScript access (XSS protection)
- Automatically sent with requests

### 2. CSRF Protection
- SameSite=Lax attribute on cookie
- Prevents cross-site request forgery

### 3. Secure Flag
- In production: secure=true (HTTPS only)
- In development: secure=false (HTTP allowed)

### 4. Token Expiration
- JWT expires after 1 hour
- Configurable via `jwt.expiration`

### 5. User Isolation
- Each OAuth2 user gets unique database entry
- Email uniqueness enforced

## Troubleshooting

### Issue: "Client ID not found"
**Solution:** Ensure `GOOGLE_CLIENT_ID` environment variable is set

### Issue: "Redirect URI mismatch"
**Solution:** Verify redirect URI in Google Cloud Console matches `oauth2.redirect-uri` in config

### Issue: "User details not loading"
**Solution:** Check that scopes include `openid`, `profile`, `email`

### Issue: "JWT cookie not being set"
**Solution:** Verify browser allows HTTP-only cookies. In production, check HTTPS is enabled

### Issue: "Manual login and OAuth2 both failing"
**Solution:** Clear browser cookies and try again. Check security configuration allows both auth methods

## Configuration Precedence

Spring Boot property resolution (highest to lowest priority):
1. Environment variables (e.g., `GOOGLE_CLIENT_ID`)
2. System properties (e.g., `-Dspring.security.oauth2.client.registration.google.client-id=...`)
3. application-{profile}.yaml (e.g., application-dev.yaml)
4. application.yaml
5. Default values in config

## Multi-Domain Setup (Production)

For production with frontend and backend on different domains:

**Update SecurityConfig:**
```java
configuration.setAllowedOrigins(
    List.of("https://your-frontend-domain.com"));
    
configuration.setAllowCredentials(true);

// In OAuth2SuccessHandler
.secure(true)  // Requires HTTPS
.sameSite("None")  // Allow cross-domain cookies
```

**Configuration:**
```yaml
oauth2:
  redirect-url: https://your-frontend-domain.com
```

## Testing

### Manual Testing Steps
1. Start your application: `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"`
2. Navigate to frontend login page
3. Click "Continue with Google"
4. Log in with your Google account
5. Verify JWT cookie is set: `document.cookie` in browser console
6. Make an authenticated request and verify JWT is sent

### Integration Tests
Consider adding tests for:
- OAuth2SuccessHandler JWT generation
- OAuth2UserService user creation
- SecurityConfig OAuth2 bean initialization

## Next Steps

1. **Obtain Google OAuth2 credentials** from Google Cloud Console
2. **Set environment variables** for Client ID and Secret
3. **Update frontend** to include "Continue with Google" button
4. **Test OAuth2 flow** locally
5. **Configure production environment** with production OAuth2 credentials

## Support

Refer to:
- [Spring Security OAuth2 Documentation](https://spring.io/projects/spring-security)
- [Google OAuth2 Documentation](https://developers.google.com/identity/protocols/oauth2)
- Spring Boot project README_DOCUMENTATION.md for more details
