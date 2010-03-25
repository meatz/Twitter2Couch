

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


public class Couch {

	String couchDBServerUrl;
	private ObjectMapper mapper = new ObjectMapper();
	Client client = new Client();
	
	
	public Couch(String couchDBServerUrl){
		this.couchDBServerUrl = couchDBServerUrl;
	}

		
	
	public JsonNode listDatabases() throws CouchException{
		
		WebResource r = client.resource(couchDBServerUrl+"/_all_dbs");
		try {
			return mapper.readTree(r.put(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	
	public JsonNode createNewDatabase(String dbname) throws CouchException{
		dbname = dbname.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+dbname);
		try {
			return mapper.readTree(r.put(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	public JsonNode deleteDatabase(String dbname) throws CouchException{
		dbname = dbname.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+dbname);
		try {
			return mapper.readTree(r.delete(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	public JsonNode getDatabaseInformation(String dbname) throws CouchException{
		dbname = dbname.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+dbname);
		try {
			return mapper.readTree(r.get(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	public JsonNode setDatabaseRevsLimit(String dbname, int limit) throws CouchException{
		dbname = dbname.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+dbname+"/_revs_limit");
		try {
			String foo = limit +"";
			return mapper.readTree(r.entity(foo).put(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	public int getDatabaseRevsLimit(String dbname) throws CouchException{
		dbname = dbname.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+dbname+"/_revs_limit");
		try{
			String result = r.get(String.class).trim();
			return Integer.parseInt(result);
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		}
		
	}
	
	public JsonNode getDatabaseChanges(String dbname) throws CouchException{
		dbname = dbname.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+dbname+"_changes");
		try {
			return mapper.readTree(r.get(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	
	public JsonNode getDocument(String database, String doc_id) throws CouchException{
		database = database.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+database+"/"+doc_id);
		try {
			return mapper.readTree(r.get(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	public JsonNode putJson(String database, String doc_id, JsonNode node) throws CouchException{
		database = database.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+database+"/"+doc_id);
		try {
			return mapper.readTree(r.entity(node.toString(), "application/json").put(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	
	
	
	
//	public JsonNode createObject(String database, Object object) throws CouchException{
//		database = database.toLowerCase();
//		WebResource r = client.resource(couchDBServerUrl+"/"+database+"/");
//		try {
//			
//			return mapper.readTree(r.entity(mapper.writeValueAsString(object), "application/json").post(InputStream.class));
//		} catch (JsonProcessingException e) {
//			throw new CouchException(500, "Malformed JSON");
//		} catch (UniformInterfaceException e) {
//			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
//		} catch (IOException e) {
//			throw new CouchException(500, e.getMessage());
//		}
//	}
//	
	
	
	
	public JsonNode putJson(String database, JsonNode node) throws CouchException{
		database = database.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+database+"/");
		
			try {
				return mapper.readTree(r.entity(node.toString(), "application/json").post(InputStream.class));
			} catch (JsonProcessingException e) {
				throw new CouchException(500, "Malformed JSON");
			} catch (UniformInterfaceException e) {
				throw new CouchException(e.getResponse().getStatus(), e.getMessage());
			} catch (IOException e) {
				throw new CouchException(500, e.getMessage());
			}
		
	}
	
	public JsonNode getAllDocs (String database) throws CouchException{
		database = database.toLowerCase();
		WebResource r = client.resource(couchDBServerUrl+"/"+database+"/_all_docs");
		try {
			return mapper.readTree(r.get(InputStream.class));
		} catch (JsonProcessingException e) {
			throw new CouchException(500, "Malformed JSON");
		} catch (UniformInterfaceException e) {
			throw new CouchException(e.getResponse().getStatus(), e.getMessage());
		} catch (IOException e) {
			throw new CouchException(500, e.getMessage());
		}
	}
	
	
}
