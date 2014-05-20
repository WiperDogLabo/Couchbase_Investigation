//Prepare dependencies before run script using command 
// grape install com.couchbase.client couchbase-client 1.4.1
// grape install org.fusesource.jansi jansi 1.4
// Afterthat , pease create a "test_bucket" bucket before run script
@Grab(group="com.couchbase.client",module="couchbase-client",version="1.4.1")
@Grab(group="org.fusesource.jansi",module="jansi",version="1.4")
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
//Using 3rd party api to customize output text color
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class CouchbaseExample {
	public static void main(String [] args) throws Exception{
	    ArrayList<URI> nodes = new ArrayList<URI>();

	    // Add one or more nodes of cluster (exchange the IP )
	    nodes.add(URI.create("http://127.0.0.1:8091/pools"));

	    // Try to connect to the client
	    CouchbaseClient client = null;
	    
	    def bucket = "test_bucket"
	    def user = "admin"
	    def passwd = ""
	    try {
	      client = new CouchbaseClient(nodes,bucket,user,passwd);
	    } catch (Exception e) {
	      System.err.println(ansi().fg(RED).a("Error connecting to Couchbase: " + e.getMessage()).reset());
	      System.exit(1);
	    }

	    //Perform CRUD Query on Couchbase server
	    //CREATE document
	    def key = "wiperdog"
	    def value = "{\"description\" : \"A greate database monitoring system !\"}"
	    client.set(key,value)

		//We can insert object directly and couchbase save it as binary data
	    def key2 = "wiperdog2"
		def value2 = [description : "A greate database monitoring system !"]
		client.set(key2,value2)

		//READ document
		//data return from get() is a Object instance
		def data =(String) client.get("wiperdog");
		println ansi().fg(GREEN).a("Data get from key wiperdog: " + data).reset()

		data = client.get("wiperdog2");	
		println ansi().fg(GREEN).a("Data get from key wiperdog2 : " + data.description).reset()	    

		//UPDATE document
		value = "{\"description\" : \"A greate database monitoring system and more !\"}"
		client.replace(key,value)
		data = client.get("wiperdog");
		println ansi().fg(GREEN).a("Data get from key wiperdog after update: " + data).reset()

		//DELETE document
		client.delete("wiperdog")
		data = client.get("wiperdog");
		println ansi().fg(GREEN).a("Data get from key wiperdog after delete: " + data).reset()		

	    // Shutdown the client
	    client.shutdown();
	}
}
