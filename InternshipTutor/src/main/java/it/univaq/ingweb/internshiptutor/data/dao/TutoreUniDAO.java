package it.univaq.ingweb.internshiptutor.data.dao;


import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.internshiptutor.data.model.TutoreUni;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public interface TutoreUniDAO {
    
    TutoreUni createTutoreUni();
    
    TutoreUni createTutoreUni(ResultSet rs) throws DataException;
    
    List<TutoreUni> getTutori() throws DataException;
    
    TutoreUni getTutoreUni(int id) throws DataException;
    
    int insertTutoreUni(TutoreUni tu) throws DataException;
    
    int deleteTutoreUni(int id) throws DataException;
}
