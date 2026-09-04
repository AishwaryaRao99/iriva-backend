# OAuth2 Google Login - Quick Setup Checklist

## ✅ What Was Implemented

### New Components Created:
- ✅ `OAuth2SuccessHandler.java` - Handles OAuth2 authentication success
- ✅ `OAuth2UserService.java` - Manages OAuth2 user creation/lookup
- ✅ Security configuration for OAuth2 login
- ✅ YAML configuration for Google OAuth2
- ✅ Documentation and guides

### Modified Components:
- ✅ `SecurityConfig.java` - Added OAuth2 login configuration
- ✅ `AuthenticationController.java` - Added OAuth2 endpoint
- ✅ `application.yaml` - Added OAuth2 properties
- ✅ `application-dev.yaml` - Added Google OAuth2 config
- ✅ `application-prod.yaml` - Added production OAuth2 config

## 📋 Quick Setup Steps

### 1. Get Google OAuth2 Credentials (5 minutes)
```
1. Go to https://console.cloud.google.com/
2. Create new project
3. Enable "Google+ API"
4. Create OAuth 2.0 Web Application credentials
5. Add Redirect URIs:
   - Local: http://localhost:8080/transparency-portal/login/oauth2/code/google
   - Prod: https://your-domain.com/transparency-portal/login/oauth2/code/google
6. Copy Client ID and Secret
```

### 2. Set Environment Variables (2 minutes)

**Option A: Set in application-dev.yaml**
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_CLIENT_ID_HERE
            client-secret: YOUR_CLIENT_SECRET_HERE
```

**Option B: Set as Environment Variables (Recommended for Production)**
```bash
export GOOGLE_CLIENT_ID="your-client-id.apps.googleusercontent.com"
export GOOGLE_CLIENT_SECRET="your-client-secret"
export OAUTH2_REDIRECT_URL="http://localhost:5173"
```

### 3. Test Backend (Local)
```bash
# Build and run
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Verify startup - should see no errors about OAuth2
# Check logs for: "OAuth2 authentication successful" when testing
```

### 4. Update Frontend (10 minutes)

Add a "Continue with Google" button that redirects to:
```javascript
// Login button click handler
const handleGoogleLogin = () => {
  window.location.href = '/transparency-portal/oauth2/authorization/google';
};
```

### 5. Test Full Flow (10 minutes)
1. Start backend: `mvn spring-boot:run`
2. Start frontend: `npm run dev`
3. Click "Continue with Google" button
4. Log in with your Google account
5. Check browser cookies for `jwt` cookie
6. Verify authenticated requests work

## 🔧 Configuration Reference

### Environment Variables
| Variable | Purpose | Example |
|----------|---------|---------|
| `GOOGLE_CLIENT_ID` | Google OAuth2 Client ID | `123456.apps.googleusercontent.com` |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 Client Secret | `GOCSPX-xxxxx` |
| `OAUTH2_REDIRECT_URL` | Frontend redirect URL | `http://localhost:5173` |
| `OAUTH2_REDIRECT_URI` | Backend redirect URI | `http://localhost:8080/.../code/google` |

### Application Properties
| Property | File | Purpose |
|----------|------|---------|
| `spring.security.oauth2.client.registration.google.client-id` | dev/prod yaml | Google Client ID |
| `spring.security.oauth2.client.registration.google.client-secret` | dev/prod yaml | Google Client Secret |
| `spring.security.oauth2.client.registration.google.redirect-uri` | dev/prod yaml | OAuth2 callback URI |
| `oauth2.redirect-url` | application.yaml | Frontend URL for redirect |

## 🔐 How It Works

```
User clicks "Continue with Google"
    ↓
Redirects to: /oauth2/authorization/google
    ↓
Spring Security forwards to Google login
    ↓
User authenticates with Google
    ↓
Google sends authorization code back
    ↓
Spring Security exchanges code for token
    ↓
Gets user info (email, name, etc.)
    ↓
OAuth2SuccessHandler processes authentication
    ├─ Checks if user exists in database
    ├─ If not → Creates new user
    ├─ Generates JWT token
    └─ Sets HTTP-only cookie
    ↓
User is authenticated
    ↓
Frontend receives user info
```

## 📊 Database Changes

OAuth2 users are stored in existing `users` table:

```sql
INSERT INTO users (username, email, password, role, created_at, updated_at) 
VALUES ('john_123ab', 'john@gmail.com', NULL, 'ROLE_USER', NOW(), NOW());
```

**Key Points:**
- `password` is NULL for OAuth2 users
- `username` is auto-generated from email
- `role` defaults to "ROLE_USER"
- Manual login users still have encrypted passwords

