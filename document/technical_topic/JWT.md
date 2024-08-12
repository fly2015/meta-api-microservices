### How to logout JWT?
- The most common way to logout a JWT is to blacklist the token on the server side. This means that the server will keep a list of blacklisted tokens and will check every incoming token against this list. If the token is in the blacklist, the server will reject it.
- Another way to logout a JWT is to use a short expiration time. This means that the token will expire after a certain amount of time, and the user will have to log in again to get a new token.
- You can also use a refresh token to logout a JWT. This means that the server will issue a new token every time the user logs in, and the old token will be invalidated.
- Finally, you can also use a combination of the above methods to logout a JWT. For example, you can use a short expiration time and a refresh token to invalidate the token after a certain amount of time.
- It is important to note that JWTs are stateless, which means that the server does not keep track of the tokens. This makes it difficult to logout a JWT, but the methods mentioned above can help you achieve this.
- In conclusion, there are several ways to logout a JWT, and the best method will depend on your specific use case. It is important to consider the security implications of each method and choose the one that best fits your needs.
- [Reference](https://auth0.com/blog/blacklist-jwts-invalidating-jwts-and-logout/)
- [Reference](https://stackoverflow.com/questions/21978658/invalidating-json-web-tokens)

### How to invalidate JWT?
In a real-world application, logging out and revoking a JWT can be a bit tricky because JWTs are stateless by design. However, there are a few strategies you can use:

1. **Token Blacklisting**: You can create a token blacklist on your server. When a user logs out, you add their token to the blacklist. Then, whenever a request comes in with a token, you check if it's on the blacklist before proceeding.

2. **Token Expiration**: You can set a short expiration time for your tokens and then refresh them if the user is still active. This way, when a user logs out, the token will expire shortly afterwards.

3. **Change User's Secret Key**: If you're using a different secret key for each user, you can change the user's secret key when they log out. This will invalidate all tokens issued with the old key.

Here's an example of how you might implement a logout endpoint in a Spring Boot application using the token blacklisting strategy:

```java
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    private final TokenBlacklist tokenBlacklist;

    public LogoutController(TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    @PostMapping("/logout")
    public void logout(String token) {
        tokenBlacklist.add(token);
    }
}
```

In this example, `TokenBlacklist` is a hypothetical service that you would need to implement. It could be a simple in-memory data structure (like a `Set`), or a more complex solution backed by a database or cache, depending on your needs.

Please note that this is a simplified example and a real-world implementation would need to consider additional factors, such as token extraction from the request and security considerations.

### How the oAuth2 to work?
OAuth 2.0 is an authorization framework that enables applications to obtain limited access to user accounts on an HTTP service, such as Facebook, GitHub, and DigitalOcean. It works by delegating user authentication to the service that hosts the user account and authorizing third-party applications to access the user account. OAuth 2.0 provides authorization flows for web and desktop applications, and mobile devices.

OAuth2 is a protocol that allows applications to authorize access to their data in a secure manner. It is commonly used for "Login with Google" or "Login with Facebook" features. Here's a simplified explanation of how it works:
1. **Authorization Request**: The client (your application) redirects the user to the authorization server (Google or Facebook). This request includes the client ID, response type, and redirect URI.

2. **User Login**: The user logs in to the authorization server with their credentials.

3. **User Consent**: The authorization server asks the user if they grant the client permission to access their data.

4. **Authorization Grant**: If the user grants permission, the authorization server redirects the user back to the client with an authorization code.

5. **Access Token Request**: The client sends a request to the authorization server, exchanging the authorization code for an access token. This request includes the client ID, client secret, authorization code, and redirect URI.

6. **Access Token Response**: The authorization server verifies the authorization code and the client's credentials, and if valid, it returns an access token to the client.

7. **Protected Resource Request**: The client uses the access token to request the protected resource (user data) from the resource server.

8. **Protected Resource Response**: The resource server verifies the access token, and if valid, it returns the requested data to the client.

This is a simplified explanation and actual implementations may vary. For example, there are different types of authorization grants for different use cases, and there may be additional steps for refreshing the access token or for handling errors.

### After I get the access token, what should I do with the token, or if I had access token, it proves that I can access resource?
After you receive the access token, you should store it securely. In a web application, you might store it in an HTTP-only cookie. In a mobile application, you might store it in secure storage provided by the operating system.

Once you have the access token, you can use it to authenticate subsequent requests to the server. When making a request to a protected resource, you include the access token in the Authorization header of the HTTP request. The server will validate the token and, if it's valid, process the request.

Here's an example of how you might include the token in a request using the `RestTemplate` class in Spring:

```java
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    private RestTemplate restTemplate = new RestTemplate();

    public String getResource(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
```

In this code, the `getResource` method makes a GET request to the specified URL. The access token is included in the Authorization header of the request.

Remember, having an access token does not automatically mean you can access all resources. The server will still check if the token is valid and if the token owner has the necessary permissions to access the requested resource.

### How server validate the access token that provided from Oauth2
The server validates the access token provided from OAuth2 by following these steps:

1. Extract the token from the Authorization header of the incoming request.
2. Decode the token. OAuth2 access tokens are typically JWTs (JSON Web Tokens), which are base64-encoded JSON strings.
3. Verify the signature of the token. This ensures that the token was issued by the expected OAuth2 provider and has not been tampered with.
4. Check the claims in the token. This typically includes checking the issuer (to ensure it matches the expected OAuth2 provider), the audience (to ensure it matches your application), and the expiration time (to ensure the token has not expired).
5. If all these checks pass, the token is considered valid and the request is processed. If any check fails, the server responds with an error.

Here's an example of how you might implement this in Java using the `JwtTokenProvider` class:

```java
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public Claims validateToken(String authToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();
            return claims;
        } catch (SignatureException ex) {
            throw new IllegalArgumentException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new IllegalArgumentException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new IllegalArgumentException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new IllegalArgumentException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT claims string is empty.");
        }
    }
}
```

In this code, the `validateToken` method takes an access token as input and validates it. If the token is valid, it returns the claims in the token. If the token is not valid, it throws an exception.

Please note that this is a simplified example. In a real-world application, you would also need to handle the case where the token is not present or is in an invalid format. You would also need to secure the JWT secret to ensure it cannot be accessed by unauthorized parties.

### But how my serve check the token that is provided by facebook or google?
When your server receives an access token from a client that was issued by an OAuth2 provider like Facebook or Google, it needs to validate the token to ensure it's legitimate. This process typically involves making a request to the OAuth2 provider's token introspection endpoint or user info endpoint.

Here's a simplified version of how this might work:

1. Extract the access token from the Authorization header of the incoming request.
2. Make a request to the OAuth2 provider's token introspection endpoint or user info endpoint, including the access token as a parameter.
3. The OAuth2 provider will respond with a JSON object that includes information about the token, such as who it was issued to, its expiration time, and the scopes it includes.
4. Your server can use this information to validate the token and determine whether the request should be processed.

Here's an example of how you might implement this in Java using Spring's `RestTemplate`:

```java
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TokenValidationService {

    private RestTemplate restTemplate = new RestTemplate();

    public boolean validateToken(String token, String introspectionUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(introspectionUrl, HttpMethod.GET, entity, String.class);

        // Parse the response and check the token validity
        // This will depend on the specific response format of the OAuth2 provider
        // For example, Google's tokeninfo endpoint returns a 'expires_in' field that can be used to check if the token is expired

        return true; // return true if the token is valid, false otherwise
    }
}
```

In this code, the `validateToken` method makes a GET request to the OAuth2 provider's token introspection endpoint. The access token is included in the Authorization header of the request. The response from the OAuth2 provider is then used to determine whether the token is valid.

Please note that the specific details of how to validate the token will depend on the specific OAuth2 provider. You should refer to the provider's documentation for details.

### The step sent the access token to Auth provider to verify is important?
Yes, the step of sending the access token to the OAuth2 provider for verification is important. This step is crucial for the following reasons:

1. **Token Validity**: The OAuth2 provider can confirm whether the token is still valid. Access tokens have an expiration time, and once that time has passed, the token is no longer valid.

2. **Token Integrity**: The OAuth2 provider can confirm whether the token has been tampered with. If the token has been modified after it was issued, it's considered invalid.

3. **Token Ownership**: The OAuth2 provider can confirm whether the token was issued to the client making the request. This helps prevent token theft and misuse.

By verifying the access token with the OAuth2 provider, your server can ensure that it's interacting with the correct, authenticated user and that the user's request is authorized.
### How to implement refresh token with JWT?
Implementing refresh tokens with JWT involves a few steps. Here's a step-by-step guide:

1. **Issue a Refresh Token**: When the user logs in, along with the JWT, also generate a refresh token. The refresh token should be stored securely on the server against the user's ID.

2. **Store the Refresh Token**: On the client side, store the refresh token securely. In a web application, you might store it in an HTTP-only cookie.

3. **Use the Refresh Token**: When the JWT expires, the client should make a request to a special endpoint that accepts the refresh token and returns a new JWT.

4. **Validate the Refresh Token**: On the server side, when a request is made to the refresh endpoint, validate the refresh token against the one stored on the server. If it's valid, issue a new JWT.

5. **Revoke the Refresh Token**: If a user logs out, the refresh token should be revoked on the server side to prevent it from being used to generate new JWTs.

Here's how you might implement this in Java using Spring Boot:

First, add a new method in `JwtTokenProvider` to generate a refresh token:

```java
public String generateRefreshToken() {
    return Jwts.builder()
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationInMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
}
```

Next, modify the `login` method in `AuthServiceImpl` to return both access and refresh tokens:

```java
@Override
public JWTAuthResponse login(LoginDto loginDto) {

    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginDto.getUsernameOrEmail(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtTokenProvider.generateToken(authentication);
    String refreshToken = jwtTokenProvider.generateRefreshToken();

    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccessToken(jwt);
    jwtAuthResponse.setRefreshToken(refreshToken);

    return jwtAuthResponse;
}
```

Then, create a new endpoint in `AuthController` to refresh the JWT:

```java
@PostMapping("/refresh")
public ResponseEntity<JWTAuthResponse> refresh(@RequestBody RefreshTokenDto refreshTokenDto) {
    String token = authService.refresh(refreshTokenDto.getRefreshToken());

    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccessToken(token);

    return ResponseEntity.ok(jwtAuthResponse);
}
```

Finally, implement the `refresh` method in `AuthServiceImpl`:

```java
@Override
public String refresh(String refreshToken) {
    if (jwtTokenProvider.validateToken(refreshToken)) {
        String username = jwtTokenProvider.getUsernameFromJWT(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    } else {
        throw new IllegalArgumentException("Invalid refresh token");
    }
}
```

Please note that this is a simplified example. In a real-world application, you would also need to handle the case where the refresh token is not present or is in an invalid format. You would also need to secure the JWT secret to ensure it cannot be accessed by unauthorized parties.

### JWT is stateless, how authentication session information that is stored on the server side.
JWT is stateless, meaning that the server does not store any session information about the user. This is one of the key advantages of JWT, as it allows for easy scaling and distribution of services. However, there are some trade-offs when it comes to session information:
JWT (JSON Web Tokens) are indeed stateless, meaning they are self-contained and do not require the server to keep a record or session data for individual tokens. The token itself contains all the necessary information for authentication and authorization.

However, in some cases, you might want to keep track of issued tokens on the server-side for additional control, such as allowing tokens to be manually revoked before they expire. This is not typical JWT usage, but it's possible.

One way to do this is by storing issued tokens in a database or cache along with the user's ID and other session information. When a request with a token comes in, you can then check the token against the list of issued tokens in your database or cache.

Here's a simplified example of how you might implement this in Java using Spring Boot and a hypothetical `TokenStore`:

```java
@Service
public class AuthServiceImpl implements AuthService {

    // ... existing fields ...

    private TokenStore tokenStore; // hypothetical TokenStore for storing tokens

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           TokenStore tokenStore) {
        // ... existing initializations ...

        this.tokenStore = tokenStore;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        // Store the token in the TokenStore along with the user's ID
        User user = (User) authentication.getPrincipal();
        tokenStore.storeToken(user.getId(), token);

        return token;
    }

    // ... existing methods ...
}
```

In this example, when a user logs in, the generated JWT is stored in a `TokenStore` along with the user's ID. The `TokenStore` could be a simple in-memory store, a database, a distributed cache, or any other type of persistent or semi-persistent storage.

Please note that this approach introduces state into your application and can complicate your architecture, especially in distributed systems. It also somewhat defeats the purpose of using JWTs, which are designed to be stateless. If you find yourself needing to store session information on the server, you might want to consider using traditional session-based authentication instead.

### I just want to know how to implement JWT and refresh token stateless
To implement JWT and refresh tokens in a stateless manner, you can follow these steps:

1. **Issue JWT and Refresh Token**: When the user logs in, generate a JWT and a refresh token. The JWT contains the user's identity and other necessary information, while the refresh token is a random string.

2. **Send Tokens to Client**: Send both the JWT and the refresh token to the client. The client stores these tokens, typically in an HTTP-only cookie or local storage.

3. **Client Sends JWT**: For each request, the client sends the JWT in the Authorization header. The server verifies the JWT signature and extracts the user's information.

4. **Refresh the JWT**: When the JWT expires, the client sends the refresh token to a special endpoint on the server. The server verifies the refresh token, and if it's valid, issues a new JWT and refresh token.

5. **Send New Tokens to Client**: The server sends the new JWT and refresh token to the client, and the process repeats.

Here's how you might implement this in Java using Spring Boot:

First, add a new method in `JwtTokenProvider` to generate a refresh token:

```java
public String generateRefreshToken() {
    return UUID.randomUUID().toString();
}
```

Next, modify the `login` method in `AuthServiceImpl` to return both access and refresh tokens:

```java
@Override
public JWTAuthResponse login(LoginDto loginDto) {

    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginDto.getUsernameOrEmail(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtTokenProvider.generateToken(authentication);
    String refreshToken = jwtTokenProvider.generateRefreshToken();

    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccessToken(jwt);
    jwtAuthResponse.setRefreshToken(refreshToken);

    return jwtAuthResponse;
}
```

Then, create a new endpoint in `AuthController` to refresh the JWT:

```java
@PostMapping("/refresh")
public ResponseEntity<JWTAuthResponse> refresh(@RequestBody Map<String, String> payload) {
    String refreshToken = payload.get("refreshToken");
    String newJwt = authService.refresh(refreshToken);

    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccessToken(newJwt);
    jwtAuthResponse.setRefreshToken(jwtTokenProvider.generateRefreshToken());

    return ResponseEntity.ok(jwtAuthResponse);
}
```

Finally, implement the `refresh` method in `AuthServiceImpl`:

```java
@Override
public String refresh(String refreshToken) {
    // In a real-world application, you should validate the refresh token against a stored value
    // Here we're just checking if it's not null or empty
    if (refreshToken != null && !refreshToken.isEmpty()) {
        // Generate a new JWT
        // In a real-world application, you should probably re-check the user's credentials or other authentication factors
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return jwtTokenProvider.generateToken(authentication);
    } else {
        throw new IllegalArgumentException("Invalid refresh token");
    }
}
```

Please note that this is a simplified example. In a real-world application, you would need to handle the case where the refresh token is not present or is in an invalid format. You would also need to secure the JWT secret to ensure it cannot be accessed by unauthorized parties.

### What is the difference between JWT and OAuth?