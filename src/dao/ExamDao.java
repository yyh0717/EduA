package dao;

import domain.Exam;
import domain.Student;
import domain.Teaching;
import service.StudentService;
import service.TeachingService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class ExamDao {
    private static ExamDao examDao = new ExamDao();
    private ExamDao() {}
    public static ExamDao getInstance() {
        return examDao;
    }
    public Collection<Exam> findAll() throws SQLException {
        Collection<Exam> exams = new TreeSet<Exam>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("SELECT * FROM exam");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            exams.add(new Exam(resultSet.getInt("id"),resultSet.getDouble("score"),
                    TeachingService.getInstance().find(resultSet.getInt("teaching_id")),
                    StudentService.getInstance().find(resultSet.getInt("student_id"))));
        }
        //关闭资源
        JdbcHelper.close(statement,connection);
        return exams;
    }
    public Collection<Exam> findByStudent(Integer id) throws SQLException {
        Collection<Exam> examsByStudent = new TreeSet<Exam>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from exam where student_id=?");
        preparedStatement.setInt(1,id);
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            examsByStudent.add(new Exam(resultSet.getInt("id"),resultSet.getDouble("score"),
                    TeachingService.getInstance().find(resultSet.getInt("teaching_id")),
                    StudentService.getInstance().find(resultSet.getInt("student_id"))));
        }
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return examsByStudent;
    }
    public Collection<Exam> findByTeaching(Integer id) throws SQLException {
        Collection<Exam> examsByTeaching = new TreeSet<Exam>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from exam where teaching_id=?");
        preparedStatement.setInt(1,id);
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            examsByTeaching.add(new Exam(resultSet.getInt("id"),resultSet.getDouble("score"),
                    TeachingService.getInstance().find(resultSet.getInt("teaching_id")),
                    StudentService.getInstance().find(resultSet.getInt("student_id"))));
        }
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return examsByTeaching;
    }
    public Exam find(Integer id) throws SQLException {
        Exam exam = null;
        Connection connection = JdbcHelper.getConn();
        String selectProject_sql = "SELECT * FROM exam WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(selectProject_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            exam = new Exam(resultSet.getInt("id"),resultSet.getDouble("score"),
                            TeachingService.getInstance().find(resultSet.getInt("teaching_id")),
                            StudentService.getInstance().find(resultSet.getInt("student_id")));;
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return exam;
    }

    public boolean update(Exam exam) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateProject_sql = "UPDATE exam SET score=?,student_id=?,teaching_id=? WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateProject_sql);
        //为预编译参数赋值
        preparedStatement.setDouble(1,exam.getScore());
        preparedStatement.setInt(2,exam.getStudent().getId());
        preparedStatement.setInt(3,exam.getTeaching().getId());
        preparedStatement.setInt(4,exam.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
    public boolean add(Exam exam) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String addProject_sql = "INSERT INTO exam(score,student_id,teaching_id) VALUES" + " (?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(addProject_sql);
        //为预编译参数赋值
        preparedStatement.setDouble(1, exam.getScore());
        preparedStatement.setInt(2, exam.getStudent().getId());
        preparedStatement.setInt(3, exam.getTeaching().getId());
        //执行预编译语句，获取添加记录行数并赋值给affectedRowNum
        int affectedRowNum = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum > 0;
    }
    public boolean delete(Exam exam) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM exam WHERE id = ?");
        //为预编译的语句参数赋值
        pstmt.setInt(1,exam.getId());
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 条成绩");
        //关闭资源
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
}
