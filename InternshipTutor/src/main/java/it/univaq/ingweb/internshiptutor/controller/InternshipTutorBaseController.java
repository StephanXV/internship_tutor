/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.ingweb.internshiptutor.controller;

import it.univaq.ingweb.framework.result.FailureResult;
import it.univaq.ingweb.internshiptutor.data.dao.InternshipTutorDataLayer;
import org.apache.log4j.Logger;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Stefano Florio
 */
public abstract class InternshipTutorBaseController extends HttpServlet {

    //logger
    final static Logger logger = Logger.getLogger(InternshipTutorBaseController.class);

    @Resource(name = "jdbc/itdb")
    private DataSource ds;
    
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        //WARNING: never declare DB-related objects including references to Connection and Statement (as our data layer)
        //as class variables of a servlet. Since servlet instances are reused, concurrent requests may conflict on such
        //variables leading to unexpected results. To always have different connections and statements on a per-request
        //(i.e., per-thread) basis, declare them in the doGet, doPost etc. (or in methods called by them) and 
        //(possibly) pass such variables through the request.
        try (InternshipTutorDataLayer datalayer = new InternshipTutorDataLayer(ds)) {
            datalayer.init();
            request.setAttribute("datalayer", datalayer);
            processRequest(request, response);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            (new FailureResult(getServletContext())).activate(ex, request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

}
