package it.univaq.web_engineering.internship_tutor.data.dao;

;
import it.univaq.web_engineering.framework.data.DataException;
import it.univaq.web_engineering.framework.data.DataLayer;
import it.univaq.web_engineering.internship_tutor.data.model.RespTirocini;
import it.univaq.web_engineering.internship_tutor.data.model.TutoreTirocinio;
import it.univaq.web_engineering.internship_tutor.data.model.TutoreUni;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author Stefano Florio
 */
public class InternshipTutorDataLayer extends DataLayer {

    public InternshipTutorDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
   
    @Override
    public void init() throws DataException {
        //registriamo i nostri dao
        registerDAO(RespTirocini.class, new RespTirociniDAO_MySQL(this));
        registerDAO(TutoreTirocinio.class, new TutoreTirocinioDAO_MySQL(this));
        registerDAO(TutoreUni.class, new TutoreUniDAO_MySQL(this));
    }
        
    //helpers    
    public RespTirociniDAO getRespTirociniDAO() {
        return (RespTirociniDAO) getDAO(RespTirocini.class);
    }

    public TutoreTirocinioDAO getTutoreTirocinioDAO() {
        return (TutoreTirocinioDAO) getDAO(TutoreTirocinio.class);
    }

    public TutoreUniDAO getTutoreUniDAO() {
        return (TutoreUniDAO) getDAO(TutoreUni.class);
    }
}
