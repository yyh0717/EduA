package dao;

import domain.Periods;
import util.JdbcHelper;
import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class PeriodsDao {
    //静态私有本类对象
    private static PeriodsDao periodsDao = new PeriodsDao();
    private PeriodsDao() {
    }
    public static PeriodsDao getInstance(){
        return periodsDao;
    }
    public Collection<Periods> findAll()throws SQLException {
        Collection<Periods> periodses = new TreeSet<Periods>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from periods");
        //从数据库中取出数据
        while (resultSet.next()){
            periodses.add(new Periods(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getInt("startPoint"),
                    resultSet.getInt("endPoint")));
        }
        JdbcHelper.close(stmt,connection);
        return periodses;
    }
    public Periods find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String findPeriods_sql = "SELECT * FROM periods where id = ?";
        //创建预编译语句盒子
        PreparedStatement pstmt = connection.prepareStatement(findPeriods_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        Periods periods= null;
        //游标下移一行，返回下一行是否有有效记录
        while (resultSet.next()){
            periods= new Periods(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getInt("startPoint"),
                    resultSet.getInt("endPoint"));
        }
        JdbcHelper.close(pstmt,connection);
        return periods;
    }
    public boolean update(Periods periods) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String updatePeriods_sql = "UPDATE periods SET no =? ,startPoint=?,endPoint=? where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updatePeriods_sql);
        pstmt.setString(1,periods.getNo());
        pstmt.setInt(2,periods.getStartPoint());
        pstmt.setInt(3,periods.getEndPoint());
        pstmt.setInt(4,periods.getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("本次更新了"+affectedRowNum+"行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
    }
    public boolean add(Periods periods) throws SQLException {
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO periods "+ "(no,startPoint,endPoint)" +" VALUES (?,?,?)");
        preparedStatement.setString(1,periods.getNo());
        preparedStatement.setInt(2,periods.getStartPoint());
        preparedStatement.setInt(3,periods.getEndPoint());
        int affectedRowNum = preparedStatement.executeUpdate();
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
    public boolean delete(Periods periods)throws SQLException{
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("Delete from periods WHERE id =?");
        preparedStatement.setInt(1,periods.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
}
