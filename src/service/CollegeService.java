package service;

import dao.CollegeDao;
import domain.College;

import java.sql.SQLException;
import java.util.Collection;

public class CollegeService {
    private static CollegeService collegeService = new CollegeService();
    private CollegeDao collegeDao = CollegeDao.getInstance();

    private CollegeService(){}

    public static CollegeService getInstance(){
        return collegeService;
    }
    public Collection<College> findAll() throws SQLException {
        return collegeDao.findAll();
    }
    public College find(Integer id) throws SQLException{
        return collegeDao.find(id);
    }
    public boolean update(College college) throws SQLException {
        return collegeDao.update(college);
    }
    public boolean add(College college) throws SQLException {
        return collegeDao.add(college);
    }
    public boolean delete(College college) throws SQLException {
        return collegeDao.delete(college);
    }

}
