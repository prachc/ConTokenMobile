package rit.contoken;

/** 
 * Continuous Token Device Connection Interface (require an implementation)
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public interface DeviceConnectionInterface {
	/*
	 * check whether the device is connected to the Internet
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>checkconnection</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	//public StatusMessage checkconnection(Object androidcontext);
	/**
	 * check the Token by sending device id (<code>uid</code>) and the Token (<code>token</code>) to the Continuous Token Server via the internet connection
	 * @param uid user id or device id
	 * @param token the Token to check
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>checktoken</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	public StatusMessage check(String uid, String token, Object androidcontext);
	/**
	 * register a new device id (<code>uid</code>) and the Token (<code>token</code>) to the Continuous Token Server via the internet connection
	 * @param uid user id or device id
	 * @param token the Token to register
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>registeruser</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	public StatusMessage check(String uname,String pass,String uid, String token, Object androidcontext);
	/*
	 * connect to the <code>url</code> and get the output message (via unsecured GET method)
	 * @deprecated This is get method, please use <code>sslpost</code> instead
	 * @param url the url to connect
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>connect</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	//public StatusMessage connect(String url,Object androidcontext);
	/*
	 * connect to the <code>url</code> and get the output message (via secured POST method)
	 * @param url the url to connect
	 * @param uid user id or device id
	 * @param token the Token to check
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>sslpost</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */	
	//public StatusMessage sslpost(String url,String uid, String token,Object androidcontext);
	
	/*
	 * connect to the <code>url</code> and get the output message (via secured POST method)
	 * @param url the url to connect
	 * @param uid user id or device id
	 * @param token the Token to check
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>sslpost</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */	
	//public StatusMessage sslpost(String url,String uname,String pass,String uid, String token,Object androidcontext);
}
