/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifmap.util;

import com.ifmap.negocio.Neo4j;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author Home
 */
public class AddNos {

    private static final String DB_PATH = Neo4j.DB_PATH;
    private final Label label = DynamicLabel.label("No");

    public String greeting;

    // START SNIPPET: vars
    GraphDatabaseService graphDb;
    Node no1;
    Node no2;
    Node no3;
    Node no4;
    Node no5;
    Node no6;
    Node no7;
    Node no8;
    Node no9;
    Node no10;
    Node no11;
    Node no12;
    Node no13;
    Node no14;
    Node no15;
    Node no16;
    Node no17;
    Node no18;
    Relationship r1;
    Relationship r2;
    Relationship r3;
    Relationship r4;
    Relationship r5;
    Relationship r6;
    Relationship r7;
    Relationship r8;
    Relationship r9;
    Relationship r10;
    Relationship r11;
    Relationship r12;
    Relationship r13;
    Relationship r14;
    Relationship r15;
    Relationship r16;
    Relationship r17;
    Relationship r18;
    Relationship r19;
    // END SNIPPET: vars

    // START SNIPPET: createReltype
    private static enum RelTypes implements RelationshipType {

        CONHECE
    }

    public static void main(String[] args) {
        AddNos hello = new AddNos();
        hello.createDb();
        hello.shutDown();
    }

