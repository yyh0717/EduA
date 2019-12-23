package service;

import dao.StudentDao;
import domain.Student;

import java.sql.SQLException;
import java.util.Collection;

public class StudentService {
    private static StudentDao studentDao= StudentDao.getInstance();
    private static StudentService
            studentService=new StudentService();
    private StudentService(){}
    public static StudentService getInstance(){
        return studentService;
    }
    public Collection<Student> findAll()throws SQLException {
        return studentDao.findAll();
    }
    public Student find(Integer id)throws SQLException {
        return studentDao.find(id);
    }
    public boolean update(Student student)
            throws SQLException,ClassNotFoundException {
        return studentDao.update(student);
    }
    public boolean add(Student student){
        return studentDao.add(student);
    }
    public void delete(Integer id) throws SQLException {
        Student student = this.find(id);
        studentDao.delete(student);
    }
    public void delete(Student student) throws SQLException {
        studentDao.delete(student);
    }
}
