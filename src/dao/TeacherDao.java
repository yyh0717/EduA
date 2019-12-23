package dao;

import domain.Teacher;
import service.DegreeService;
import service.DepartmentService;
import service.TitleService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class TeacherDao {
    private static TeacherDao teacherDao=new TeacherDao();
    private TeacherDao(){}
    public static TeacherDao getInstance(){
        return teacherDao;
    }
    public Collection<Teacher> findAll() throws SQLException {
        TreeSet<Teacher> teachers = new TreeSet<Teacher>();
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from teacher,actor where teacher.id = actor.id");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            teachers.add(new Teacher(resultSet.getInt("id"),
                    resultSet.getString("TeacherNo"),
                    resultSet.getString("name"),
                    resultSet.getString("IDCard"),
                    resultSet.getString("phoneNumber"),
                    DepartmentService.getInstance().find(resultSet.getInt("department_id")),
                    DegreeService.getInstance().find(resultSet.getInt("degree_id")),
                    TitleService.getInstance().find(resultSet.getInt("title_id"))
                    ));
        }
        //执行预编译语句
        JdbcHelper.close(resultSet,stmt,connection);
        return teachers;
    }

    public Teacher find(Integer id) throws SQLException{
        Teacher teacher = null;
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teacher,actor WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks,school值为参数，创建对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            teacher= new Teacher(resultSet.getInt("id"),
                    resultSet.getString("TeacherNo"),
                    resultSet.getString("name"),
                    resultSet.getString("IDCard"),
                    resultSet.getString("phoneNumber"),
                    DepartmentService.getInstance().find(resultSet.getInt("department_id")),
                    DegreeService.getInstance().find(resultSet.getInt("degree_id")),
                    TitleService.getInstance().find(resultSet.getInt("title_id"))
                    );
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return teacher;
    }

    public boolean update(Teacher teacher) throws SQLException,ClassNotFoundException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE actor,teacher SET actor.name=?,actor.IDCard=?,actor.phoneNumber=?," +
                        "teacher.degree_id=?,teacher.title_id=?,teacher.department_id=?,teacher.TeacherNo=? " +
                        "WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setString(1,teacher.getName());
        preparedStatement.setString(2, teacher.getIDCard());
        preparedStatement.setString(3, teacher.getPhoneNumber());
        preparedStatement.setInt(4,teacher.getDegree().getId());
        preparedStatement.setInt(5,teacher.getTitle().getId());
        preparedStatement.setInt(6,teacher.getDepartment().getId());
        preparedStatement.setString(7,teacher.getTeacherNo());
        preparedStatement.setInt(8, teacher.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("修改了"+ affectedRowNum + "条记录");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum > 0;
    }

    public boolean add(Teacher teacher){
        //获取数据库连接对象
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean teacherIfAdd = true;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            //添加预编译语句
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO actor (name,IDCard,phoneNumber) " +
                            "VALUES (?,?,?) ",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, teacher.getName());
            preparedStatement.setString(2, teacher.getIDCard());
            preparedStatement.setString(3, teacher.getPhoneNumber());
            int affectedRowNum = preparedStatement.executeUpdate();
            System.out.println("添加了 " + affectedRowNum + " 行actor记录");
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int teacher_id = resultSet.getInt(1);
            preparedStatement = connection.prepareStatement("INSERT  INTO teacher(id,TeacherNo, degree_id, department_id, title_id) "
                    + "VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1,teacher_id);
            preparedStatement.setString(2,teacher.getTeacherNo());
            preparedStatement.setInt(3, teacher.getDegree().getId());
            preparedStatement.setInt(4, teacher.getDepartment().getId());
            preparedStatement.setInt(5, teacher.getTitle().getId());
            int affectedRowNum1 = preparedStatement.executeUpdate();
            System.out.println("添加了 " + affectedRowNum1 + " 行teacher记录");
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            teacherIfAdd = false;
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
        return teacherIfAdd;
    }


    public boolean delete(Teacher teacher) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        boolean IfDEL = true;
        try {
            //在user表中先删teacherId为当前teacher的id的user记录
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            String deleteTeacher_sql = "DELETE FROM teacher WHERE id = ?";
            pstmt = connection.prepareStatement(deleteTeacher_sql);
            pstmt.setInt(1,teacher.getId());
            int affectedRowNum = pstmt.executeUpdate();
            System.out.println("删除了 " + affectedRowNum +" 行记录");
            //删teacher记录
            String deleteActor_sql = "DELETE FROM actor WHERE id = ?";
            pstmt = connection.prepareStatement(deleteActor_sql);
            pstmt.setInt(1,teacher.getId());
            int affectedRowNum1 = pstmt.executeUpdate();
            System.out.println("删除了 " + affectedRowNum1 +" 行记录");

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            try {
                if (connection != null){
                    connection.rollback();
                }
            } catch (SQLException e1){
                e.printStackTrace();
            }
            IfDEL = false;
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
        return IfDEL;
    }
}
