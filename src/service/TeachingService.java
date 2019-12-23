package service;

import dao.TeachingDao;
import domain.Teaching;

import java.sql.SQLException;
import java.util.Collection;

public class TeachingService {
    private static TeachingService teachingService = new TeachingService();
    private TeachingDao teachingDao = TeachingDao.getInstance();

    private TeachingService(){}

    public static TeachingService getInstance(){
        return teachingService;
    }

    public Collection<Teaching> findAll() throws SQLException {
        return teachingDao.findAll();
    }
    public Teaching find(Integer id) throws SQLException {
        return teachingDao.find(id);
    }

    public void update(Teaching teaching) throws SQLException {
        teachingDao.update(teaching);
    }

    public void add(Teaching teaching) throws SQLException {
        teachingDao.add(teaching);
    }

    public void delete(Integer id) throws SQLException {
        Teaching teaching = this.find(id);
        this.delete(teaching);
    }

    public void delete(Teaching teaching) throws SQLException {
        teachingDao.delete(teaching);
    }
}
