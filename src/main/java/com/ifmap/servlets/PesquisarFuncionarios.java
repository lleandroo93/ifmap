/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ifmap.servlets;

import com.ifmap.negocio.Neo4j;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author leandro
 */
@WebServlet(urlPatterns = "/pesquisafunc")
public class PesquisarFuncionarios extends HttpServlet{
    
    private final String EMAIL = "email";
    private final String SENHA = "senha";
    private final String NOME = "nome";
    private final Label label = DynamicLabel.label("Funcionario");

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

        try (Transaction tx = Neo4j.db.beginTx())
        {
            Node node;
            writer.println("{");
            writer.println("\'funcionarios\': [");
            Iterator<Node> allNodes = GlobalGraphOperations.at(Neo4j.db).getAllNodesWithLabel(label).iterator();
            while (allNodes.hasNext()){
                node = allNodes.next();
                writer.println("{");
                writer.println("\'nome\': \'" + node.getProperty(NOME) + "\',");
                writer.println("\'email\': \'" + node.getProperty(EMAIL) + "\',");
                writer.println("\'senha\': \'" + node.getProperty(SENHA) + "\'");
                if (allNodes.hasNext()) {
                    writer.println("},");
                } else {
                    writer.println("}");
                }
            }
            writer.println("],");
            writer.println("\'success\': 1");
            writer.println("}");
            tx.success();
        }
    }
}
