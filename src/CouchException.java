

public class CouchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7869612935623079599L;
	
	
	int status;
	String message;
	
	public CouchException(int status, String message){
		this.status = status;
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public int getStatus(){
		return status;
	}
	
	@Override
	public String toString() {
		return status +" : " + message;
	}
}
