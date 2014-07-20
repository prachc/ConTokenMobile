package rit.contoken.mobile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import rit.contoken.DeviceConnectionInterface;
import rit.contoken.DeviceStorageInterface;
import rit.contoken.StatusMessage;
import android.content.Context;
/** 
 * Continuous Token Device Storage (implements <code>rit.contoken.DeviceStorageInterface</code>)
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public class DeviceStorage implements DeviceStorageInterface{
	/**
	 * save the Token to the device storage
	 * @param token the Token to save
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>saveToken</code> operation status (success, failed, error etc...)
	 * <li>if the storage operation was successful
	 * <br> <code>return: StatusMessage(true,DeviceStorage.saveToken() was successful)</code>
	 
	 * <li>if the an exception was found (FileNotFoundException, IOException)
	 * <br> <code>return: StatusMessage(false,"DeviceStorage.saveToken() [Exception Class Name]")</code>
	 
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	@Override
	public StatusMessage saveToken(String token,Object androidcontext) {
		/*if(!saveFlag)
			return new StatusMessage(false,"DeviceStorage.saveToken() saveFlag is false");*/
		StatusMessage sm = new StatusMessage(false,"DeviceStorage.saveToken() initialization");
		String FILENAME = "token_storage";
		
		Context context = (Context) androidcontext;
		
		try {
			FileOutputStream fos;
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(token.getBytes());
			if(fos != null) 
		    	fos.close();
			sm.succeed = true;
			sm.msg = "DeviceStorage.saveToken() was successful";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sm.msg = "DeviceStorage.saveToken() FileNotFoundException";
		} catch (IOException e) {
			e.printStackTrace();
			sm.msg = "DeviceStorage.saveToken() IOException";
		}
		
		return sm;
	}

	/**
	 * get the Token from the device storage
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>getToken</code> operation status (success, failed, error etc...)
	
	 * <li>if the get operation was successful
	 * <br> <code>return: StatusMessage(true,[token])</code>
	 
	 * <li>if the an exception was found (FileNotFoundException, IOException)
	 * <br> <code>return: StatusMessage(false,"")</code>
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	 // <br> <code>return: StatusMessage(false,"DeviceStorage.getToken() [Exception Class Name]")</code>
	@Override
	public StatusMessage getToken(Object androidcontext) {
		StatusMessage sm = new StatusMessage(false,"DeviceStorage.getToken() initialization");
		String FILENAME = "token_storage";
		Context context = (Context) androidcontext;
			
		try {
			FileInputStream fis;
			fis = context.openFileInput(FILENAME);
		    InputStreamReader inputreader = new InputStreamReader(fis);
		    BufferedReader buffreader = new BufferedReader(inputreader);
		    
		    String line;
		    sm.msg = "";
		    while ((line = buffreader.readLine()) != null) {
		        sm.msg += line;
		    }
		    sm.succeed = true;

		    if(fis != null) 
		    	fis.close();
		    if(inputreader != null) 
		    	inputreader.close();
		    if(buffreader != null) 
		    	buffreader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sm.succeed = false;
			sm.msg = "";
			//sm.msg = "DeviceStorage.getToken() FileNotFoundException";
		} catch (IOException e) {
			e.printStackTrace();
			sm.succeed = false;
			sm.msg = "";
			//sm.msg = "DeviceStorage.getToken() IOException";
		}
		return sm;
	}

	/**
	 * get the the previous receive status flag from the device storage
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>getRcvFlag</code> operation status (success, failed, error etc...)
	 * <li>if the get operation was successful
	 * <br> <code>return: StatusMessage(true,[rcvFlag])</code>
	 
	 * <li>if the an exception was found (FileNotFoundException, IOException)
	 * <br> <code>return: StatusMessage(false,"DeviceStorage.getRcvFlag() [Exception Class Name]")</code>
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	@Override
	public StatusMessage getRcvFlag(Object androidcontext) {
		StatusMessage sm = new StatusMessage(false,"DeviceStorage.getRcvFlag() initialization");
		String FILENAME = "rcvflag_storage";
		Context context = (Context) androidcontext;
		
		try {	
			FileInputStream fis;
			fis = context.openFileInput(FILENAME);
		    InputStreamReader inputreader = new InputStreamReader(fis);
		    BufferedReader buffreader = new BufferedReader(inputreader);
		    
		    String line;
		    sm.msg = "";
		    while ((line = buffreader.readLine()) != null) {
		        sm.msg += line;
		    }
		    sm.succeed = true;

		    if(fis != null) 
		    	fis.close();
		    if(inputreader != null) 
		    	inputreader.close();
		    if(buffreader != null) 
		    	buffreader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sm.msg = "DeviceStorage.getRcvFlag() FileNotFoundException";
		} catch (IOException e) {
			e.printStackTrace();
			sm.msg = "DeviceStorage.getRcvFlag() IOException";
		}
		return sm;
	}

	/**
	 * save the previous receive status flag
	 * @param rcvFlag the previous receive status flag to save, <code>rcvFlag</code> is determined by other method that indicate the receive status (<code>DeviceConnectionInterface.check</code>)
	 * @param androidcontext the Android context of the mobile application activity's class (to access the private device storage)
	 * @return <code>StatusMessage</code> indicate the <code>saveRcvFlag</code> operation status (success, failed, error etc...)
	 * <li>if the storage operation was successful
	 * <br> <code>return: StatusMessage(true,DeviceStorage.saveRcvFlag() was successful)</code>
	 	 
	 * <li>if the an exception was found (FileNotFoundException, IOException)
	 * <br> <code>return: StatusMessage(false,"DeviceStorage.saveRcvFlag() [Exception Class Name]")</code>
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 * @see DeviceConnectionInterface
	 */
	@Override
	public StatusMessage saveRcvFlag(boolean genFlag, Object androidcontext) {
		StatusMessage sm = new StatusMessage(false,"DeviceStorage.saveRcvFlag() initialization");
		String FILENAME = "rcvflag_storage";
		
		Context context = (Context) androidcontext;
		
		try {
			FileOutputStream fos;
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(Boolean.toString(genFlag).getBytes());
			fos.close();
			sm.succeed = true;
			sm.msg = "DeviceStorage.saveRcvFlag() was successful";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sm.msg = "DeviceStorage.saveRcvFlag() FileNotFoundException";
		} catch (IOException e) {
			e.printStackTrace();
			sm.msg = "DeviceStorage.saveRcvFlag() IOException";
		}
		
		return sm;
	}

}
