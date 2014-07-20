package rit.contoken;

/** 
 * An object that binded the operation status (databsse query or I/O) and the operation's result (an output or an error message)
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public class StatusMessage {
	/** 
	 * Operation status indicator 
	 * <li><code>succeed == true</code> indicates that the operation was successful
	 * <li><code>succeed == false</code> indicates that the operation was failed
	 **/
	public boolean succeed;
	/** 
	 * Operation result message contains the output or the error message of the operation
	 **/
	public String msg;
	
	/** 
	 * Default Constructor
	 **/
	public StatusMessage(){
		
	}
	
	/** 
	 * Constructor with <code>succeed</code> and <code>msg</code> argument
	 * @param succeed operation status attribute
	 * @param msg operation message attribute
	 **/
	public StatusMessage(boolean succeed,String msg){
		this.succeed = succeed;
		this.msg = msg;
	}
	
	/** 
	 * create JSON message from StatusMessage
	 * <code>status</code> in JSON object is <code>succeed</code> or <code>failed</code>, imply from <code>true</code> or <code>false</code> of <code>succeed</code> attribute
	 * @return JSON string of the StatusMessage
	 * <li>example 1: <code>{status: "succeed", msg:"foo baa"}</code>
	 * <li>example 2: <code>{status: "failed", msg:"foo baa"}</code>
	 **/
	public String toJSON(){
		String status = null;
		if(succeed)
			status = "succeed";
		else status = "failed";
		
		return "{status:\""+status+"\",msg:\""+msg+"\"}";				
	}
}
