import java.util.Iterator;

import org.codehaus.jackson.JsonNode;


public class Dumper extends Thread{

	
	String hashtag_db_prefix = "hashtag_";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		new Dumper().start();
//		new Dumper().dumptimeline();
//		new Dumper().dumpHashtag("zensursula");
	}
	
	
	private Couch couch;
	private TwitterAPI twitter;
	
	
	public Dumper(){
		couch = new Couch("http://localhost:5984");
		createDBs();
		twitter = new TwitterAPI();
	}
	
	
	@Override
	public void run() {
		try {
			dumptimeline();
			Thread.sleep(45000);
			
//			dumpTrends();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	super.run();
	}
	
	
	
	private void createDBs(){
		try {
			couch.createNewDatabase("timeline");
		} catch (CouchException e) {
			// db already exists
		}
	}
	
	
	public void spyUser(String screen_name){
		
		try {
			JsonNode node = twitter.spyUser("meatz");
			System.out.println(node.toString());
		} catch (CouchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	public void dumptimeline(){
		try {
			JsonNode node = twitter.publicTimeline();
			Iterator<JsonNode> iter = node.getElements();
			
			while(iter.hasNext()){
				JsonNode tweet = iter.next();
				
				String id = tweet.get("id").getValueAsText();
				
				couch.putJson("timeline", id, tweet);
				System.out.println("dumped id:" + id);
			}
			
//			System.out.println(node.toString());
		} catch (CouchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
//	public void dumpTimeline(){
//		
//		JsonNode timelineNode = null;
//		
//		
//	}
	
	public void dumpHashtag(String tag){
		try{
			couch.createNewDatabase(hashtag_db_prefix+"tag");	
		}catch (CouchException ce){
			//ignore
		}
		
		int page = 1;
		while (true){
			try {
				JsonNode jn = twitter.searchHashTag(tag, page);
				
				Iterator<JsonNode> iter = jn.get("results").getElements();
				
				if (! iter.hasNext()){
					break;
				}
				
				while (iter.hasNext()){
					JsonNode tweet = iter.next();
					
					String id = tweet.get("id").getValueAsText();
					
					couch.putJson(hashtag_db_prefix+"tag", id,tweet);
					
					System.out.println(id + " page: " +page);
				}
				
				page++;
			
			}catch (CouchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	
		}
			}
	public void dumpTrends(){
		
		
		JsonNode jn = null;
		try {
		
			jn = twitter.getCurrentTrends();
			couch.putJson("trends",jn);
			System.out.println(jn);
					
		} catch (CouchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


}
