package dao;

import domain.CourseType;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseTypeDao {
    private static CourseTypeDao courseTypeDao = new CourseTypeDao();
    private CourseTypeDao(){
    }
    //getInstance方法
    public static CourseTypeDao getInstance() {
        return courseTypeDao;
    }
    public CourseType find(Integer id) throws SQLException {
        //连接对象
        Connection connection = JdbcHelper.getConn();
        //预编译语句
        String courseType_sql = "SELECT * FROM coursetype where id = ?";
        //预编译对象
        PreparedStatement pstmt = connection.prepareStatement(courseType_sql);
        //赋值
        pstmt.setInt(1,id);
        //执行返回结果集
        ResultSet resultSet = pstmt.executeQuery();
        //指针下移
        resultSet.next();
        //创建对象
        CourseType courseType = new CourseType(
                resultSet.getInt("id"),
                resultSet.getString("no"),
                resultSet.getString("description"),
                resultSet.getString("remarks")
        );
        //关闭资源
        JdbcHelper.close(pstmt,connection);
        //返回对象
        return courseType;
    }
}
