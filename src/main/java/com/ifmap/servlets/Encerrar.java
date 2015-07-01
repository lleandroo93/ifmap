/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ifmap.servlets;

import com.ifmap.negocio.Neo4j;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author leandro
 */
@WebServlet(urlPatterns = "/encerrar")
public class Encerrar extends HttpServlet{

    @Override
    public void init() throws ServletException {
        if (Neo4j.db == null) {
            new Neo4j();
        }
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Neo4j.db.shutdown();
        writer.print("GraphDB desconectado com sucesso");
    }
    
    
    
}
