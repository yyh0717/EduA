package dao;

import domain.Student;
import domain.Teaching;
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
        ResultSet resultSet = stmt.executeQuery("select * from student,actor where student.id=actor.id");
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
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student,actor WHERE student.id=actor.id and student.id=?");
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

    public boolean update(Student student) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE actor,student SET actor.name =?,actor.IDCard=?,actor.phoneNumber=?,student.department_id=? " +
                        "WHERE student.id=actor.id and student.id=? ");
        //为预编译参数赋值
        preparedStatement.setString(1,student.getName());
        preparedStatement.setString(2, student.getIDCard());
        preparedStatement.setString(3, student.getPhoneNumber());
        preparedStatement.setInt(4, student.getDepartment().getId());
        preparedStatement.setInt(5, student.getId());
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
                    "INSERT INTO actor (name,IDCard,phoneNumber) VALUES (?,?,?) "
                    ,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getIDCard());
            preparedStatement.setString(3, student.getPhoneNumber());
            int affectedRowNum = preparedStatement.executeUpdate();
            System.out.println("添加了 " + affectedRowNum + " 行actor记录");
            //通过getGeneratedKeys()获取主键
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            //获得新增记录的id
            int studentId = resultSet.getInt(1);
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO student (id, studentNo, department_id) VALUES (?,?,?) "
                    ,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,studentId);
            preparedStatement.setString(2,student.getStudentNo());
            preparedStatement.setInt(3,student.getDepartment().getId());
            int affectedRowNum1 = preparedStatement.executeUpdate();
            System.out.println("添加了 " + affectedRowNum + " 行student记录");
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
    public boolean delete(Student student){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean studentifDEL = true;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("DELETE FROM student where id=?");
            preparedStatement.setInt(1,student.getId());
            int mount = preparedStatement.executeUpdate();
            System.out.println("本次更新了"+ mount +"行");
            preparedStatement = connection.prepareStatement("DELETE FROM actor where id = ?");
            preparedStatement.setInt(1,student.getId());
            int mount1 = preparedStatement.executeUpdate();
            System.out.println("本次更新了"+ mount1 +"行");
            connection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e.printStackTrace();
            }
            studentifDEL =false;
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
        return studentifDEL;
    }
}
