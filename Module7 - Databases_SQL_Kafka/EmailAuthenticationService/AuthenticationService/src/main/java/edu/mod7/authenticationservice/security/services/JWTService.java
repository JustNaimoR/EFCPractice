package edu.mod7.authenticationservice.security.services;

import edu.mod7.authenticationservice.models.Client;
import edu.mod7.authenticationservice.security.models.UserDetailsImpl;
import edu.mod7.authenticationservice.services.ClientService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {
    @Value("${security.jwt.jwt-secret}")
    private String SECRET_KEY;
    @Value("${security.jwt.access-expiration-min}")              // 10 minutes
    private int ACCESS_EXPIRATION_MIN;

    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;


    public String generateAccessToken(String username) {
        Client client = clientService.findByUsername(username);

        return generateAccessToken(client);
    }

    public String generateAccessToken(Client client) {
        return generateAccessToken(new UserDetailsImpl(client));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, ACCESS_EXPIRATION_MIN * 60_000L);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationMillis) {
        Date issuedDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + expirationMillis);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(SECRET_KEY))
                .compact();
    }

    // Декодирование ключа для подписи в jwt
    private Key getSigningKey(String key) {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(key)
        );
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }



    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * @param token          - jwt token
     * @param claimsResolver - используемый метод для извлечения нужных полей из jwt
     * @param <T>            - класс извлекаемого Claim
     * @return необходимое поле T
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}