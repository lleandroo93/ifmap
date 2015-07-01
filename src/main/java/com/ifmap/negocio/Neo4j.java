/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ifmap.negocio;

import java.io.Serializable;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author leandro
 */
public class Neo4j implements Serializable{
    public static GraphDatabaseService db;
    public static ExecutionEngine engine;
    public static final String DB_PATH = "/home/bfsf/Documentos/ifmap";

    public Neo4j() {
        Neo4j.db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        Neo4j.engine = new ExecutionEngine(db);
        registerShutdownHook(db);
    }
    
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
