package service;


import dao.CourseTypeDao;
import domain.CourseType;

import java.sql.SQLException;

public class CourseTypeService {
    private static CourseTypeService courseTypeService = new CourseTypeService();
    private CourseTypeDao courseTypeDao = CourseTypeDao.getInstance();

    private CourseTypeService(){}

    public static CourseTypeService getInstance(){
        return courseTypeService;
    }
    public CourseType find(Integer id) throws SQLException {
        return courseTypeDao.find(id);
    }

}
