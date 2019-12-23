package dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class SchoolDao {
    private static SchoolDao schoolDao=new SchoolDao();
    private SchoolDao(){}
    public static SchoolDao getInstance(){
        return schoolDao;
    }
    public Collection<School> findAll() throws SQLException {
        Collection<School> schools = new TreeSet<School>();
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from school");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            schools.add(new School(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description")));
        }
        //执行预编译语句
        JdbcHelper.close(resultSet,stmt,connection);
        return schools;
    }
    public School find(Integer id) throws SQLException{
        Connection connection =  JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from school where id=?");
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        School school = null;
        while (resultSet.next()){
            school = new School(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description"));
        }
        JdbcHelper.close(preparedStatement,connection);
        return school;
    }


}
