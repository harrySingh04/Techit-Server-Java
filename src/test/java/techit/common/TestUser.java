package techit.common;

import techit.model.User;

public class TestUser extends User {
   private static final long serialVersionUID = 1L;
   
   private String password;
   
   /**
    * getPassword - Returns the value of password
    *
    * @return the password
    */
   @Override
   public String getPassword() {
      return password;
   }
   
   /**
    * setPassword - Sets the value of password
    *
    * @param password
    *           the password to set
    */
   @Override
   public void setPassword(String password) {
      this.password = password;
   }
}