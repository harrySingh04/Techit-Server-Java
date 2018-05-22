package techit.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import techit.model.User;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@ControllerAdvice(basePackages = { "techit.rest.controller" })
public class SecurityControllerAdvice {
   
   @Autowired
   UserDao userDao;
   
   @ModelAttribute("loggedInUser")
   public void
          login(
             @RequestHeader(value = "Authorization",
                            required = false) String bearerToken,
             ModelMap modelMap) {
      
      if (bearerToken == null) {
         throw new RestException(401, "Access denied! User not logged in.");
      }
      
      String token = bearerToken.contains("Bearer")
         ? bearerToken.substring(
            bearerToken.indexOf("Bearer") + "Bearer".length() + 1)
         : bearerToken;
      
      String username = SecurityUtil.getUsernameFromJWT(token);
      
      if (username == null) {
         throw new RestException(401, "Access denied! User token not valid.");
      }
      
      User loggedInUser = userDao.getUser(username);
      
      if (loggedInUser == null) {
         throw new RestException(401, "Access denied! User token not valid.");
      }
      
      modelMap.put("loggedInUser", loggedInUser);
   }
}
