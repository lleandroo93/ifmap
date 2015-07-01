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
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author Home
 */
@WebServlet(urlPatterns = "/pesquisanos")
public class PesquisarNos extends HttpServlet {

    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final Label labelNo = DynamicLabel.label("No");

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

        try (Transaction tx = Neo4j.db.beginTx()) {
            Relationship relacao;
            writer.println("{");
            writer.println("\'pontos\': [");
            Iterator<Relationship> allNodes = GlobalGraphOperations.at(Neo4j.db).getAllRelationships().iterator();
            System.out.println("getAllRelationships");
            boolean escreve = false;
            while (allNodes.hasNext()) {
                System.out.println("hasNext");
                relacao = allNodes.next();
                System.out.println("Label endNode = " + relacao.getEndNode().hasLabel(labelNo));
                System.out.println("Label startNode = " + relacao.getStartNode().hasLabel(labelNo));
                if (relacao.getEndNode().hasLabel(labelNo) && relacao.getStartNode().hasLabel(labelNo)) {
                    if (escreve){
                        writer.println(",");
                    } else {
                        escreve = true;
                    }
                    writer.println("{");
                    writer.println("\'latorigem\': \'" + relacao.getEndNode().getProperty(LATITUDE) + "\',");
                    writer.println("\'longorigem\': \'" + relacao.getEndNode().getProperty(LONGITUDE) + "\',");
                    writer.println("\'latdestino\': \'" + relacao.getStartNode().getProperty(LATITUDE) + "\',");
                    writer.println("\'longdestino\': \'" + relacao.getStartNode().getProperty(LONGITUDE) + "\'");
                    writer.print("}");
                }
            }
            System.out.println("End while");
            writer.println();
            writer.println("],");
            writer.println("\'success\': 1");
            writer.println("}");
            tx.success();
        }
    }
}
