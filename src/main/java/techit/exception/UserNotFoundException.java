package techit.exception;

import techit.model.User;

public class UserNotFoundException extends WorkOrderException {
   
   private static final long serialVersionUID = 1L;
   
   public UserNotFoundException(Class<? extends User> userClass, int userId) {
      super("GeneralUser of type " + userClass.getSimpleName() + " with id "
         + userId + " not found in the system");
   }
   
   public UserNotFoundException(Class<? extends User> userClass,
      String userKey) {
      super("GeneralUser of type " + userClass.getSimpleName() + " with key "
         + userKey + " not found in the system");
   }
}
