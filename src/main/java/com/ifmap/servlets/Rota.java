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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author leandro
 */
@WebServlet(urlPatterns = "/rota")
public class Rota extends HttpServlet {

    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final Label label = DynamicLabel.label("Ponto");
    private Float B;
    private Float A;
    private Float C;
    private Float menorDistancia;

    @Override
    public void init() throws ServletException {
        if (Neo4j.db == null) {
            new Neo4j();
        }
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Float latOrigem = Float.parseFloat(req.getParameter("lato"));
        Float longOrigem = Float.parseFloat(req.getParameter("lono"));
        String latDestino = req.getParameter("latd");
        String longDestino = req.getParameter("lond");

        List<Relationship> relationships = getRelationships();
        Relationship closestRelationshipPath = getClosestPath(relationships, latOrigem, longOrigem);
        WeightedPath path = getPath(closestRelationshipPath, latDestino, longDestino);

        PrintWriter writer = resp.getWriter();
        Iterator<Node> nodes = path.nodes().iterator();
        try (Transaction tx = Neo4j.db.beginTx()) {
            writer.println(closestRelationshipPath.getEndNode() + " - " + closestRelationshipPath.getStartNode());
            Node node;
            writer.println("{");
            writer.println("\'pontos\': [");
            while (nodes.hasNext()) {
                node = nodes.next();
                writer.println("{");
                writer.println("\'latitude\': \'" + node.getProperty(LATITUDE) + "\',");
                writer.println("\'longitude\': \'" + node.getProperty(LONGITUDE) + "\',");
                if (nodes.hasNext()) {
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

    private WeightedPath getPath(Relationship closestRelationshipPath, String latDestino, String longDestino) {
        try (Transaction tx = Neo4j.db.beginTx()) {
            Node endNode = closestRelationshipPath.getEndNode();
            Node startNode = closestRelationshipPath.getStartNode();
            WeightedPath pathEndNode = bestPathWithDijkstra(latDestino, longDestino, endNode);
            WeightedPath pathStartNode = bestPathWithDijkstra(latDestino, longDestino, startNode);

            if (pathEndNode.weight() < pathStartNode.weight()) {
                tx.success();
                return pathEndNode;
            } else {
                tx.success();
                return pathStartNode;
            }
        }
    }

    private WeightedPath bestPathWithDijkstra(String latDestino, String longDestino, Node endNode) {
        Node ponto = null;

        try (ResourceIterator<Node> pontos = Neo4j.db.findNodesByLabelAndProperty(label, "latitude", latDestino).iterator()) {
            while (pontos.hasNext()) {
                ponto = pontos.next();
                if (ponto.getProperty(LONGITUDE).equals(longDestino)) {
                    break;
                }
            }
        }
        PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(
                PathExpanders.forTypeAndDirection(Relacao.CONHECE, Direction.BOTH), "distancia");

        WeightedPath path = finder.findSinglePath(endNode, ponto);
        return path;
    }

    private Relationship getClosestPath(List<Relationship> relationships, Float latOrigem, Float longOrigem) throws NumberFormatException {
        Relationship closestPath = null;
        menorDistancia = 99999999999f;
        try (Transaction tx = Neo4j.db.beginTx()) {
            for (Relationship relationship : relationships) {
                Node startNode = relationship.getStartNode();
                Node endNode = relationship.getEndNode();

                Float x1 = Float.parseFloat((String) startNode.getProperty(LATITUDE));
                Float y1 = Float.parseFloat((String) startNode.getProperty(LONGITUDE));
                Float x2 = Float.parseFloat((String) endNode.getProperty(LATITUDE));
                Float y2 = Float.parseFloat((String) endNode.getProperty(LONGITUDE));

                if (x2 == 0 && x1 == 0) {
                    A = 0f;
                } else {
                    A = -((y2 - y1) / (x2 - x1));
                }
                B = 1f;
                C = - y1 - (A * x1);

                Float distancia = Math.abs(A * latOrigem + B * longOrigem + C) / ((float) Math.sqrt(A * A + B * B));
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    closestPath = relationship;
                }
            }
            tx.success();
        }
        return closestPath;
    }

    private List<Relationship> getRelationships() {
        List<Relationship> rels = new ArrayList<>();
        try (Transaction tx = Neo4j.db.beginTx()) {
            for (Relationship rel : GlobalGraphOperations.at(Neo4j.db).getAllRelationships()) {
                rels.add(rel);
            }
            tx.success();
        }
        return rels;
    }

}
