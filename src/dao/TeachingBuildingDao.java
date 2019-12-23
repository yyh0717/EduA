package dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.TeachingBuilding;
import domain.Title;
import service.TeachingBuildingService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class TeachingBuildingDao {
    private static TeachingBuildingDao teachingBuildingDao = new TeachingBuildingDao();
    public static TeachingBuildingDao getInstance(){
        return teachingBuildingDao;
    }
    private TeachingBuildingDao(){}
    public Collection<TeachingBuilding> findAll() throws SQLException {
        Connection connection = JdbcHelper.getConn();
        Collection<TeachingBuilding> teachingBuildings = new HashSet<TeachingBuilding>();
        //在该连接上创建语句盒子对象
        Statement stmt = connection.createStatement();
        //执行SQL查询语句并获得结果集对象
        ResultSet resultSet = stmt.executeQuery("select * from teachingbuilding");
        while (resultSet.next()){
            TeachingBuilding teachingBuilding = new TeachingBuilding(
                    resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description"));
            teachingBuildings.add(teachingBuilding);
        }
        JdbcHelper.close(stmt,connection);
        return teachingBuildings;
    }
    public TeachingBuilding find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String teachingBuilding_sql = "SELECT * FROM teachingbuilding where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(teachingBuilding_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        TeachingBuilding teachingBuilding = null;
        if (resultSet.next()) {
            teachingBuilding = new TeachingBuilding(
                    resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description")
            );
        }
        JdbcHelper.close(pstmt,connection);
        return teachingBuilding;
    }
}