## 🎯 Frontend Integration Examples

### React
```jsx
<button onClick={() => {
  window.location.href = '/transparency-portal/oauth2/authorization/google';
}}>
  Continue with Google
</button>
```

### Vue
```vue
<button @click="googleLogin">
  Continue with Google
</button>

<script>
export default {
  methods: {
    googleLogin() {
      window.location.href = '/transparency-portal/oauth2/authorization/google';
    }
  }
}
</script>
```

### Plain HTML/JavaScript
```html
<button onclick="window.location.href='/transparency-portal/oauth2/authorization/google'">
  Continue with Google
</button>
```

## ✔️ Verification Checklist

- [ ] Google OAuth2 credentials obtained
- [ ] `GOOGLE_CLIENT_ID` environment variable set
- [ ] `GOOGLE_CLIENT_SECRET` environment variable set
- [ ] Backend starts without OAuth2 configuration errors
- [ ] Application logs show OAuth2 beans initialized
- [ ] Frontend "Continue with Google" button redirects correctly
- [ ] OAuth2 login flow completes successfully
- [ ] JWT cookie is set after OAuth2 login
- [ ] User exists in database with NULL password
- [ ] Subsequent requests include JWT cookie automatically

## 🐛 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| `Client ID not found` | Set `GOOGLE_CLIENT_ID` env var or in YAML |
| `Redirect URI mismatch` | Match URI in Google Console with backend config |
| `OAuth2 login fails silently` | Check backend logs for detailed error message |
| `JWT cookie not set` | Verify `OAuth2SuccessHandler` is being called |
| `Manual login still required` | Both methods should work simultaneously |
| `CORS error on Google redirect` | Check CORS config allows Google domain |

## 📝 Files Created/Modified

### New Files
- `src/main/java/.../security/OAuth2SuccessHandler.java`
- `src/main/java/.../user/service/OAuth2UserService.java`
- `OAUTH2_GOOGLE_LOGIN_GUIDE.md`
- `OAUTH2_QUICK_SETUP.md` (this file)

### Modified Files
- `src/main/java/.../configuration/SecurityConfig.java`
- `src/main/java/.../auth/controller/AuthenticationController.java`
- `src/main/resources/application.yaml`
- `src/main/resources/application-dev.yaml`
- `src/main/resources/application-prod.yaml`

## 🚀 Deployment Steps

### For Production:

1. **Get Production Google OAuth2 Credentials**
   - Update redirect URI to production domain

2. **Set Production Environment Variables**
   ```bash
   export GOOGLE_CLIENT_ID="prod-client-id"
   export GOOGLE_CLIENT_SECRET="prod-client-secret"
   export OAUTH2_REDIRECT_URI="https://your-domain.com/transparency-portal/login/oauth2/code/google"
   export OAUTH2_REDIRECT_URL="https://your-domain.com"
   ```

3. **Update application-prod.yaml**
   - Ensure all placeholders are replaced with actual values

4. **Deploy Backend**
   - Build: `mvn clean package`
   - Deploy JAR with environment variables set

5. **Test in Production**
   - Verify OAuth2 flow works with production domain
   - Monitor logs for any OAuth2 errors

## 📚 Additional Resources

- [Spring Security OAuth2 Docs](https://spring.io/projects/spring-security)
- [Google OAuth2 Setup Guide](https://developers.google.com/identity/protocols/oauth2)
- [HTTP-Only Cookies Security](https://owasp.org/www-community/attacks/xss/)
- [CSRF Prevention](https://owasp.org/www-community/attacks/csrf)

## ✨ Key Features

✅ **Dual Authentication Methods**
- Manual login with username/password (existing JWT)
- OAuth2 login with Google (new)

✅ **Unified JWT Tokens**
- Both auth methods generate the same JWT token
- Same cookie settings and expiration

✅ **Automatic User Creation**
- New Google users automatically added to database
- Default role assigned

✅ **Security Best Practices**
- HTTP-only cookies prevent XSS
- CSRF protection with SameSite attribute
- HTTPS support for production

✅ **No Breaking Changes**
- Existing manual login still works
- Existing users unaffected
- Backward compatible

## 🎉 Next Steps

1. Complete the [Google OAuth2 Credentials Setup](OAUTH2_GOOGLE_LOGIN_GUIDE.md#step-1-get-google-oauth2-credentials)
2. Set environment variables
3. Run local tests
4. Update frontend with "Continue with Google" button
5. Deploy to production

---

**Need help?** Refer to `OAUTH2_GOOGLE_LOGIN_GUIDE.md` for detailed documentation.