    void createDb() {
        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerShutdownHook(graphDb);
        // END SNIPPET: startDb

        // START SNIPPET: transaction
        try (Transaction tx = graphDb.beginTx()) {
            // Database operations go here
            // END SNIPPET: transaction
            // START SNIPPET: addData
            no1 = graphDb.createNode();
            no1.addLabel(label);
            no1.setProperty("latitude", "2.812942");
            no1.setProperty("longitude", "-60.693830");

            no2 = graphDb.createNode();
            no2.addLabel(label);
            no2.setProperty("latitude", "2.813191");
            no2.setProperty("longitude", "-60.693928");
 
            r1 = no1.createRelationshipTo(no2, AddNos.RelTypes.CONHECE);
            r1.setProperty("distancia",calculaDistancia(2.812942f, 2.813191f, -60.693830f, -60.693928f));
            
            no3 = graphDb.createNode();
            no3.addLabel(label);
            no3.setProperty("latitude", "2.813009");
            no3.setProperty("longitude", "-60.694363");
            
            r2 = no3.createRelationshipTo(no2, AddNos.RelTypes.CONHECE);
            r2.setProperty("distancia",calculaDistancia(2.813191f, 2.813009f, -60.693928f, -60.694363f));
            
            no4 = graphDb.createNode(); //entre a CORES e biblioteca
            no4.addLabel(label);
            no4.setProperty("latitude", "2.813535");
            no4.setProperty("longitude", "-60.694058");
            
            r3 = no4.createRelationshipTo(no2, AddNos.RelTypes.CONHECE);
            r3.setProperty("distancia",calculaDistancia(2.813535f, 2.813009f, -60.694058f, -60.694363f));
            
            no5 = graphDb.createNode(); //frente da deg
            no5.addLabel(label);
            no5.setProperty("latitude", "2.813435");
            no5.setProperty("longitude", "-60.694306");
            
            r4 = no5.createRelationshipTo(no4, AddNos.RelTypes.CONHECE);
            r4.setProperty("distancia", calculaDistancia(2.813535f, 2.813435f, -60.694058f, -60.694306f));
            
            no6 = graphDb.createNode();  //começo do corredor do ensino médio (deiinf)
            no6.addLabel(label);
            no6.setProperty("latitude", "2.813317");
            no6.setProperty("longitude", "-60.694250");
            
            r5 = no6.createRelationshipTo(no5, AddNos.RelTypes.CONHECE);
            r5.setProperty("distancia", calculaDistancia(2.813435f, 2.813317f, -60.694306f, -60.694250f));
            
            no7 = graphDb.createNode(); //escada do ensino médio
            no7.addLabel(label);
            no7.setProperty("latitude", "2.813094");
            no7.setProperty("longitude", "-60.694797");
            
            r6 = no7.createRelationshipTo(no6, AddNos.RelTypes.CONHECE);
            r6.setProperty("distancia", calculaDistancia(2.813317f, 2.813094f, -60.694250f, -60.694797f));
            
            no8 = graphDb.createNode();  //final do corredor do ensino médio
            no8.addLabel(label);
            no8.setProperty("latitude", "2.813009");
            no8.setProperty("longitude", "-60.695025");
            
            r7 = no8.createRelationshipTo(no7, AddNos.RelTypes.CONHECE);
            r7.setProperty("distancia", calculaDistancia(2.813094f, 2.813009f, -60.694797f, -60.695025f));
            
            no9 = graphDb.createNode(); //rampa do TADS
            no9.addLabel(label);
            no9.setProperty("latitude", "2.813783");
            no9.setProperty("longitude", "-60.694167");
            
            r8 = no9.createRelationshipTo(no4, AddNos.RelTypes.CONHECE);
            r8.setProperty("distancia", calculaDistancia(2.813535f, 2.813783f, -60.694058f, -60.694167f));
            
            no10 = graphDb.createNode();  //perto do banheiro - pracinha - caminho pra quadra
            no10.addLabel(label);
            no10.setProperty("latitude", "2.813682");
            no10.setProperty("longitude", "-60.694412");
            
            r9 = no10.createRelationshipTo(no9, AddNos.RelTypes.CONHECE);
            r9.setProperty("distancia", calculaDistancia(2.813783f, 2.813682f, -60.694167f, -60.694412f));
            
            r10 = no10.createRelationshipTo(no5, AddNos.RelTypes.CONHECE);
            r10.setProperty("distancia", calculaDistancia(2.813435f, 2.813682f, -60.694306f, -60.694412f));
            
            no11 = graphDb.createNode();  //frente da  quadra
            no11.addLabel(label);
            no11.setProperty("latitude", "2.813540");
            no11.setProperty("longitude", "-60.694729");
            
            r11 = no11.createRelationshipTo(no10, AddNos.RelTypes.CONHECE);
            r11.setProperty("distancia", calculaDistancia(2.813682f, 2.813540f, -60.694412f, -60.694729f));
        
            no12 = graphDb.createNode(); //armários
            no12.addLabel(label);
            no12.setProperty("latitude", "2.813300");
            no12.setProperty("longitude", "-60.694635");
            
            r12 = no12.createRelationshipTo(no11, AddNos.RelTypes.CONHECE);
            r12.setProperty("distancia", calculaDistancia(2.813540f, 2.813300f, -60.694729f, -60.694635f));
            
            r13 = no12.createRelationshipTo(no5, AddNos.RelTypes.CONHECE);
            r13.setProperty("distancia", calculaDistancia(2.813435f, 2.813300f, -60.694306f, -60.694635f));
            
            no13 = graphDb.createNode(); //saida corredor do ensino médio / banheiros
            no13.addLabel(label);
            no13.setProperty("latitude", "2.813198");
            no13.setProperty("longitude", "-60.694836");
            
            r14 = no13.createRelationshipTo(no7, AddNos.RelTypes.CONHECE);
            r14.setProperty("distancia", calculaDistancia(2.813094f, 2.813198f, -60.694797f, -60.694836f));
            
            no14 = graphDb.createNode();
            no14.addLabel(label);
            no14.setProperty("latitude", "2.813091");
            no14.setProperty("longitude", "-60.695072");
            
            r15 = no14.createRelationshipTo(no13, AddNos.RelTypes.CONHECE);
            r15.setProperty("distancia", calculaDistancia(2.813198f, 2.813091f, -60.694836f, -60.695072f));
            
            no15 = graphDb.createNode(); //S para cantina
            no15.addLabel(label);
            no15.setProperty("latitude", "2.813927");
            no15.setProperty("longitude", "-60.694272");
            
            r16 = no15.createRelationshipTo(no9, AddNos.RelTypes.CONHECE);
            r16.setProperty("distancia", calculaDistancia(2.813783f, 2.813927f, -60.694167f, -60.694272f));
            
            no16 = graphDb.createNode(); //S para cantina
            no16.addLabel(label);
            no16.setProperty("latitude", "2.813943");
            no16.setProperty("longitude", "-60.694163");
            
            r17 = no16.createRelationshipTo(no15, AddNos.RelTypes.CONHECE);
            r17.setProperty("distancia", calculaDistancia(2.813927f, 2.813943f, -60.694272f, -60.694163f));
            
            no17 = graphDb.createNode();
            no17.addLabel(label);
            no17.setProperty("latitude", "2.814061");
            no17.setProperty("longitude", "-60.694213");
            
            r18 = no17.createRelationshipTo(no16, AddNos.RelTypes.CONHECE);
            r18.setProperty("distancia", calculaDistancia(2.813943f, 2.814061f, -60.694163f, -60.694213f));
            
            
            tx.success();
            
        }
        // END SNIPPET: transaction
    }

    void shutDown() {
        System.out.println();
        System.out.println("Shutting down database ...");
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }

    // START SNIPPET: shutdownHook
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    private String calculaDistancia(float lat1, float lat2, float lon1, float lon2) {
        return String.valueOf(Math.sqrt((Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2)) * 10000) / 100);
    }
    // END SNIPPET: shutdownHook
}
