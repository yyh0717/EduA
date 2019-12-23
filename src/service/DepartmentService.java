package service;

import dao.DepartmentDao;
import domain.Department;

import java.sql.SQLException;
import java.util.Collection;

public class DepartmentService {
    private static DepartmentDao departmentDao= DepartmentDao.getInstance();
    private static DepartmentService departmentService=new DepartmentService();
    private DepartmentService(){}

    public static DepartmentService getInstance(){
        return departmentService;
    }
    public Collection<Department> findAll()throws SQLException {
        return departmentDao.findAll();
    }
    public Department find(Integer id) throws SQLException{
        return departmentDao.find(id);
    }
    public boolean update(Department department) throws SQLException{
        return departmentDao.update(department);
    }
    public boolean add(Department department) throws SQLException {
        return departmentDao.add(department);
    }
    public boolean delete(Department department)throws SQLException {
        return departmentDao.delete(department);
    }
}
