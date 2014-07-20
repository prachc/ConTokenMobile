package rit.contoken;

/** 
 * Continuous Token Database Interface (require an implementation)
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public interface ConTokenDBInterface {
	/** 
	 * check whether there is the user or device id (<code>uid</code>) in the database
	 * @param uid user or device id
	 * @return StatusMessage <code>StatusMessage</code> indicate the database operation status (success, failed, error etc...)
	 * @see StatusMessage
	 */
	public StatusMessage isExist(String uid);
	/** 
	 * get the Token of <code>uid</code> in the database
	 * @param uid user or device id
	 * @return StatusMessage <code>StatusMessage</code> indicate output or the database operation status (success, failed, error etc...)
	 * @see StatusMessage
	 */
	public StatusMessage get(String uid);
	/** 
	 * insert a new token of <code>uid</code> in the database
	 * @param uid user or device id
	 * @return StatusMessage <code>StatusMessage</code> indicate the database operation status (success, failed, error etc...)
	 * @see StatusMessage
	 */
	public StatusMessage insert(String uid, String key);
	/** 
	 * update a new token of <code>uid</code> in the database
	 * @param uid user or device id
	 * @return StatusMessage <code>StatusMessage</code> indicate the database operation status (success, failed, error etc...)
	 * @see StatusMessage
	 */
	public StatusMessage update(String uid, String key);
	/** 
	 * increment the duplication count of <code>uid</code> in the database
	 * @param uid user or device id
	 * @return StatusMessage <code>StatusMessage</code> indicate the database operation status (success, failed, error etc...)
	 * @see StatusMessage
	 */
	public StatusMessage incDup(String uid);
}
