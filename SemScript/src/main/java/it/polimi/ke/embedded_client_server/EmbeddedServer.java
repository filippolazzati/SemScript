package it.polimi.ke.embedded_client_server;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;

import java.util.Iterator;

/**
 * This class allows to run a Fuseki server through the Java API provided
 * by Jena. It builds a {@link Model} from an ontology (.owl file) and one from
 * data (.rdf file) and after it performs reasoning through the standard OWL
 * reasoner provided by Jena. The server tunes in the 3031 port. The dataset is called /ds.
 */
public class EmbeddedServer {

    public static void main(String[] args) {

        //Set the name of the file you want to query
        String fileName = "the_social_network";

        //Create a Jena model from the ontology and the data:
        Model ontology = ModelFactory.createDefaultModel();
        Model data = ModelFactory.createDefaultModel();

        //read the data from the files; the serialization format is given by the extension of the file
        ontology.read("./src/main/resources/SemScript.owl");
        data.read("./src/main/resources/movies_in_rdf/" + fileName + ".ttl");

        //configure a reasoner; in this case the default Jena OWL reasoner has been used
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(ontology);
        InfModel inferredModel = ModelFactory.createInfModel(reasoner, data);

        //set the namespace:
        String ex = "http://www.semanticweb.org/elisabetta&amp;filippo/ontologies/2021/4/SemScript#";
        inferredModel.setNsPrefix( "ex", ex );

        //check for inconsistencies between the data
        ValidityReport validity = inferredModel.validate();
        if (validity.isValid()) {
            System.out.println("Data is OK");
        } else {
            System.out.println("Conflicts");
            for (Iterator<ValidityReport.Report> i = validity.getReports(); i.hasNext(); ) {
                ValidityReport.Report report = i.next();
                System.out.println(" - " + report);
            }
        }

        //create the dataset
        Dataset dataset = DatasetFactory.create(inferredModel);

        //start the Fuseki server on 3031 port
        FusekiServer server = FusekiServer.create()
                .add("/ds", dataset)
                .port(3031) //different from the standard 3030
                .build() ;
        server.start() ;

        //set the logging info
        FusekiLogging.setLogging();

        //to print the model on the standard output: (very slow, be careful)
        //inferredModel.write(System.out); //used for debugging
    }
}
