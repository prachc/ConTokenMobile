package rit.contoken;

/**
 * Rakuten Username and Password Authentication Database Interface (require an implementation)
 * @author Prach CHAISATIEN
 * @version 1.0 Build 001 Sep 7, 2011.
 */
public interface AuthenDBInterface {
	/** 
	 * verify username and password in the Authentication database
	 * @param uname Username
	 * @param pass Password
	 * @return StatusMessage <code>StatusMessage</code> indicate the result of username and password authentication process
	 * @see StatusMessage
	 */
	public StatusMessage checkAuthentication(String uname,String pass);
}
