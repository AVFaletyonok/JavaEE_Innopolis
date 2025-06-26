package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class Main
{
//    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

//    public static String generateToken(String userName) {
//        return Jwts.builder()
//                .setSubject(userName)
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
//                .compact();
//    }
//
//    public static String validateToken(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//            return claims.getSubject();
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
