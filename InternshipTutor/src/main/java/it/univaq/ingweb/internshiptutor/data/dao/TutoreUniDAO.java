package it.univaq.ingweb.internshiptutor.data.dao;


import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface TutoreUniDAO {
    
    TutoreUni createTutoreUni();
    
    TutoreUni createTutoreUni(ResultSet rs) throws DataException;
    
    TutoreUni getTutoreUni(int id) throws DataException;
    
    void insertTutoreUni(TutoreUni tu) throws DataException;
    
    int deleteTutoreUni(int id) throws DataException;
}
