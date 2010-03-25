import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class TwitterAPI {

	private Client client = Client.create();
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Does the acutal work.
	 * Returns a JsonNode representing the result of the query.
	 * @param url
	 * @return
	 * @throws CouchException
	 */
	private JsonNode query(String url) throws CouchException{
		try{
			WebResource r = client.resource(url);
		
		return mapper.readTree(r.get(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	public JsonNode getStatus(String id) throws CouchException{
		return query("http://api.twitter.com/1/statuses/show/"+id+".json");
	}
	
	
	/**
	 * 
	 * @param tag without leading #
	 * @param page starting with 1
	 * @return
	 * @throws CouchException 
	 */
	public JsonNode searchHashTag(String tag, int page) throws CouchException{
		return query("http://search.twitter.com/search.json?q=%23"+tag+"&rpp=100&page="+page);
	}
	
	public JsonNode publicTimeline() throws CouchException {
		return query("http://api.twitter.com/1/statuses/public_timeline.json");
	}

	public JsonNode getCurrentTrends() throws CouchException {
		return query( "http://search.twitter.com/trends/current.json");
	}

	public JsonNode spyUser(String screen_name) throws CouchException {
		return query("http://api.twitter.com/1/users/show.json?screen_name="
				+ screen_name.trim());
	}
}