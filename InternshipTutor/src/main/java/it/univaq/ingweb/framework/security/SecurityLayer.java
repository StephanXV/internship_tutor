package it.univaq.ingweb.framework.security;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityLayer {

    //--------- SESSION SECURITY ------------    
    //questa funzione esegue una serie di controlli di sicurezza
    //sulla sessione corrente. Se la sessione non è valida, la cancella
    //e ritorna null, altrimenti la aggiorna e la restituisce
    //this method executed a set of standard chacks on the current session.
    //If the session exists and is valid, it is rerutned, otherwise
    //the session is invalidated and the method returns null
    public static HttpSession checkSession(HttpServletRequest r) {
        boolean check = true;

        HttpSession s = r.getSession(false);
        //per prima cosa vediamo se la sessione è attiva
        //first, let's see is the sessione is active
        if (s == null) {
            return null;
        }

        //check sulla validità  della sessione
        //second, check is the session contains valid data
        if (s.getAttribute("id_utente") == null) {
            check = false;
            //check sull'ip del client
            //check if the client ip chaged
        } else if ((s.getAttribute("ip") == null) || !((String) s.getAttribute("ip")).equals(r.getRemoteHost())) {
            check = false;
            //check sulle date
            //check if the session is timed out
        } else {
            //inizio sessione
            //session start timestamp
            Calendar begin = (Calendar) s.getAttribute("inizio-sessione");
            //ultima azione
            //last action timestamp
            Calendar last = (Calendar) s.getAttribute("ultima-azione");
            //data/ora correnti
            //current timestamp
            Calendar now = Calendar.getInstance();
            if (begin == null) {
                check = false;
            } else {
                //secondi trascorsi dall'inizio della sessione
                //seconds from the session start
                long secondsfrombegin = (now.getTimeInMillis() - begin.getTimeInMillis()) / 1000;
                //dopo tre ore la sessione scade
                //after three hours the session is invalidated
                if (secondsfrombegin > 3 * 60 * 60) {
                    check = false;
                } else if (last != null) {
                    //secondi trascorsi dall'ultima azione
                    //seconds from the last valid action
                    long secondsfromlast = (now.getTimeInMillis() - last.getTimeInMillis()) / 1000;
                    //dopo trenta minuti dall'ultima operazione la sessione è invalidata
                    //after 30 minutes since the last action the session is invalidated                    
                    if (secondsfromlast > 30 * 60) {
                        check = false;
                    }
                }
            }
        }
        if (!check) {
            s.invalidate();
            return null;
        } else {
            //reimpostiamo la data/ora dell'ultima azione
            //if che checks are ok, update the last action timestamp
            s.setAttribute("ultima-azione", Calendar.getInstance());
            return s;
        }
    }

    public static HttpSession createSession(HttpServletRequest request, String username, int id_utente, String tipo) {
        HttpSession s = request.getSession(true);
        s.setAttribute("username", username);
        s.setAttribute("ip", request.getRemoteHost());
        s.setAttribute("inizio-sessione", Calendar.getInstance());
        s.setAttribute("id_utente", id_utente);
        s.setAttribute("tipologia", tipo);
        return s;
    }

    public static void disposeSession(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }

    //--------- DATA SECURITY ------------

    //questa funzione rimuove gli slash aggiunti da addSlashes
    //this function removes the slashes added by addSlashes
    public static String stripSlashes(String s) {
        return s.replaceAll("\\\\(['\"\\\\])", "$1");
    }

    public static int checkNumeric(String s) throws NumberFormatException {
        //convertiamo la stringa in numero, ma assicuriamoci prima che sia valida
        //convert the string to a number, ensuring its validity
        if (s != null) {
            //se la conversione fallisce, viene generata un'eccezione
            //if the conversion fails, an exception is raised
            return Integer.parseInt(s);
        } else {
            throw new NumberFormatException("impossibile convertire");
        }
    }

    public static boolean checkNumericBool(String s) throws NumberFormatException {
        if (s != null && !s.equals("") && s.length() > 0) {
            try {
                int n = Integer.parseInt(s);
            } catch (NumberFormatException | NullPointerException nfe) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean checkDurata(String string) {
            if (string.length()<4){
                try {
                    int d = Integer.parseInt(string);
                } catch (NumberFormatException | NullPointerException nfe) {
                    return false;
                }
                return true;
            }
	    return false;
    }


    public static String issetString(String s) throws SecurityLayerException {
        //convertiamo la stringa in numero, ma assicuriamoci prima che sia valida
        //convert the string to a number, ensuring its validity
        if (s != null && s.length() > 0) {
            //se la conversione fallisce, viene generata un'eccezione
            //if the conversion fails, an exception is raised
            return s;
        } else {
            throw new SecurityLayerException("Richiesta non valida");
        }
    }


    public static boolean checkString(String s) throws IllegalArgumentException {
        //convertiamo la stringa in numero, ma assicuriamoci prima che sia valida
        //convert the string to a number, ensuring its validity
        if (s != null && s.length() > 0) {
            //se la conversione fallisce, viene generata un'eccezione
            //if the conversion fails, an exception is raised
            return true;
        } else {
            throw new IllegalArgumentException("String argument is null");
        }
    }

    public static LocalDate checkDate(String date) throws IllegalArgumentException {
        //convertiamo la stringa in data, ma assicuriamoci prima che sia valida
        if (date != null) {
            return LocalDate.parse(date);
        } else {
            throw new IllegalArgumentException("Date wrong");
        }
    }
    
    public static boolean checkDateString(String date) throws IllegalArgumentException {
        //convertiamo la stringa in data, ma assicuriamoci prima che sia valida
        if (date != null) {
            try {
                LocalDate.parse(date);
                
            } catch (DateTimeParseException ex){
                throw new IllegalArgumentException("Date wrong");
            }
            return true;
        }
        return false;
    }
    
     public static boolean checkBoolean(String b) throws IllegalArgumentException {
        //convertiamo la stringa in data, ma assicuriamoci prima che sia valida
        if (b != null) {
            if (b.equals("true") || b.equals("false"))
                return true;
        }
        return false;
    }
    
    // controllo sulla validità di una email
    public static boolean checkEmail(String email) {
	String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	return Pattern.compile(emailRegex).matcher(email).matches();
    }
    
    // controllo che una stringa sia in un range di lunghezza
    public static boolean hasLength(String string, int minLength, int maxLength) {
	return string != null && string.length() >= minLength && string.length() <= maxLength;
    }

    // controllo che una stringa sia un numero di telefono valido (to do)
    public static boolean checkTelefono(String t) {
        if (t != null && t.length() > 0) {
            return true;
        } else {
            throw new IllegalArgumentException("Telefono argument isn't valid");
        }
    }
    
    // controllo sul cap
    public static boolean checkCap(String c) {
        if (c != null && c.length() == 5) {
            return true;
        } else {
            throw new IllegalArgumentException("Cap argument isn't valid");
        }
    }
    
    //--------- CONNECTION SECURITY ------------
    //questa funzione verifica se il protocollo HTTPS è attivo
    //checks if the HTTPS protocol is in use
    public static boolean checkHttps(HttpServletRequest r) {
        return r.isSecure();
        //metodo "fatto a mano" che funziona solo se il server trasmette gli header corretti
        //the following is an "handmade" alternative, which works only if the server sends correct headers
        //String httpsheader = r.getHeader("HTTPS");
        //return (httpsheader != null && httpsheader.toLowerCase().equals("on"));
    }

    //questa funzione ridirige il browser sullo stesso indirizzo
    //attuale, ma con protocollo https
    //this function redirects the browser on the current address, but
    //with https protocol
    public static void redirectToHttps(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //estraiamo le parti della request url
        String server = request.getServerName();
        //int port = request.getServerPort();
        String context = request.getContextPath();
        String path = request.getServletPath();
        String info = request.getPathInfo();
        String query = request.getQueryString();

        //ricostruiamo la url cambiando il protocollo e la porta COME SPECIFICATO NELLA CONFIGURAZIONE DI TOMCAT
        //rebuild the url changing port and protocol AS SPECIFIED IN THE SERVER CONFIGURATION
        String newUrl = "https://" + server + ":8443" + context + path + (info != null ? info : "") + (query != null ? "?" + query : "");
        try {
            //ridirigiamo il client
            //redirect
            response.sendRedirect(newUrl);
        } catch (IOException ex) {
            try {
                //in caso di problemi tentiamo prima di inviare un errore HTTP standard
                //in case of problems, first try to send a standard HTTP error message
                response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Cannot redirect to HTTPS, blocking request");
            } catch (IOException ex1) {
                //altrimenti generiamo un'eccezione
                //otherwise, raise an exception
                throw new ServletException("Cannot redirect to https!");
            }
        }
    }
}
