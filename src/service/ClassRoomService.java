package service;

import dao.ClassRoomDao;
import dao.CourseDao;
import domain.ClassRoom;

import java.sql.SQLException;
import java.util.Collection;

public class ClassRoomService {
    private static ClassRoomService classRoomService = new ClassRoomService();
    private ClassRoomService(){}
    private ClassRoomDao classRoomDao = ClassRoomDao.getInstance();
    public static ClassRoomService getInstance(){return classRoomService;}
    public ClassRoom find(Integer id) throws SQLException {
        return classRoomDao.find(id);
    }
    public Collection<ClassRoom> findAll() throws SQLException{
        return classRoomDao.findAll();
    }
}
