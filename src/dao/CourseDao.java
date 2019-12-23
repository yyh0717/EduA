package dao;

import domain.Course;
import service.CollegeService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CourseDao {
    //静态私有本类对象
    private static CourseDao courseDao = new CourseDao();
    private CourseDao() {
    }
    public static CourseDao getInstance(){
        return courseDao;
    }
    //返回所有课程的方法
    public Collection<Course> findAll() throws SQLException{
        Collection<Course> courses = new HashSet<Course>();
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建空的语句盒子
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from course");
        while (resultSet.next()){
            Course course = new Course(
                    resultSet.getInt("id"),
                    resultSet.getString("courseNo"),
                    resultSet.getString("name"),
                    resultSet.getString("remarks"),
                    resultSet.getInt("credit"),
                    resultSet.getInt("classHour"),
                    resultSet.getInt("examWeek"),
                    CourseTypeDao.getInstance().find(resultSet.getInt("courseType_id")),
                    CollegeService.getInstance().find(resultSet.getInt("college_id"))
            );
            courses.add(course);
        }
        statement.close();
        connection.close();
        return courses;
    }
    //模糊查询
    public Collection<Course> findByFuzzy(String name) throws SQLException {
        Collection<Course> courses = new HashSet<Course>();
        Connection connection = JdbcHelper.getConn();
        String course_sql = "SELECT * from course where name LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(course_sql);
        preparedStatement.setString(1,"%"+name+"%");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            Course course = new Course(
                    resultSet.getInt("id"),
                    resultSet.getString("courseNo"),
                    resultSet.getString("name"),
                    resultSet.getString("remarks"),
                    resultSet.getInt("credit"),
                    resultSet.getInt("classHour"),
                    resultSet.getInt("examWeek"),
                    CourseTypeDao.getInstance().find(resultSet.getInt("courseType_id")),
                    CollegeService.getInstance().find(resultSet.getInt("college_id"))
            );
            courses.add(course);
        }
        JdbcHelper.close(preparedStatement,connection);
        return courses;
    }
    //根据id查找响应课程
    public Course find(Integer id) throws SQLException {
        //连接对象
        Connection connection = JdbcHelper.getConn();
        //预编译语句
        String course_sql = "SELECT * FROM course where id = ?";
        //预编译对象
        PreparedStatement pstmt = connection.prepareStatement(course_sql);
        //赋值
        pstmt.setInt(1,id);
        //执行返回结果集
        ResultSet resultSet = pstmt.executeQuery();
        Course course = null;
        //指针下移
        while (resultSet.next()) {
            //创建对象
            course = new Course(
                    resultSet.getInt("id"),
                    resultSet.getString("courseNo"),
                    resultSet.getString("name"),
                    resultSet.getString("remarks"),
                    resultSet.getInt("credit"),
                    resultSet.getInt("classHour"),
                    resultSet.getInt("examWeek"),
                    CourseTypeDao.getInstance().find(resultSet.getInt("courseType_id")),
                    CollegeService.getInstance().find(resultSet.getInt("college_id"))
            );
        }
        //关闭资源
        JdbcHelper.close(pstmt,connection);
        //返回对象
        return course;
    }
    public void update(Course course) throws SQLException {
        //连接对象
        Connection connection = JdbcHelper.getConn();
        //预编译语句
        String courseUpdate_Sql = "UPDATE course SET courseNo=?," +
                "name = ?," +
                "remarks = ?," +
                "credit = ?," +
                "classHour = ?," +
                "examWeek = ?," +
                "courseType_id = ?,college_id = ?" +
                " where id = ?";
        //预编译对象
        PreparedStatement pstmt = connection.prepareStatement(courseUpdate_Sql);
        //赋值
        pstmt.setString(1,course.getCourseNo());
        pstmt.setString(2,course.getName());
        pstmt.setString(3,course.getRemarks());
        pstmt.setInt(4,course.getCredit());
        pstmt.setInt(5,course.getClassHour());
        pstmt.setInt(6,course.getExamWeek());
        pstmt.setInt(7,course.getCourseType().getId());
        pstmt.setInt(8,course.getCollege().getId());
        pstmt.setInt(9,course.getId());
        //执行
        pstmt.executeUpdate();
        //关闭资源
        JdbcHelper.close(pstmt,connection);
    }
    //删除方法根据id
    public void delete(Integer id) throws SQLException {
        Course course = this.find(id);
        this.delete(course);
    }
    //根据对象的删除方法用事物
    public void delete(Course course) throws SQLException {
        //声明连接和预编译对象
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            //给连接对象赋值
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            //sql语句
            String deleteCourse_sql = "DELETE FROM course WHERE id = ?";
            //给预编译赋值
            pstmt = connection.prepareStatement(deleteCourse_sql);
            //赋值
            pstmt.setInt(1, course.getId());
            //执行返回影响行数
            int affectedRowNum = pstmt.executeUpdate();
            System.out.println("删除了 " + affectedRowNum + " 行记录");
        } catch (SQLException e) {
            //抓取错误
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e.printStackTrace();
            } finally {
                //恢复自动提交
                if (connection != null) {
                    //恢复自动提交
                    connection.setAutoCommit(true);
                }
                //关闭资源
                JdbcHelper.close(pstmt, connection);
            }
        }
    }
    public void add(Course course){
        //声明连接和预编译对象
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            //给连接对象赋值
            connection = JdbcHelper.getConn();
            //关闭自动提交
            connection.setAutoCommit(false);
            //预编译语句
            String addCourse_sql = "INSERT INTO course(courseNo,name,remarks,credit,classHour,examWeek,courseType_id,college_id) VALUES" +
                    " (?,?,?,?,?,?,?,?)";
            //赋值
            pstmt = connection.prepareStatement(addCourse_sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,course.getCourseNo());
            pstmt.setString(2,course.getName());
            pstmt.setString(3,course.getRemarks());
            pstmt.setInt(4,course.getCredit());
            pstmt.setInt(5,course.getClassHour());
            pstmt.setInt(6,course.getExamWeek());
            pstmt.setInt(7,course.getCourseType().getId());
            pstmt.setInt(8,course.getCollege().getId());
            //执行返回影响行数
            int affectedRowNum = pstmt.executeUpdate();
            System.out.println("添加了 " + affectedRowNum +" 行记录");
        } catch (SQLException e) {
            //抓取错误
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            try {
                if (connection != null){
                    connection.rollback();
                }
            } catch (SQLException e1){
                e.printStackTrace();
            }

        } finally {
            //回复自动提交
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
