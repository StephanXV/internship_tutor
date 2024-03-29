/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.ingweb.internshiptutor.data.impl;

import it.univaq.ingweb.internshiptutor.data.model.Azienda;
import it.univaq.ingweb.internshiptutor.data.model.Studente;
import it.univaq.ingweb.internshiptutor.data.model.Valutazione;
import java.util.List;

/**
 *
 * @author Stefano Florio
 */
public class ValutazioneImpl implements Valutazione {
    
    private Studente studente;
    private Azienda azienda;
    private int stelle;

    public ValutazioneImpl() {
        this.studente = null;
        this.azienda = null;
        this.stelle = 0;
    }
    
    @Override
    public Studente getStudente() {
        return studente;
    }

    @Override
    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    @Override
    public Azienda getAzienda() {
        return azienda;
    }

    @Override
    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    @Override
    public int getStelle() {
        return stelle;
    }

    @Override
    public void setStelle(int stelle) {
        this.stelle = stelle;
    }   

    @Override
    public String toString() {
        return "ValutazioneImpl{" + "studente=" + studente + ", azienda=" + azienda + ", stelle=" + stelle + '}';
    }
    
    
    
}
