package it.polimi.ke.embedded_client_server_multiple_movies;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * This class allows to run a Fuseki server through the Java API provided
 * by Jena. It builds a {@link Model} from the rdf files correspondent to the movies.
 * It does not perform reasoning. The server tunes in the 3031 port. The dataset is called /ds.
 * <U>This is the server when you want to query multiple movies (namely different named graphs).</U>
 */
public class EmbeddedServerMultipleMovies {

    public static void main(String[] args) {

        //Set the name of the files you want to query
        String fileName1 = "the_social_network";
        String fileName2 = "marriage_story";

        //Create Jena models from the rdf files:
        Model defaultGraph = ModelFactory.createDefaultModel();
        Model movie1 = ModelFactory.createDefaultModel();
        Model movie2 = ModelFactory.createDefaultModel();

        //read the data from the files; the serialization format is given by the extension of the file
        defaultGraph.read("./src/main/resources/movies_in_rdf/default_graph.ttl");
        movie1.read("./src/main/resources/movies_in_rdf/" + fileName1 + ".ttl");
        movie2.read("./src/main/resources/movies_in_rdf/" + fileName2 + ".ttl");

        //set the namespace:
        String ex = "http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#";
        defaultGraph.setNsPrefix( "ex", ex );
        movie1.setNsPrefix( "ex", ex );
        movie2.setNsPrefix( "ex", ex );

        //create an empty dataset
        Dataset dataset = DatasetFactory.create();
        dataset.getPrefixMapping().setNsPrefix("", "");

        //fill it with the graphs
        dataset.setDefaultModel(defaultGraph);
        dataset.addNamedModel(fileName1 + ".ttl", movie1);
        dataset.addNamedModel(fileName2 + ".ttl", movie2);

        //start the Fuseki server on 3031 port
        FusekiServer server = FusekiServer.create()
                .add("/ds", dataset)
                .port(3032) //different from the standard 3030
                .build() ;
        server.start() ;

        //set the logging info
        FusekiLogging.setLogging();

        //to print the model on the standard output: (very slow, be careful) //used for debugging
        //System.out.println("***************************************default graph**************************************");
        //defaultGraph.write(System.out);
        //System.out.println("***************************************" + fileName1 + "named graph**************************************");
        //movie1.write(System.out);
        //System.out.println("***************************************" + fileName2 + "named graph**************************************");
        //movie2.write(System.out);
    }
}
