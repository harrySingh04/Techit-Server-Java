package techit.model.dao.jpa;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.User;
import techit.model.dao.UserDao;
import techit.model.helper.UserRole;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public User getUser(int id) {
      User user = null;
      try {
         user = entityManager.find(User.class, id);
      }
      catch (NoResultException e) {
         // Ignore
      }
      return user;
   }

   @Override
   public User getUser(String username) {
      User user = null;
      try {
         user = entityManager.createQuery("from User where username = ?",
            User.class).setParameter(0, username).getSingleResult();
      }
      catch (NoResultException e) {
         // Do nothing
      }
      return user;
   }

   @Override
   public User getTechnician(int id) {
      User user = null;
      try {
         user = entityManager.createQuery(
            "from User where id = ? and userRoleId = ?",
            User.class).setParameter(0, id).setParameter(1,
               UserRole.TECHNICIAN.getValue()).getSingleResult();
      }
      catch (NoResultException e) {
         // Do nothing
      }
      return user;
   }

   @Override
   public User getTechnician(String username) {
      User user = null;
      try {
         user = entityManager.createQuery(
            "from User where username = ? and userRoleId = ?",
            User.class).setParameter(0, username).setParameter(1,
               UserRole.TECHNICIAN.getValue()).getSingleResult();
      }
      catch (NoResultException e) {
         // Do nothing
      }
      return user;
   }

   @Override
   public User getSupervisor(int id) {
      User user = null;
      try {
         user = entityManager.createQuery(
            "from User where id = ? and userRoleId = ?",
            User.class).setParameter(0, id).setParameter(1,
               UserRole.SUPERVISOR.getValue()).getSingleResult();
      }
      catch (NoResultException e) {
         // Do nothing
      }
      return user;
   }

   @Override
   public User getSupervisor(String username) {
      User user = null;
      try {
         user = entityManager.createQuery(
            "from User where username = ? and userRoleId = ?",
            User.class).setParameter(0, username).setParameter(1,
               UserRole.SUPERVISOR.getValue()).getSingleResult();
      }
      catch (NoResultException e) {
         // Do nothing
      }
      return user;
   }

   @Override
   public List<User> getUsers() {
      List<User> users;
      try {
         users = entityManager.createQuery("from User order by id",
            User.class).getResultList();
      }
      catch (NoResultException e) {
         users = Collections.emptyList();
      }
      return users;
   }

   @Override
   public List<User> getTechnicians() {
      List<User> users;
      try {
         users = entityManager.createQuery(
            "from User where userRoleId = ? order by id",
            User.class).setParameter(0,
               UserRole.TECHNICIAN.getValue()).getResultList();
      }
      catch (NoResultException e) {
         users = Collections.emptyList();
      }
      return users;
   }

   @Override
   public List<User> getSupervisors() {
      List<User> users;
      try {
         users = entityManager.createQuery(
            "from User where userRoleId = ? order by id",
            User.class).setParameter(0,
               UserRole.SUPERVISOR.getValue()).getResultList();
      }
      catch (NoResultException e) {
         users = Collections.emptyList();
      }
      return users;
   }

   @Override
   public User saveUser(User user) {
      return entityManager.merge(user);
   }
}
