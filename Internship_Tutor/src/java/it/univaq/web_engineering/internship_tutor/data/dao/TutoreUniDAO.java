package it.univaq.web_engineering.internship_tutor.data.dao;


import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.internship_tutor.data.model.TutoreUni;
import java.sql.ResultSet;

/**
 *
 * @author Stefano Florio
 */
public interface TutoreUniDAO {
    
    TutoreUni createTutoreUni();
    
    TutoreUni createTutoreUni(ResultSet rs) throws DataException;
    
    TutoreUni getTutoreUni(int id) throws DataException;
}
