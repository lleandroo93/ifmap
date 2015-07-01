/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifmap.servlets;

import com.ifmap.negocio.Neo4j;
import com.ifmap.negocio.Relacao;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 *
 * @author leandro
 */
@WebServlet(urlPatterns = "/addponto")
public class AdicionarPonto extends HttpServlet {

    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String NOME = "nome";
    private final String DESCRICAO = "descricao";
    private final Label labelPonto = DynamicLabel.label("Ponto");
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
        String lat = req.getParameter("lat");
        String lon = req.getParameter("lon");
        String nome = req.getParameter("nome");
        String desc = req.getParameter("desc");

        if (null != lat && null != lon) {
            try (Transaction tx = Neo4j.db.beginTx()) {
                Node no = Neo4j.db.createNode(labelPonto);
                no.setProperty(LATITUDE, lat);
                no.setProperty(LONGITUDE, lon);
                no.setProperty(NOME, nome);
                no.setProperty(DESCRICAO, desc);
                for (int i = 0; i < 5; i++) {
                    if (req.getParameter("llat" + i) != null) {
                        criaRelacaoEntre(no, req.getParameter("llat" + i), req.getParameter("llong" + i));
                    }
                }
                writer.println(Neo4j.engine.execute("match (n) return n").dumpToString());
                writer.println(Neo4j.engine.execute("MATCH (a)-[r]->(b)\n"
                        + " WHERE labels(a) <> [] AND labels(b) <> []\n"
                        + " RETURN DISTINCT head(labels(a)) AS This, type(r) as To, head(labels(b)) AS That\n"
                        + " LIMIT 10").dumpToString());
                tx.success();
            }

        } else {
            writer.println("Precisa informar latitude e longitude");
        }

    }

    private void criaRelacaoEntre(Node no, String latitude, String longitude) {
        double lat = Double.parseDouble(latitude);
        double longt = Double.parseDouble(longitude);
        double nola = Double.parseDouble((String) no.getProperty(LATITUDE));
        double nolo = Double.parseDouble((String) no.getProperty(LONGITUDE));
        double distancia = Math.sqrt((Math.pow(lat - nola, 2) + Math.pow(longt - nolo, 2)) * 10000) / 100;

        try (ResourceIterator<Node> pontos = Neo4j.db.findNodesByLabelAndProperty(labelNo, "latitude", latitude).iterator()) {
            while (pontos.hasNext()) {
                Node ponto = pontos.next();
                System.out.println(ponto.getProperty(LONGITUDE));
                if (ponto.getProperty(LONGITUDE).equals(longitude)) {
                    Relationship relacao1 = no.createRelationshipTo(ponto, Relacao.CONHECE);
                    relacao1.setProperty("distancia", distancia);
                    
                    Relationship relacao2 = ponto.createRelationshipTo(no, Relacao.CONHECE);
                    relacao2.setProperty("distancia", distancia);
                    break;
                }
            }
        }
    }

}
