package dao;

import domain.Term;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class TermDao {
    //静态私有本类对象
    private static TermDao termDao = new TermDao();
    private TermDao() {
    }
    public static TermDao getInstance(){
        return termDao;
    }
    public Collection<Term> findAll()throws SQLException {
        Collection<Term> terms = new TreeSet<Term>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from term");
        //从数据库中取出数据
        while (resultSet.next()){
            terms.add(new Term(resultSet.getInt("id"),
                    resultSet.getString("no"),resultSet.getString("description")));
        }
        JdbcHelper.close(stmt,connection);
        return terms;
    }
    public Term find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String findTerm_sql = "SELECT * FROM term where id = ?";
        //创建预编译语句盒子
        PreparedStatement pstmt = connection.prepareStatement(findTerm_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        Term term= null;
        //游标下移一行，返回下一行是否有有效记录
        while (resultSet.next()){
            term= new Term(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description"));
        }
        JdbcHelper.close(pstmt,connection);
        return term;
    }
    public boolean update(Term term) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String updateTerm_sql = "UPDATE term SET description =?,no =? where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateTerm_sql);
        pstmt.setString(1,term.getDescription());
        pstmt.setString(2,term.getNo());
        pstmt.setInt(3,term.getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("本次更新了"+affectedRowNum+"行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
    }
    public boolean add(Term term) throws SQLException {
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO term "+ "(no,description)" +" VALUES (?,?)");
        preparedStatement.setString(1,term.getNo());
        preparedStatement.setString(2,term.getDescription());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println(affectedRowNum);
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
    public boolean delete(Term term)throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("Delete from term WHERE id =?");
        preparedStatement.setInt(1,term.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("删除了"+ affectedRowNum+"行");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }
}
