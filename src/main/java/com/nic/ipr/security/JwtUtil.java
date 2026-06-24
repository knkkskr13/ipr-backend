package com.nic.ipr.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final long EXPIRATION=10*60*60*1000;//its in miliseconds
    private final String SECRET = "niciprsecretkey123456789012345678901234";

    // Generates a SecretKey from the SECRET string for HMAC-SHA signing of JWTs
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Generate token for a username
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)                     // 1. Set the subject (the user identity)
                .issuedAt(new Date())                  // 2. Add the issue timestamp (when token was created)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 3. Set expiry time (10 hours later)
                .signWith(getSigningKey())             // 4. Sign the token using your secret key (HMAC-SHA)
                .compact();                            // 5. Build and return the final JWT string
    }


    // Extract username from token
    public String extractUsername(String token) {
        return Jwts.parser()                       // 1. Start building a JWT parser
                .verifyWith(getSigningKey())       // 2. Provide the secret key to verify the signature
                .build()                           // 3. Build the parser instance
                .parseSignedClaims(token)          // 4. Parse the token and validate its signature
                .getPayload()                      // 5. Get the claims (payload) inside the token
                .getSubject();                     // 6. Extract the "sub" (subject = username) claim
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username)   // 1. Confirm the token belongs to the given user
                && !isTokenExpired(token);                   // 2. Ensure the token is still valid (not expired)
    }


    private boolean isTokenExpired(String token) {
        return Jwts.parser()                       // 1. Create a JWT parser
                .verifyWith(getSigningKey())       // 2. Verify the token's signature using your secret key
                .build()                           // 3. Build the parser
                .parseSignedClaims(token)          // 4. Parse the token into claims (payload)
                .getPayload()                      // 5. Access the claims object
                .getExpiration()                   // 6. Read the "exp" (expiration) claim
                .before(new Date());               // 7. Compare with current time
    }
}
