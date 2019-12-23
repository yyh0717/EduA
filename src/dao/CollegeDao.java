package dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.College;
import domain.School;
import service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class CollegeDao {
    private static CollegeDao collegeDao = new CollegeDao();
    private static Collection<College> colleges;
    public CollegeDao(){}

    public static CollegeDao getInstance(){
        return collegeDao;
    }

    public Collection<College> findAll() throws SQLException {
        colleges = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from college");
        //从数据库中取出数据
        while (resultSet.next()){
            colleges.add(new College(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description"),
                    SchoolService.getInstance().find(resultSet.getInt("school_id"))));
        }
        JdbcHelper.close(stmt,connection);
        return colleges;
    }

    public College find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String findCollege_sql = "SELECT * FROM college where id = ?";
        //创建预编译语句盒子
        PreparedStatement pstmt = connection.prepareStatement(findCollege_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        College college= null;
        //游标下移一行，返回下一行是否有有效记录
        while (resultSet.next()){
        college= new College(resultSet.getInt("id"),
                resultSet.getString("no"),
                resultSet.getString("description"),
                SchoolService.getInstance().find(resultSet.getInt("school_id")));}
        JdbcHelper.close(pstmt,connection);
        return college;
    }

    public boolean update(College college) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String updateCollege_sql = "UPDATE college SET description =?,no =? where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateCollege_sql);
        pstmt.setString(1,college.getDescription());
        pstmt.setString(2,college.getNo());
        pstmt.setInt(3,college.getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("本次更新了"+affectedRowNum+"行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
    }

    public boolean add(College college) throws SQLException {
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO college "+ "(no,description,school_id)" +" VALUES (?,?,?)");
        preparedStatement.setString(1,college.getNo());
        preparedStatement.setString(2,college.getDescription());
        preparedStatement.setInt(3,college.getSchool().getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }

    public boolean delete(College college) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("Delete from college WHERE id =?");
        preparedStatement.setInt(1,college.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println(affectedRowNum);
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }


    //存储过程
    public College addWithSP(College college) throws SQLException, ClassNotFoundException {
        //加载驱动程序
        Connection connection = JdbcHelper.getConn();
        //根据连接对象准备可调用语句对象，sp_addSchool为存贮过程名称，后面为4个参数
        CallableStatement callableStatement
                = connection.prepareCall("{CALL sp_addCollege(?,?,?,?)}");
        //将第4个参数设置为输出参数，类型为长整型（数据库的数据类型）
        callableStatement.registerOutParameter(4, Types.BIGINT);
        callableStatement.setString(1,college.getDescription());
        callableStatement.setString(2,college.getNo());
        callableStatement.setInt(3,college.getSchool().getId());
        //执行可调用语句callableStatement
        callableStatement.execute();
        //获得第4个参数的值：数据库为该记录自动生成的id
        int id = callableStatement.getInt(4);
        //为参数school的id字段赋值
        college.setId(id);
        JdbcHelper.close(callableStatement,connection);
        return college;
    }

}
