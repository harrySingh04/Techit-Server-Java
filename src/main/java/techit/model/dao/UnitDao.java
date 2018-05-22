package techit.model.dao;

import java.util.List;

import techit.model.Unit;

public interface UnitDao {
   
   /**
    * saveUnit
    *
    * @param unit
    * @return
    */
   Unit saveUnit(Unit unit);
   
   /**
    * getUnit
    *
    * @param unitId
    * @return
    */
   Unit getUnit(int unitId);
   
   /**
    * getUnits
    *
    * @return
    */
   List<Unit> getUnits();
}