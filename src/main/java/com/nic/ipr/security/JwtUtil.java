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

    //-----------------Token Generation--- ENCRYPTION-------------//

    // Generate token for a username
    public String generateToken(String username,String role) {
        return Jwts.builder()
                .subject(username)                     // 1. Set the subject (the user identity)
                .claim("role", role)             //2.Embed the role into the token claims payload
                .issuedAt(new Date())                  // 3. Add the issue timestamp (when token was created)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 4. Set expiry time (10 hours later)
                .signWith(getSigningKey())             // 5. Sign the token using your secret key (HMAC-SHA)
                .compact();                            // 6. Build and return the final JWT string
    }
    //----------------------------------------------------------//



    // helper method , helps in extraction of role in JwtUtil.java
    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
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
