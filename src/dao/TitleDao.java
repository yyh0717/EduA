package dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.College;
import domain.Department;
import domain.School;
import domain.Title;
import service.CollegeService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class TitleDao {
    private static TitleDao titleDao = new TitleDao();
    private TitleDao(){}
    public static TitleDao getInstance(){
        return titleDao;
    }
    public Collection<Title> findAll() throws SQLException {
        Collection<Title> titles = new TreeSet<Title>();
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from title");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            titles.add(new Title(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description")));
        }
        //执行预编译语句
        JdbcHelper.close(resultSet,stmt,connection);
        return titles;
    }
    public Title find(Integer id) throws SQLException{
        Connection connection =  JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from title where id=?");
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Title title = null;
        while (resultSet.next()){
            title = new Title(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description"));
        }
        JdbcHelper.close(preparedStatement,connection);
        return title;
    }
}
