package service;

import dao.TermDao;
import domain.Term;

import java.sql.SQLException;
import java.util.Collection;

public class TermService {
    private static TermService termService = new TermService();
    private TermDao termDao = TermDao.getInstance();

    private TermService(){}

    public static TermService getInstance(){
        return termService;
    }
    public Collection<Term> findAll()throws SQLException {
        return termDao.findAll();
    }
    public Term find(Integer id) throws SQLException{
        return termDao.find(id);
    }
    public boolean update(Term term) throws SQLException {
        return termDao.update(term);
    }
    public boolean add(Term term) throws SQLException {
        return termDao.add(term);
    }
    public boolean delete(Term term)throws SQLException{
        return termDao.delete(term);
    }
}
