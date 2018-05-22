package techit.rest.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class SecurityUtil {
   private static final String KEY = "SECRET_KEY";
   private static final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
   
   public static String encryptPassword(String planTextPassword) {
      return bcryptEncoder.encode(planTextPassword);
   }
   
   public static boolean checkPasswordMatch(String plainTextPassword,
      String encryptedUserPassword) {
      return bcryptEncoder.matches(plainTextPassword, encryptedUserPassword);
   }
   
   public static String convertToJWT(String username) {
      return Jwts.builder().setSubject(username).signWith(
         SignatureAlgorithm.HS256, KEY).compact();
   }
   
   public static String getUsernameFromJWT(String token) {
      try {
         return Jwts.parser().setSigningKey(KEY).parseClaimsJws(
            token).getBody().getSubject();
      }
      catch (SignatureException e) {
         return null;
      }
   }
}
