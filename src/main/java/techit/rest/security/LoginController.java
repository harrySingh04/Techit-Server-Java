package techit.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import techit.model.User;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@RestController
public class LoginController {
   @Autowired
   private UserDao userDao;
   
   @RequestMapping(value = "/login", method = RequestMethod.POST)
   public String login(@RequestParam("username") String username,
      @RequestParam("password") String password) {
      
      if (username == null || password == null) {
         throw new RestException(401, "Username and/or password is null");
      }
      
      User user = userDao.getUser(username);
      if (user == null) {
         throw new RestException(401, "Invalid Username and/or password");
      }
      
      if (!SecurityUtil.checkPasswordMatch(password, user.getPassword())) {
         throw new RestException(401, "Invalid Username and/or password");
      }
      
      String compactJws = SecurityUtil.convertToJWT(username);
      
      return compactJws;
   }
}
