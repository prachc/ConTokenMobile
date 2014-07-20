package rit.contoken;
/** 
 * Continuous Token Device Storage Interface (require an implementation)
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public interface DeviceStorageInterface {
	/**
	 * save the Token to the device storage
	 * @param token the Token to save
	 
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>saveToken</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	 /* @param SaveFlag use <code>true</code> to save, <code>false</code> to abandon (this parameter is determined by other method that indicate the device data sending status)*/
	public StatusMessage saveToken(String token, Object androidcontext);
	/**
	 * get the Token from the device storage
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>getToken</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	public StatusMessage getToken(Object androidcontext);
	/**
	 * save the previous receive status flag
	 * @param rcvFlag the previous receive status flag to save, <code>rcvFlag</code> is determined by other method that indicate the receive status (<code>DeviceConnectionInterface.check</code>)
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>saveRcvFlag</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 * @see DeviceConnectionInterface
	 */
	public StatusMessage saveRcvFlag(boolean rcvFlag, Object androidcontext);
	/**
	 * get the the previous receive status flag from the device storage
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>getRcvFlag</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	public StatusMessage getRcvFlag(Object androidcontext);
}
