package it.polimi.ke.client_for_fuseki_server;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.*;

import org.apache.jena.system.Txn;

/**
 * This class represents the client of a Fuseki server. It allows to connect
 * to the local Fuseki server on port 3030 and to run the 8 queries contained in the
 * {@link queries} folder. It requires that data is already present on the server.
 * <U>N.B.: it queries the dataset called SemScript.</U>
 */
public class Client {

    //main method
    public static void main(String ...args) {
        //Create a connection with the local Fuseki server (port number = 3030)
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create()
                .destination("http://127.0.0.1:3030/SemScript/query");

        //Query 1:
        Query query1 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "\n" +
                "SELECT ?name\n" +
                "WHERE { ?character ex:hasName ?name.\n" +
                "        ?character ex:says ?something.\n" +
                "        FILTER regex(?something, \"(german)\", \"i\")}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 1: Which characters speak in german at least once?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query1, ResultSetFormatter::out);
        }

        //Query 2:
        Query query2 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "SELECT distinct ?name\n" +
                "WHERE { ?character ex:hasName ?name.\n" +
                "        ?character ex:says ?something.\n" +
                "        FILTER regex(?something, \"Did you know\", \"i\")}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 2: Which characters say the sentence \"Did you know\"?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query2, ResultSetFormatter::out);
        }

        //Query 3:
        Query query3 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "SELECT ?number\n" +
                "WHERE { ?scene ex:location ?location.\n" +
                "        ?scene ex:time ?time.\n" +
                "        ?scene ex:sceneNumber ?number.\n" +
                "        FILTER (?location = \"NY TAXICAB\" && ?time = \"NIGHT\")}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 3: Which are the numbers of the scenes played in a New York taxicab during the night?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query3, ResultSetFormatter::out);
        }

        //Query 4:
        Query query4 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "\n" +
                "SELECT ?page\n" +
                "WHERE { ?scene ex:sceneNumber \"0\".\n" +
                "        ?scene ex:page ?page.}\n" +
                "ORDER BY ASC(?page)");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 4: Which are the pages occupied by the scene number 0?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query4, ResultSetFormatter::out);
        }

        //Query 5:
        Query query5 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "\n" +
                "SELECT ?time\n" +
                "WHERE { ?scene ex:sceneNumber \"5\".\n" +
                "        ?scene ex:time ?time.}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 5: In which part of the day does the scene number 5 take place?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query5, ResultSetFormatter::out);
        }

        //Query 6:
        Query query6 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "\n" +
                "SELECT count(?something)\n" +
                "       WHERE { ?character ex:says ?something.\n" +
                "               FILTER regex(?something, \"Facebook\", \"i\")}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 6: How many times is the word \"Facebook\" used?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query6, ResultSetFormatter::out);
        }

        //Query 7:
        Query query7 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "\n" +
                "SELECT (min(?page) AS ?number)\n" +
                "WHERE { ?scene ex:sceneNumber \"0\".\n" +
                "        ?scene ex:page ?page.}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 7: Which is the starting page of the scene number 0?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query7, ResultSetFormatter::out);
        }

        //Query 8:
        Query query8 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "\n" +
                "SELECT (count(?dualDialogueScene) AS ?times)\n" +
                "          WHERE { ?dualDialogueScene ex:hasActor1 \"EDUARDO\".\n" +
                "                  ?dualDialogueScene ex:hasActor2 \"DUSTIN\".}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 8: How many times Eduardo and Dustin are in the same scene?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query8, ResultSetFormatter::out);
        }
    }
}
