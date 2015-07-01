/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifmap.servlets;

import com.ifmap.negocio.Neo4j;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.neo4j.graphdb.Transaction;

/**
 *
 * @author Home
 */
@WebServlet(name = "Excluir", urlPatterns = {"/excluir"})
public class ExcluirPonto extends HttpServlet {

    @Override
    public void init() throws ServletException {
        if (Neo4j.db == null) {
            new Neo4j();
        }
        super.init();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ponto = request.getParameter("ponto");
        try (Transaction tx = Neo4j.db.beginTx()) {
            System.out.println("MATCH(n:Ponto {nome:\'" + ponto + "\'}) OPTIONAL MATCH (n)-[r]-() DELETE n, r");
            Neo4j.engine.execute("MATCH(n:Ponto {nome:\'" + ponto + "\'}) OPTIONAL MATCH (n)-[r]-() DELETE n, r");
            tx.success();
        }
    }

}
