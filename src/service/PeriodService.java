package service;

import dao.PeriodsDao;
import domain.Periods;

import java.sql.SQLException;
import java.util.Collection;

public class PeriodService {
    private static PeriodService periodService = new PeriodService();
    private PeriodService() {
    }
    private static PeriodsDao periodsDao = PeriodsDao.getInstance();
    public static PeriodService getInstance(){
        return periodService;
    }
    public boolean delete(Periods periods)throws SQLException {
        return periodsDao.delete(periods);
    }
    public boolean add(Periods periods) throws SQLException {
        return periodsDao.add(periods);
    }
    public boolean update(Periods periods) throws SQLException {
        return periodsDao.update(periods);
    }
    public Periods find(Integer id) throws SQLException{
        return periodsDao.find(id);
    }
    public Collection<Periods> findAll()throws SQLException {
        return periodsDao.findAll();
    }
}
