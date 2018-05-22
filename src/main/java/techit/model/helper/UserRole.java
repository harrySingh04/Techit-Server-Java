package techit.model.helper;

public enum UserRole {
   SYS_ADMIN(0), SUPERVISOR(1), TECHNICIAN(2), USER(3);

   private int roleValue;

   private UserRole(int roleValue) {
      this.roleValue = roleValue;
   }

   public int getValue() {
      return roleValue;
   }

   public static UserRole valueOf(int roleId) {
      for (UserRole userRole : values()) {
         if (userRole.getValue() == roleId) {
            return userRole;
         }
      }
      throw new IllegalArgumentException("Invalid user role id " + roleId);
   }
};