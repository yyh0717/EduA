package service;

import dao.CourseDao;
import dao.TeacherDao;
import domain.Teacher;

import java.sql.SQLException;
import java.util.Collection;

public class TeacherService {
    private static TeacherService teacherService = new TeacherService();
    private TeacherDao teacherDao = TeacherDao.getInstance();

    private TeacherService(){}

    public static TeacherService getInstance(){
        return teacherService;
    }
    public Collection<Teacher> findAll() throws SQLException {
        return teacherDao.findAll();
    }
    public Teacher find(Integer id) throws SQLException{
        return teacherDao.find(id);
    }
    public boolean update(Teacher teacher) throws SQLException,ClassNotFoundException{
        return teacherDao.update(teacher);
    }
    public void add(Teacher teacher) throws SQLException {
        teacherDao.add(teacher);
    }
    public void delete(Teacher teacher) throws SQLException {
        teacherDao.delete(teacher);
    }
}
