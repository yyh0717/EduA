package service;



import dao.TitleDao;
import domain.Title;

import java.sql.SQLException;
import java.util.Collection;

public class TitleService {
    private static TitleService titleService = new TitleService();
    private TitleDao titleDao = TitleDao.getInstance();

    private TitleService(){}

    public static TitleService getInstance(){
        return titleService;
    }
    public Collection<Title> findAll() throws SQLException {
        return titleDao.findAll();
    }
    public Title find(Integer id) throws SQLException{
        return titleDao.find(id);
    }
}
