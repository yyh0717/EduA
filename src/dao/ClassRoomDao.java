package dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.ClassRoom;
import domain.Title;
import service.TeachingBuildingService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class ClassRoomDao {
    private  static ClassRoomDao classRoomDao = new ClassRoomDao();
    private ClassRoomDao(){}
    public static ClassRoomDao getInstance(){
        return classRoomDao;
    }
    public Collection<ClassRoom> findAll() throws SQLException{
        Collection<ClassRoom> classRooms = new HashSet<ClassRoom>();
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
        Statement stmt = connection.createStatement();
        //执行SQL查询语句并获得结果集对象
        ResultSet resultSet = stmt.executeQuery("select * from ClassRoom");
        while (resultSet.next()){
            ClassRoom classRoom = new ClassRoom(
                    resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getInt("maximumNumber"),
                    TeachingBuildingService.getInstance().find(resultSet.getInt("teachingbuild_id"))
            );
            classRooms.add(classRoom);
        }
        return classRooms;
    }
    //定义find()方法
    public ClassRoom find(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String classRoom_sql = "SELECT * FROM ClassRoom where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(classRoom_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        ClassRoom classRoom = null;
        if (resultSet.next()) {
            classRoom = new ClassRoom(
                    resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getInt("maximumNumber"),
                    TeachingBuildingService.getInstance().find(resultSet.getInt("teachingbuild_id"))
            );
        }
        JdbcHelper.close(pstmt,connection);
        return classRoom;
    }

}
