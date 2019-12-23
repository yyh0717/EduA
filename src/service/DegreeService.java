package service;

import dao.CourseDao;
import dao.DegreeDao;
import domain.Degree;

import java.sql.SQLException;
import java.util.Collection;

public class DegreeService {
    private static DegreeService degreeService = new DegreeService();
    private DegreeDao degreeDao = DegreeDao.getInstance();

    private DegreeService(){}

    public static DegreeService getInstance(){
        return degreeService;
    }
    public Collection<Degree> findAll()throws SQLException {
        return degreeDao.findAll();
    }
    public Degree find(Integer id) throws SQLException{
        return degreeDao.find(id);
    }
    public boolean update(Degree degree) throws SQLException {
        return degreeDao.update(degree);
    }
    public boolean add(Degree degree) throws SQLException {
        return degreeDao.add(degree);
    }
    public boolean delete(Degree degree)throws SQLException{
        return degreeDao.delete(degree);
    }
}
