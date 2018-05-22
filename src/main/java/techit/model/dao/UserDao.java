package techit.model.dao;

import java.util.List;

import techit.model.User;

public interface UserDao {
   
   /**
    * getUser
    *
    * @param id
    * @return
    */
   User getUser(int id);
   
   /**
    * getUser
    *
    * @param username
    * @return
    */
   User getUser(String username);
   
   /**
    * getTechnician
    *
    * @param id
    * @return
    */
   User getTechnician(int id);
   
   /**
    * getTechnician
    *
    * @param username
    * @return
    */
   User getTechnician(String username);
   
   /**
    * getSupervisor
    *
    * @param id
    * @return
    */
   User getSupervisor(int id);
   
   /**
    * getSupervisor
    *
    * @param username
    * @return
    */
   User getSupervisor(String username);
   
   /**
    * getUsers
    *
    * @return
    */
   List<User> getUsers();
   
   /**
    * getTechnicians
    *
    * @return
    */
   List<User> getTechnicians();
   
   /**
    * getSupervisors
    *
    * @return
    */
   List<User> getSupervisors();
   
   /**
    * saveUser
    *
    * @param user
    * @return
    */
   User saveUser(User user);
}