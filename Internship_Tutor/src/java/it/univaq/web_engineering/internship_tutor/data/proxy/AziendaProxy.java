/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package it.univaq.web_engineering.internship_tutor.data.proxy;

import it.univaq.web_engineering.internship_tutor.data.impl.AziendaImpl;

/**
 *
 * @author steph
 */
public class AziendaProxy extends AziendaImpl {
    
    protected boolean dirty;
    protected int author_key = 0;
    protected int issue_key = 0;
}
