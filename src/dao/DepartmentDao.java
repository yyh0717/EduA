package dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.College;
import domain.Department;
import domain.School;
import service.CollegeService;
import service.DepartmentService;
import service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class DepartmentDao {
    private static Collection<Department> departments;

    private static DepartmentDao departmentDao=new DepartmentDao();
    private DepartmentDao(){}

    public static DepartmentDao getInstance(){
        return departmentDao;
    }
    public Collection<Department> findAll()throws SQLException{
        departments = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from department");
        while (resultSet.next()){
            departments.add(new Department(resultSet.getInt("id"), resultSet.getString("no"),resultSet.getString("name"),
                    CollegeService.getInstance().find(resultSet.getInt("college_id"))));
        }
        JdbcHelper.close(statement,connection);
        return departments;
    }
//    public Collection<Department> findAllBySchool(int schoolId) throws SQLException{
//        Collection<Department> departmentsBySchoolId = new HashSet<>();
//        Connection connection = JdbcHelper.getConn();
//        Statement stmt = connection.createStatement();
//        ResultSet resultSet = stmt.executeQuery("SELECT * from department");
//        while (resultSet.next()){
//            if (resultSet.getInt("school_id")==schoolId){
//                departmentsBySchoolId.add(new Department(
//                        resultSet.getInt("id"),
//                        resultSet.getString("description"),
//                        resultSet.getString("no"),
//                        resultSet.getString("remarks"),
//                        CollegeService.getInstance().find(resultSet.getInt("school_id"))));
//            }
//        }
//        return departmentsBySchoolId;
//    }
    public Department find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String findDepartment_sql = "SELECT * FROM department where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(findDepartment_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        Department department = null;
        while (resultSet.next()) {
            department = new Department(resultSet.getInt("id"), resultSet.getString("no"),resultSet.getString("name"),
                    CollegeService.getInstance().find(resultSet.getInt("college_id")));
        }
        JdbcHelper.close(pstmt,connection);
        return department;
    }

    public boolean update(Department department) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String updateDepartment_sql = "UPDATE department SET no = ?,name =? where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        pstmt.setString(1,department.getNo());
        pstmt.setString(2,department.getName());
        pstmt.setInt(3,department.getId());

        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("本次改动了"+affectedRowNum+"行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
    }
    public boolean add(Department department) throws SQLException {
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO department "+ "( no,name,college_id)" +" VALUES (?,?,?)");
        preparedStatement.setString(1,department.getNo());
        preparedStatement.setString(2,department.getName());
        preparedStatement.setInt(3,department.getCollege().getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("本次增加了"+affectedRowNum +"行专业");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
    /**public boolean delete(Integer id){
     Department department = this.find(id);
     return this.delete(department);
     }*/
    public boolean delete(Department department)throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("Delete from department WHERE id =?");
        preparedStatement.setInt(1,department.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("本次删除了"+affectedRowNum+"条专业记录");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
}
