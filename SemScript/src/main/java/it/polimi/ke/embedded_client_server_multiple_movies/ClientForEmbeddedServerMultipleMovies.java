package it.polimi.ke.embedded_client_server_multiple_movies;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

/**
 * It allows to connect to the {@link EmbeddedServerMultipleMovies} through the Java API.
 * It connects to localhost on port number 3031 and executes the query
 * correspondent to the competency questions devised.
 * The url for the connection is:
 * http://localhost:3031/ds/query
 */
public class ClientForEmbeddedServerMultipleMovies {
    public static void main(String[] args) {

        //Create a connection with the embedded server (port number = 3031)
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create()
                .destination("http://127.0.0.1:3032/ds/query");

        //Query 0:
        Query query0 = QueryFactory.create("SELECT ?s ?p ?o\n" +
                "       WHERE { ?s ?p ?o}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 0: get the content of the default graph");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query0, ResultSetFormatter::out);
        }

        //Query 1:
        Query query1 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "PREFIX  :       <>\n" +
                "\n" +
                "SELECT ?title\n" +
                "{\n" +
                "?g ex:title ?title.\n" +
                "}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 1: Which are the titles of all the movies in the dataset?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query1, ResultSetFormatter::out);
        }

        //Query 2:
        Query query2 = QueryFactory.create("PREFIX ex:      <http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#>\n" +
                "\n" +
                "SELECT distinct ?title ?name\n" +
                "WHERE{\n" +
                "     ?movie ex:title ?title.\n" +
                "  GRAPH ?movie\n" +
                "  { ?character ex:hasName ?name.\n" +
                "    ?character ex:says ?something.\n" +
                "    FILTER regex(?something, \"Abracadabra\", \"i\")}\n" +
                "}");

        //A connection is built each time; the results are print on the command line
        System.out.println("Query 2: Which characters in which movie say \"Abracadabra\" ?");
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query2, ResultSetFormatter::out);
        }
    }
}
