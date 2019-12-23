package service;

import dao.TeachingBuildingDao;
import domain.ClassRoom;
import domain.TeachingBuilding;

import java.sql.SQLException;
import java.util.Collection;

public class TeachingBuildingService {
    private static TeachingBuildingService teachingBuildingService = new TeachingBuildingService();
    private TeachingBuildingService(){}
    public static TeachingBuildingService getInstance(){return teachingBuildingService;}
    public Collection<TeachingBuilding> findAll() throws SQLException {
        return TeachingBuildingDao.getInstance().findAll();
    }
    public TeachingBuilding find(Integer id) throws SQLException {
        return TeachingBuildingDao.getInstance().find(id);
    }
}
