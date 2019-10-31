package it.univaq.ingweb.internshiptutor.data.dao;


import it.univaq.ingweb.framework.data.DataException;
import it.univaq.ingweb.framework.data.DataLayer;
import it.univaq.ingweb.internshiptutor.data.model.*;
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
        registerDAO(Azienda.class, new AziendaDAO_MySQL(this));
        registerDAO(Utente.class, new UtenteDAO_MySQL(this));
        registerDAO(Studente.class, new StudenteDAO_MySQL(this));
        registerDAO(Valutazione.class, new ValutazioneDAO_MySQL(this));
        registerDAO(Candidatura.class, new CandidaturaDAO_MySQL(this));
        registerDAO(OffertaTirocinio.class, new OffertaTirocinioDAO_MySQL(this));
        registerDAO(Resoconto.class, new ResocontoDAO_MySQL(this));
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
    
    public AziendaDAO getAziendaDAO() {
        return (AziendaDAO) getDAO(Azienda.class);
    }
    
    public UtenteDAO getUtenteDAO() {
        return (UtenteDAO) getDAO(Utente.class);
    }
    
    public StudenteDAO getStudenteDAO() {
        return (StudenteDAO) getDAO(Studente.class);
    }
    
    public ValutazioneDAO getValutazioneDAO() {
        return (ValutazioneDAO) getDAO(Valutazione.class);
    }
    
    public CandidaturaDAO getCandidaturaDAO() {
        return (CandidaturaDAO) getDAO(Candidatura.class);
    }
    
    public OffertaTirocinioDAO getOffertaTirocinioDAO() {
        return (OffertaTirocinioDAO) getDAO(OffertaTirocinio.class);
    }
    
    public ResocontoDAO getResocontoDAO() {
        return (ResocontoDAO) getDAO(Resoconto.class);
    }
    
    
}
