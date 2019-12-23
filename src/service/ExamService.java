package service;

import dao.ExamDao;
import domain.Exam;

import java.sql.SQLException;
import java.util.Collection;

public class ExamService {
    private ExamDao examDao= ExamDao.getInstance();
    private static ExamService examService= new ExamService();

    //Singleton
    private ExamService(){}

    public static ExamService getInstance(){
        return examService;
    }

    //获取所有学生考试信息
    public Collection<Exam> findAll()throws SQLException {
        return examDao.findAll();
    }

    //增加一个学生考试信息
    public void add(Exam exam)throws SQLException {
        examDao.add(exam);
    }
    //修改一个学生考试成绩
    public void update(Exam exam)throws SQLException {
        examDao.update(exam);
    }

    //获得id对应的学生考试信息
    public Exam find(Integer id)throws SQLException  {
        return examDao.find(id);
    }
    public Collection<Exam> findByStudent(Integer id) throws SQLException {
        return examDao.findByStudent(id);
    }
    public Collection<Exam> findByTeaching(Integer id) throws SQLException {
        return examDao.findByTeaching(id);
    }
    //删除一个学生考试信息
    public void delete(Integer id) throws SQLException {
        Exam exam = this.find(id);
        examDao.delete(exam);
    }

    public boolean delete(Exam exam)throws SQLException{
        return examDao.delete(exam);
    }
}
