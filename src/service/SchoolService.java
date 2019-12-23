package service;

import dao.CourseDao;
import dao.SchoolDao;
import domain.School;

import java.sql.SQLException;
import java.util.Collection;

public class SchoolService {
    private SchoolDao schoolDao = SchoolDao.getInstance();
    private static SchoolService schoolService = new SchoolService();
    private SchoolService(){}

    public static SchoolService getInstance(){
        return schoolService;
    }
    public Collection<School> findAll() throws SQLException {
        return schoolDao.findAll();
    }
    public School find(Integer id) throws SQLException{
        return schoolDao.find(id);
    }
}
