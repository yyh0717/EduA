package service;

import dao.CourseDao;

import domain.Course;

import java.sql.SQLException;
import java.util.Collection;

public class CourseService {
    private static CourseService courseService = new CourseService();
    private CourseDao courseDao = CourseDao.getInstance();

    private CourseService(){}

    public static CourseService getInstance(){
        return courseService;
    }

    public Collection<Course> findAll() throws SQLException {
        return courseDao.findAll();
    }

    public Course find(Integer id) throws SQLException {
        return courseDao.find(id);
    }

    public void update(Course course) throws SQLException {
        courseDao.update(course);
    }

    public void add(Course course) throws SQLException {
        courseDao.add(course);
    }

    public void delete(Integer id) throws SQLException {
        Course course = this.find(id);
        this.delete(course);
    }

    public void delete(Course course) throws SQLException {
        courseDao.delete(course);
    }
    public Collection<Course> findByFuzzy(String name) throws SQLException {
        return courseDao.findByFuzzy(name);
    }
}
