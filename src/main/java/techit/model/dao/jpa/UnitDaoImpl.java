package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Unit;
import techit.model.dao.UnitDao;

@Repository
@Transactional
public class UnitDaoImpl implements UnitDao {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public Unit saveUnit(Unit unit) {
      return entityManager.merge(unit);
   }

   @Override
   public Unit getUnit(int unitId) {
      try {
         Unit unit = entityManager.find(Unit.class, unitId);

         return unit;
      }
      catch (NoResultException e) {
         return null;
      }
   }

   @Override
   public List<Unit> getUnits() {
      try {
         List<Unit> units = entityManager.createQuery("from Unit order by id",
            Unit.class).getResultList();

         return units;
      }
      catch (NoResultException e) {
         return null;
      }
   }
}
