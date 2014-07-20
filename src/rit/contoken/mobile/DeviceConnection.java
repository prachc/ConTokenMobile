package rit.contoken.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import rit.contoken.DeviceConnectionInterface;
import rit.contoken.StatusMessage;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** 
 * Continuous Token Device Connection (implements <code>rit.contoken.DeviceConnectionInterface</code>)
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public class DeviceConnection implements DeviceConnectionInterface{
	/**
	 * The Continuous Server HTTP port number (in <code>http://[ip]:[port]/[context_path]</code> format)
	 */
	public int HTTP_PORT = 8080;
	/**
	 * The Continuous Server HTTPS port number (in <code>http://[ip]:[port]/[context_path]</code> format)
	 */
	public int HTTPS_PORT = 8443;
	/**
	 * The Continuous Server IP address (in <code>http://[ip]:[port]/[context_path]</code> format)
	 */
	public String IP_ADDRESS = "10.0.2.2";
	/**
	 * Context path of the server (in <code>http://[ip]:[port]/[context_path]</code> format)
	 */
	public String CONTEXT_PATH = "contoken/check";
	/**
	 * Connection timeout in millisecond (default = 25000 ms)
	 */
	public int TIMEOUT = 25000;
	
	/*
	 * connect to the <code>url</code> and get the output message (via unsecured GET method)
	 * @param url the url to connect
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicates the <code>connect</code> operation status (success, failed, error etc...)
	 * <li>if the connection was successful
	 * <br> <code>return: StatusMessage(true,[message from the server])</code>
	 
	 * <li>if the connection was unavailable (no signal or no internet connection)
	 * <br> <code>return: StatusMessage(false,"checkconnection: Network Connection is Unavailable")</code>
	 
	 * <li>if the connection was error
	 * <br> <code>return: StatusMessage(false,"connect: Message is sent/No Response")</code>
	 
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	/*@Override
	public StatusMessage connect(String url, Object androidcontext) {
		System.out.println(url);
		
		StatusMessage conflag = checkconnection(androidcontext);
		
		if(!conflag.succeed)
			return conflag;
			
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
		HttpResponse response = null;
		
		try {
			response = httpClient.execute(new HttpGet(url));
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line, result = "";
	        while ((line = br.readLine()) != null)
	            result += line;
	        conflag.msg = result;
		}catch (Exception e) {
			System.out.println("connection error");
			//e.printStackTrace();
			conflag.succeed = false;
			conflag.msg = "connect: Message is sent/No Response"; 
			return conflag;
		}
		
		return conflag; 
	}*/
	
	/**
	 * connect to the <code>url</code> and get the output message (via secured POST method)
	 * @param uid user id or device id
	 * @param token the Token to register
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>sslpost</code> operation status (success, failed, error etc...)
	 * <li>if the connection was successful
	 * <br> <code>return: StatusMessage(true,[message from the server])</code>
	 
	 * <li>if the connection was unavailable (no signal or no internet connection)
	 * <br> <code>return: StatusMessage(false,"Check: Network Connection is Unavailable")</code>
	 
	 * <li>if the connection was error
	 * <br> <code>return: StatusMessage(false,"Check: Message is sent/No Response")</code>
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */	
	@Override
	public StatusMessage check(String uid, String token, Object androidcontext) {
		System.out.println("check:url="+"https://"+IP_ADDRESS+":"+HTTPS_PORT+"/"+CONTEXT_PATH);
		System.out.println("check:uid="+uid);
		System.out.println("check:token="+token);
		
		StatusMessage conflag = checkconnection(androidcontext);
		
		if(!conflag.succeed)
			return conflag;
		
		HttpClient httpClient = getCustomedHttpClient();
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
		HttpResponse response = null;
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("uid", uid));    
		nameValuePairs.add(new BasicNameValuePair("key", token)); 

		
		try {
			HttpPost httppost = new HttpPost("https://"+IP_ADDRESS+":"+HTTPS_PORT+"/"+CONTEXT_PATH);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpClient.execute(httppost);

			//response = httpClient.execute(new HttpGet(url));
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line, result = "";
	        while ((line = br.readLine()) != null)
	            result += line;
	        conflag.msg = result;
		}catch (Exception e) {
			System.out.println("connection error");
			//e.printStackTrace();
			conflag.succeed = false;
			conflag.msg = "check: Message is sent/No Response"; 
			return conflag;
		}
		
		return conflag;
	}
	
	/**
	 * connect to the <code>url</code> and get the output message (via secured POST method)
	 * @param uid user id or device id
	 * @param token the Token to register
	 * @param uname Authentication username
	 * @param pass Authentication password
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>sslpost</code> operation status (success, failed, error etc...)
	 * <li>if the connection was successful
	 * <br> <code>return: StatusMessage(true,[message from the server])</code>
	 
	 * <li>if the connection was unavailable (no signal or no internet connection)
	 * <br> <code>return: StatusMessage(false,"Check: Network Connection is Unavailable")</code>
	 
	 * <li>if the connection was error
	 * <br> <code>return: StatusMessage(false,"Check: Message is sent/No Response")</code>
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	@Override
	public StatusMessage check(String uid, String token, String uname, String pass, Object androidcontext) {
		System.out.println("check:url="+"https://"+IP_ADDRESS+":"+HTTPS_PORT+"/"+CONTEXT_PATH);
		
		System.out.println("check:uid="+uid);
		System.out.println("check:token="+token);
		System.out.println("check:uname="+uname);
		System.out.println("check:pass="+pass);
		
		StatusMessage snd_status = checkconnection(androidcontext);
		
		if(!snd_status.succeed)
			return snd_status;
		
		StatusMessage rcv_status = new StatusMessage();
		
		HttpClient httpClient = getCustomedHttpClient();
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
		HttpResponse response = null;
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("key", token)); 
		nameValuePairs.add(new BasicNameValuePair("uid", uid)); 
		nameValuePairs.add(new BasicNameValuePair("uname", uname));
		nameValuePairs.add(new BasicNameValuePair("pass", pass));
		   
		try {
			HttpPost httppost = new HttpPost("https://"+IP_ADDRESS+":"+HTTPS_PORT+"/"+CONTEXT_PATH);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpClient.execute(httppost);

			//response = httpClient.execute(new HttpGet(url));
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line, result = "";
	        while ((line = br.readLine()) != null)
	            result += line;
	        rcv_status.msg = result;
		}catch (Exception e) {
			System.out.println("connection error");
			//e.printStackTrace();
			rcv_status.succeed = false;
			rcv_status.msg = "Check: Message is sent/No Response"; 
			return rcv_status;
		}
		
		return rcv_status;
	}

	/**
	 * check whether the device is connected to the Internet
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>checkconnection</code> operation status (success, failed, error etc...)
	 * <li>if the connection was available
	 * <br> <code>return: StatusMessage(true,"checkconnection: Network Connection is OK")</code>
	  
	 * <li>if the connection was unavailable (no signal or no internet connection)
	 * <br> <code>return: StatusMessage(false,"checkconnection: Network Connection is Unavailable")</code>
	 
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	
	private StatusMessage checkconnection(Object androidcontext) {
		Context context = (Context)androidcontext;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return new StatusMessage(true,"Check: Network Connection is OK");
		}else
			return new StatusMessage(false,"Check: Network Connection is Unavailable");
	}

	/*
	 * check the Token by sending device id (<code>uid</code>) and the Token (<code>token</code>) to the Continuous Token Server via the internet connection
	 * @param uid user id or device id
	 * @param token the Token to check
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>checktoken</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	/*@Override
	public StatusMessage checktoken(String uid, String token,Object androidcontext) {
		//return connect("http://"+IP_ADDRESS+":"+HTTP_PORT+"/contoken/check?uid="+uid+"&key="+token,androidcontext);
		return sslpost("https://"+IP_ADDRESS+":"+HTTPS_PORT+"/contoken/check",uid,token,androidcontext);
		//return sslpost("http://"+IP_ADDRESS+":"+HTTP_PORT+"/contoken/check",uid,token,androidcontext);
	}*/

	/*
	 * register a new device id (<code>uid</code>) and the Token (<code>token</code>) to the Continuous Token Server via the internet connection
	 * @param uid user id or device id
	 * @param token the Token to register
	 * @param androidcontext the Android context of the mobile application activity's class (to access the device connection status)
	 * @return <code>StatusMessage</code> indicate the <code>registeruser</code> operation status (success, failed, error etc...)
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	/*@Override
	public StatusMessage registeruser(String uid, String token,Object androidcontext) {
		//return connect("http://"+IP_ADDRESS+":"+HTTP_PORT+"/contoken/regis?uid="+uid+"&key="+token,androidcontext);
		return sslpost("https://"+IP_ADDRESS+":"+HTTPS_PORT+"/contoken/regis",uid,token,androidcontext);
		//return sslpost("http://"+IP_ADDRESS+":"+HTTP_PORT+"/contoken/regis",uid,token,androidcontext);
	}*/
	
	/**
	 * set the Continous Server IP Address (in <code>http://[ip]:[port]/[context_path]</code> format)
	 * @param ip the Continous Server IP
	 */
	public void setIPAddress(String ip){
		this.IP_ADDRESS = ip;
	}
	
	/**
	 * set Connection timeout in millisecond
	 * @param timeout timeout in millisecond
	 */
	public void setTimeout(int timeout){
		this.TIMEOUT = timeout;
	}
	
	/**
	 * set HTTP port number
	 * @param httpport HTTP port number (in <code>http://[ip]:[port]/[context_path]</code> format)
	 */
	public void setHTTP_PORT(int httpport) {
		HTTP_PORT = httpport;
	}
	
	/**
	 * set HTTPS port number
	 * @param httpsport HTTPS port number (in <code>http://[ip]:[port]/[context_path]</code> format)
	 */
	public void setHTTPS_PORT(int httpsport) {
		HTTPS_PORT = httpsport;
	}
	
	/**
	 * set context path
	 * @param contextpath context path of the server (in <code>http://[ip]:[port]/[context_path]</code> format)
	 */
	public void setContextPath(String contextpath) {
		CONTEXT_PATH = contextpath;
	}
	
	/**
	 * Retrieve the customized HttpClient object (for HTTPS connections) 
	 * @return customized HttpClient (casted from {@link DefaultHttpClient})
	 * @see DefaultHttpClient
	 */
	private HttpClient getCustomedHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);
	    	/*KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());        
	    	FileInputStream instream = new FileInputStream(new File(".keystore")); 
	    	try {
	    	    trustStore.load(instream, "changeit".toCharArray());
	    	} finally {
	    	    instream.close();
	    	}*/

	        SSLSocketFactory sf = new ConTokenSSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
	        registry.register(new Scheme("https", sf, HTTPS_PORT));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	
	/**
	 * Socket Certification
	 * @author Prach CHAISATIEN
	 *
	 */
	private class ConTokenSSLSocketFactory extends SSLSocketFactory {
	    SSLContext sslContext = SSLContext.getInstance("TLS");

	    public ConTokenSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	        super(truststore);

	        TrustManager tm = new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub
					
				}
	        };

	        sslContext.init(null, new TrustManager[] { tm }, null);
	    }

	    @Override
	    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	    }

	    @Override
	    public Socket createSocket() throws IOException {
	        return sslContext.getSocketFactory().createSocket();
	    }
	}
	

	
}
