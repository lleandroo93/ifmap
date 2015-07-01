/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ifmap.servlets;

import com.ifmap.negocio.Neo4j;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 *
 * @author leandro
 */
@WebServlet(urlPatterns = "/login")
public class Login extends HttpServlet {

    @Override
    public void init() throws ServletException {
        if (Neo4j.db == null) {
            new Neo4j();
        }
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        if (null != email && null != senha) {
            try (Transaction tx = Neo4j.db.beginTx()) {
                ExecutionResult execute = Neo4j.engine.execute("match (n:Funcionario {email:\'" + email + "\', senha:\'" + senha + "\'}) return n");
                if (execute.iterator().hasNext()) {
                    writer.print("1");
                } else {
                    writer.print("0");
                }
                tx.success();
            }

        } else {
            writer.println("Precisa informar email e senha");
        }
    }

}
