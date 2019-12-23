package dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.College;
import domain.Degree;
import domain.Department;
import domain.Title;
import service.CollegeService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class DegreeDao {
    //静态私有本类对象
    private static DegreeDao degreeDao = new DegreeDao();
    private DegreeDao() {
    }
    public static DegreeDao getInstance(){
        return degreeDao;
    }
    public Collection<Degree> findAll()throws SQLException{
        Collection<Degree> degrees = new TreeSet<Degree>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from degree");
        //从数据库中取出数据
        while (resultSet.next()){
            degrees.add(new Degree(resultSet.getInt("id"),
                    resultSet.getString("no"),resultSet.getString("description")));
        }
        JdbcHelper.close(stmt,connection);
        return degrees;
    }
    public Degree find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String findDegree_sql = "SELECT * FROM degree where id = ?";
        //创建预编译语句盒子
        PreparedStatement pstmt = connection.prepareStatement(findDegree_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        Degree degree= null;
        //游标下移一行，返回下一行是否有有效记录
        while (resultSet.next()){
        degree= new Degree(resultSet.getInt("id"),resultSet.getString("no"),resultSet.getString("description"));
        }
        JdbcHelper.close(pstmt,connection);
        return degree;
    }
    public boolean update(Degree degree) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String updateDegree_sql = "UPDATE degree SET description =?,no =? where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
        pstmt.setString(1,degree.getDescription());
        pstmt.setString(2,degree.getNo());
        pstmt.setInt(3,degree.getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("本次更新了"+affectedRowNum+"行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
    }
    public boolean add(Degree degree) throws SQLException {
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO degree "+ "(no,description)" +" VALUES (?,?)");
        preparedStatement.setString(1,degree.getNo());
        preparedStatement.setString(2,degree.getDescription());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println(affectedRowNum);
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
    public boolean delete(Degree degree)throws SQLException{
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("Delete from degree WHERE id =?");
        preparedStatement.setInt(1,degree.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("删除了"+ affectedRowNum+"行");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
}
