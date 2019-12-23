package dao;

import domain.Student;
import service.DepartmentService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class StudentDao {
    private static StudentDao studentDao=new StudentDao();
    private StudentDao(){}
    public static StudentDao getInstance(){
        return studentDao;
    }
    public Collection<Student> findAll() throws SQLException {
        Collection<Student> students = new HashSet<Student>();
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from student");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            students.add(new Student(resultSet.getInt("id"),
                    resultSet.getString("StudentNo"),
                    resultSet.getString("name"),
                    resultSet.getString("IDCard"),
                    resultSet.getString("phoneNumber"),
                    DepartmentService.getInstance().find(resultSet.getInt("department_id"))));
        }
        //执行预编译语句
        JdbcHelper.close(resultSet,stmt,connection);
        return students;
    }

    public Student find(Integer id) throws SQLException{
        Student student = null;
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            student = new Student(resultSet.getInt("id"),
                    resultSet.getString("StudentNo"),
                    resultSet.getString("name"),
                    resultSet.getString("IDCard"),
                    resultSet.getString("phoneNumber"),
                    DepartmentService.getInstance().find(resultSet.getInt("department_id")));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return student;
    }

    public boolean update(Student student) throws SQLException,ClassNotFoundException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE student SET name=?,IDCard=?,phoneNumber=?,department_id=? WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setString(1,student.getName());
        preparedStatement.setString(2, student.getIDCard());
        preparedStatement.setString(3, student.getPhoneNumber());
        preparedStatement.setInt(4, student.getDepartment().getId());
        preparedStatement.setInt(5,student.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("修改了"+ affectedRowNum + "条学生记录");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum > 0;
    }
    public boolean add(Student student) {
        //获取数据库连接对象
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean student1 = true;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            //添加预编译语句
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO student (name,StudentNo,IDCard,phoneNumber,Department_id) VALUES (?,?,?,?,?) "
                    ,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getStudentNo());
            preparedStatement.setString(3, student.getIDCard());
            preparedStatement.setString(4, student.getPhoneNumber());
            preparedStatement.setInt(5, student.getDepartment().getId());
            int affectedRowNum = preparedStatement.executeUpdate();
            System.out.println("添加了 " + affectedRowNum + " 行学生记录");
            //通过getGeneratedKeys()获取主键
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            //获得新增记录的id
            int studentId = resultSet.getInt(1);
            connection.commit();
            student1 = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            student1 = false;
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    //恢复自动提交
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JdbcHelper.close(preparedStatement, connection);
        }
        return student1;
    }

    public void delete(Student student) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            //在user表中先删studentId为当前student的id的user记录
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            String deleteUser_sql = "DELETE FROM user WHERE student_id = ?";
            pstmt = connection.prepareStatement(deleteUser_sql);
            pstmt.setInt(1,student.getId());
            int affectedRowNum = pstmt.executeUpdate();
            System.out.println("删除了 " + affectedRowNum +" 行记录");
            //删student记录
            String deleteStudent_sql = "DELETE FROM student WHERE id = ?";
            pstmt = connection.prepareStatement(deleteStudent_sql);
            pstmt.setInt(1,student.getId());
            int affectedRowNum1 = pstmt.executeUpdate();
            System.out.println("删除了 " + affectedRowNum1 +" 行记录");
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            try {
                if (connection != null){
                    connection.rollback();
                }
            } catch (SQLException e1){
                e.printStackTrace();
            }
        } finally {
            try {
                if (connection != null){
                    //恢复自动提交
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(pstmt,connection);
        }
    }
}
