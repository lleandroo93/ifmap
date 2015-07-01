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
@WebServlet(urlPatterns = "/pesquisa")
public class Pesquisar extends HttpServlet {
    
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String NOME = "nome";
    private final String DESCRICAO = "descricao";
    private final Label label = DynamicLabel.label("Ponto");

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
            writer.println("\'pontos\': [");
            Iterator<Node> allNodes = GlobalGraphOperations.at(Neo4j.db).getAllNodesWithLabel(label).iterator();
            while (allNodes.hasNext()){
                node = allNodes.next();
                writer.println("{");
                writer.println("\'nome\': \'" + node.getProperty(NOME) + "\',");
                writer.println("\'descricao\': \'" + node.getProperty(DESCRICAO) + "\',");
                writer.println("\'latitude\': \'" + node.getProperty(LATITUDE) + "\',");
                writer.println("\'longitude\': \'" + node.getProperty(LONGITUDE) + "\'");
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
