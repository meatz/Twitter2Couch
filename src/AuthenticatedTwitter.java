


import java.io.Console;
import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.Base64;

/**
 * 
 * Wrapper for the twitter api:
 * 
 * http://apiwiki.twitter.com/HTTP-Response-Codes-and-Errors
 * 
 * @author meatz
 *
 */
public class AuthenticatedTwitter {
	
	
	public static void main(String[] args) {
		try {
			System.out.println( new AuthenticatedTwitter("user", "password").getFriendsTimeline());
		} catch (CouchException e) {
			e.printStackTrace();
		}
	}


   HostnameVerifier hv = new HostnameVerifier() {

       @Override
       public boolean verify(String hostname, SSLSession sslSession) {
           return true;
       }
   };
   
   private static final String AUTHENTICATION_HEADER = "Authorization";
   private static final String BaseURI = "https://twitter.com";
  
   private final String authentication;
   private final WebResource wr;
   
   
   boolean colorsSupported = false;
   Console console;

 

   /**
    * Creates a new client instance with given user credentials
    *
    * @param username
    * @param password
    */
   public AuthenticatedTwitter(String username, String password) {
       authentication = "Basic " + new String (Base64.encode(username+":"+password));
       ClientConfig config = new DefaultClientConfig();

       SSLContext ctx = null;

       try {
           ctx = SSLContext.getInstance("SSL");
           //http://java.sun.com/javase/6/docs/api/javax/net/ssl/SSLContext.html#init
           ctx.init(null, null, null); //use the default implementations
       } catch (Exception e) {
       }

       config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hv, ctx));
      

       Client client = Client.create(config);
       wr = client.resource(BaseURI);

   }

   
   private ObjectMapper mapper = new ObjectMapper();
   
 

//	public JsonNode publicTimeline() throws CouchException {
//		return query("http://api.twitter.com/1/statuses/public_timeline.json");
//	}
   
   public JsonNode getFriendsTimeline()throws CouchException{
	   return queryAuthenticated("statuses/friends_timeline.json");
   }

	private JsonNode queryAuthenticated(String path) throws CouchException{
		try{
			  return mapper.readTree( wr.path(path).header(AUTHENTICATION_HEADER, authentication).accept(MediaType.APPLICATION_JSON_TYPE).get(InputStream.class));
			   
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}  

  
}

