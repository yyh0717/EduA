package dao;

import domain.Teaching;
import service.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class TeachingDao {
    //静态私有本类对象
    private static TeachingDao teachingDao = new TeachingDao();
    private TeachingDao() {
    }
    public static TeachingDao getInstance(){
        return teachingDao;
    }
    //返回所有课程的方法
    public Collection<Teaching> findAll() throws SQLException{
        Collection<Teaching> teachings = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建空的语句盒子
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from teaching");
        while (resultSet.next()){
            Teaching teaching = new Teaching(
                    resultSet.getInt("id"),
                    resultSet.getInt("minimumNumber"),
                    resultSet.getInt("startWeek"),
                    resultSet.getInt("endWeek"),
                    CourseService.getInstance().find(resultSet.getInt("course_id")),
                    TeacherService.getInstance().find(resultSet.getInt("teacher_id")),
                    TermService.getInstance().find(resultSet.getInt("term_id")),
                    ClassRoomService.getInstance().find(resultSet.getInt("classRoom_id")),
                    PeriodService.getInstance().find(resultSet.getInt("periods_id"))
            );
            teachings.add(teaching);
        }
        JdbcHelper.close(statement,connection);
        return teachings;
    }
    //        //模糊查询
//        public Collection<Teaching> findByFuzzy(String name) throws SQLException {
//            Collection<Teaching> teachings = new HashSet<Teaching>();
//            Connection connection = JdbcHelper.getConn();
//            String teaching_sql = "SELECT * from teaching where name LIKE ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(teaching_sql);
//            preparedStatement.setString(1,"%"+name+"%");
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()){
//                Teaching teaching = new Teaching(
//                        resultSet.getInt("id"),
//                        resultSet.getInt("minimumNumber"),
//                        resultSet.getInt("startWeek"),
//                        resultSet.getInt("endWeek"),
//                        CourseDao.getInstance().find(resultSet.getInt("course_id")),
//                        TeacherDao.getInstance().find(resultSet.getInt("teacher_id")),
//                        TermDao.getInstance().find(resultSet.getInt("term_id")),
//                        ClassRoomDao.getInstance().find(resultSet.getInt("classRoom_id")),
//                        PeriodsDao.getInstance().find(resultSet.getInt("periods_id"))
//                );
//                teachings.add(teaching);
//            }
//            JdbcHelper.close(preparedStatement,connection);
//            return teachings;
//        }
    //根据id查找响应课程
    public Teaching find(Integer id) throws SQLException {
        //连接对象
        Connection connection = JdbcHelper.getConn();
        //预编译语句
        String teaching_sql = "SELECT * FROM teaching where id = ?";
        //预编译对象
        PreparedStatement pstmt = connection.prepareStatement(teaching_sql);
        //赋值
        pstmt.setInt(1,id);
        //执行返回结果集
        ResultSet resultSet = pstmt.executeQuery();
        Teaching teaching = null;
        //指针下移
        while (resultSet.next()) {
            //创建对象
            teaching = new Teaching(
                    resultSet.getInt("id"),
                    resultSet.getInt("minimumNumber"),
                    resultSet.getInt("startWeek"),
                    resultSet.getInt("endWeek"),
                    CourseService.getInstance().find(resultSet.getInt("course_id")),
                    TeacherService.getInstance().find(resultSet.getInt("teacher_id")),
                    TermService.getInstance().find(resultSet.getInt("term_id")),
                    ClassRoomService.getInstance().find(resultSet.getInt("classRoom_id")),
                    PeriodService.getInstance().find(resultSet.getInt("periods_id"))
            );
        }
        //关闭资源
        JdbcHelper.close(pstmt,connection);
        //返回对象
        return teaching;
    }
    public void update(Teaching teaching) throws SQLException {
        //连接对象
        Connection connection = JdbcHelper.getConn();
        //预编译语句
        String teachingUpdate_Sql = "UPDATE teaching SET minimumNumber=?,startWeek=?,endWeek=?,teacher_id=?,Course_id=?,Term_id=?,ClassRoom_id=?,Periods_id=?"+
                " where id = ?";
        //预编译对象
        PreparedStatement pstmt = connection.prepareStatement(teachingUpdate_Sql);
        //赋值
        pstmt.setInt(1,teaching.getMinimumNumber());
        pstmt.setInt(2,teaching.getStartWeek());
        pstmt.setInt(3,teaching.getEndWeek());
        pstmt.setInt(4,teaching.getTeacher().getId());
        pstmt.setInt(5,teaching.getCourse().getId());
        pstmt.setInt(6,teaching.getTerm().getId());
        pstmt.setInt(7,teaching.getClassRoom().getId());
        pstmt.setInt(8,teaching.getPeriods().getId());
        pstmt.setInt(9,teaching.getId());
        //执行
        pstmt.executeUpdate();
        //关闭资源
        JdbcHelper.close(pstmt,connection);
    }
    //删除方法根据id
    public void delete(Integer id) throws SQLException {
        Teaching teaching = this.find(id);
        this.delete(teaching);
    }
    //根据对象的删除方法用事物
    public void delete(Teaching teaching) throws SQLException{
        //声明连接和预编译对象
        Connection connection = JdbcHelper.getConn();
        //sql语句
        String deleteTeaching_sql = "DELETE FROM teaching WHERE id = ?";
        //创建预编译语句
        PreparedStatement pstmt = connection.prepareStatement(deleteTeaching_sql);
        //赋值
        pstmt.setInt(1,teaching.getId());
        //执行返回影响行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 " + affectedRowNum +" 行记录");
        JdbcHelper.close(pstmt,connection);
    }
    public void add(Teaching teaching) throws SQLException {
        Connection connection = JdbcHelper.getConn();

        String addTeaching_sql = "INSERT INTO teaching(minimumNumber,startWeek,endWeek,course_id,teacher_id,term_id,classRoom_id,periods_id) VALUES" +
                " (?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(addTeaching_sql,Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1,teaching.getMinimumNumber());
        pstmt.setInt(2,teaching.getStartWeek());
        pstmt.setInt(3,teaching.getEndWeek());
        pstmt.setInt(4,teaching.getCourse().getId());
        pstmt.setInt(5,teaching.getTeacher().getId());
        pstmt.setInt(6,teaching.getTerm().getId());
        pstmt.setInt(7,teaching.getClassRoom().getId());
        pstmt.setInt(8,teaching.getPeriods().getId());
        //执行返回影响行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("添加了 " + affectedRowNum +" 行开课记录");
    }
}
