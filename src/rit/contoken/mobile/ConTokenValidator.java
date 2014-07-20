package rit.contoken.mobile;

import org.json.JSONException;
import org.json.JSONObject;

import rit.contoken.ConToken;
import rit.contoken.StatusMessage;
import android.content.Context;

/** 
 * Continuous Token Validator
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 002 Sep 7, 2011.
 */
public class ConTokenValidator {
	/**
	 * The length of the Token
	 */
	public static final int TOKEN_LENGTH = 8;
	
	/*
	 * validate the Token
	 * @param deviceconnection initialized <code>DeviceConnection</code> sent from the main <code>Activity</code>
	 * @param devicestorage <code>DeviceStorage</code> sent from the main <code>Activity</code> for the Token string and the previous Reveice Flag storage process
	 * @param uid device_id 
	 * @param androidcontext the <code>Context</code> object of the main <code>Activity</code>
	 * @return <code>StatusMessage</code> indicate the <code>validate</code> operation status (success, failed, error etc...)
	 * <li>if the Token was correct
	 * <br> <code>return: StatusMessage(true,"Validate: Token is correct")</code>
	 
	 * <li>if the Token was incorrect
	 * <br> <code>return: StatusMessage(false,"Validate: Token is incorrect, Require username/password authentication");</code>
	 
	 * <li>if the device is not yet registered to the Continuous Token server (new installation)
	 * <br> <code>return: StatusMessage(false,"Validate: device id is not exist, Require username/password authentication");</code>
	 
	 * <li>if the connection was error
	 * <br> <code>return: StatusMessage(false,"Validate: connection was error")</code>
	 * 
	 * <li>if the response was broken, or have a wrong format (not JSON)
	 * <br> <code>return: StatusMessage(false,"Validate: server response was not correct (JSON message was broken)")</code>
	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	/*public static StatusMessage validate(DeviceConnectionInterface deviceconnection,DeviceStorageInterface devicestorage,String uid,Object androidcontext){
		Context context = (Context) androidcontext;
		
		StatusMessage sm = new StatusMessage();
		boolean rcvFlag = Boolean.parseBoolean(devicestorage.getRcvFlag(context).msg);
		String token = ConToken.gen(devicestorage.getToken(context).msg,genFlag);
		
		sm = deviceconnection.check(uid,token,context); 
		
		if(sm.succeed){
			devicestorage.saveToken(token,genFlag,context);
			try {
				System.out.println(sm.msg);
				JSONObject json = new JSONObject(sm.msg);
				sm.msg = json.getString("msg");
				
				if(sm.msg.contains("duplicate")){
					System.out.println("found duplicate");
					genFlag = true;
					devicestorage.saveGenFlag(genFlag, context);
					//GenFlagTV.setText(devicestorage.getGenFlag(this).msg);
					sm = validate(deviceconnection,devicestorage,uid,context);
				}else if(sm.msg.contains("incorrect")){
					System.out.println("found incorrect");
					sm = new StatusMessage(false,"Validate: Token is incorrect, Require username/password authentication");
					//promptUsername();
				}else if(sm.msg.contains("is not existed")){
					System.out.println("found not existed");
					sm = new StatusMessage(false,"Validate: device id is not exist, Require username/password authentication");
					//promptUsername();
				}else{
					System.out.println("found correct");
					System.out.println(sm.msg);
					genFlag = true;
					devicestorage.saveGenFlag(genFlag, context);
					sm = new StatusMessage(true,"Validate: Token is correct");
					//GenFlagTV.setText(devicestorage.getGenFlag(this).msg);
				}
	
			} catch (JSONException e) {
				e.printStackTrace();
				genFlag = false;
				devicestorage.saveGenFlag(genFlag, context);
				sm = new StatusMessage(false,"Validate: server response was not correct (JSON message was broken)");
				
				//GenFlagTV.setText(devicestorage.getGenFlag(this).msg);
				//StatusTV.setText(sm.msg);
			}
		}else{
			System.out.println("connection error");
			genFlag = false;
			devicestorage.saveGenFlag(genFlag, context);
			sm = new StatusMessage(false,"Validate: connection was error");
			//GenFlagTV.setText(devicestorage.getGenFlag(this).msg);
			//StatusTV.setText(sm.msg);
		}
		return sm;
	}*/
	
	/**
	 * check the Token (no Authentication parameters)
	 * @param uid device_id 
	 * @param androidcontext the <code>Context</code> object of the main <code>Activity</code>
	 * @return <code>StatusMessage</code> indicate the <code>check</code> operation status (success, failed, error etc...) and server response (OK, NG)
	 * <li>if the Token was correct
	 * <br> <code>return: StatusMessage(true,"Check: Token is OK")</code>
	 
	 * <li>if the Token was incorrect
	 * <br> <code>return: StatusMessage(true,"Check: Token is NG, Require username/password authentication");</code>
	 
	 * <li>if the Token storage was not found
	 * <br> <code>return: StatusMessage(true,"Check: Token is NG, Storage was not found");</code>
	 
	 * <li>if there was an error (Internet connection etc.)
	 * <br> <code>return: StatusMessage(false,"Check: [Error Message]")</code>

	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	private static StatusMessage check(String uid,Object androidcontext){
		Context context = (Context) androidcontext;
		
		DeviceStorage devicestorage = new DeviceStorage();
		DeviceConnection deviceconnection = new DeviceConnection();
		
		StatusMessage sm = new StatusMessage();
		boolean rcvFlag = Boolean.parseBoolean(devicestorage.getRcvFlag(context).msg);
		
		sm = devicestorage.getToken(androidcontext);
		//if(!sm.succeed&&sm.msg.contains("FileNotFoundException"))
		//	return new StatusMessage(true,"Check: Token is NG, Storage was not found");
		
		String token = ConToken.gen(sm.msg,rcvFlag);
		
		sm = deviceconnection.check(uid,token,context); 
		
		if(sm.succeed){
			devicestorage.saveToken(token,context);
			try {
				System.out.println(sm.msg);
				JSONObject json = new JSONObject(sm.msg);
				sm.msg = json.getString("msg");
				
				//DUP
				if(sm.msg.contains("duplicate")){
					System.out.println("found duplicate");
					rcvFlag = true;
					devicestorage.saveRcvFlag(rcvFlag, context);
					
					sm = check(uid,context);
				//NG
				}else if(sm.msg.contains("incorrect")){
					System.out.println("found incorrect");
					sm = new StatusMessage(true,"Check: Token is NG, Require username/password authentication");
					//promptUsername();
				}
				//OK
				else{
					System.out.println("found correct");
					System.out.println(sm.msg);
					rcvFlag = true;
					devicestorage.saveRcvFlag(rcvFlag, context);
					sm = new StatusMessage(true,"Check: Token is OK");
					//GenFlagTV.setText(devicestorage.getGenFlag(this).msg);
				}
			//FAILED: JSON Error
			} catch (JSONException e) {
				e.printStackTrace();
				rcvFlag = false;
				devicestorage.saveRcvFlag(rcvFlag, context);
				sm = new StatusMessage(false,"Check: server response was not correct (JSON message was broken)");
				
				//GenFlagTV.setText(devicestorage.getGenFlag(this).msg);
				//StatusTV.setText(sm.msg);
			}
		//FAILED: SND_FLAG = false
		}else{
			System.out.println("connection error");
			rcvFlag = false; 
			devicestorage.saveRcvFlag(rcvFlag, context);
			sm = new StatusMessage(false,"Check: connection was error");
			//GenFlagTV.setText(devicestorage.getGenFlag(this).msg);
			//StatusTV.setText(sm.msg);
		}
		return sm;
	}
	
	/**
	 * Verfy or Register a Token (using <code>check</code> method 1)with or 2)without Authentication parameters)
	 * @param uname Username 
	 * @param pass Password 
	 * @param uid device_id 
	 * @param androidcontext the <code>Context</code> object of the main <code>Activity</code>
	 * @return <code>StatusMessage</code> indicate the <code>check</code> operation status (success, failed, error etc...) and server response (OK, NG, DUP)
	 * <li>1) Verfication Mode (<code>uname==null&&pass==null</code>)
	 * <li>if the Token was correct
	 * <br> <code>return: StatusMessage(true,"Check: Token is OK")</code>
	 
	 * <li>if the Token was incorrect
	 * <br> <code>return: StatusMessage(true,"Check: Token is NG, Require username/password authentication");</code>
	 
	 * <li>if the Token storage was not found
	 * <br> <code>return: StatusMessage(true,"Check: Token is NG, Storage was not found");</code>
	 
	 * <li>if there was an error (Internet connection etc.)
	 * <br> <code>return: StatusMessage(false,"Check: [Error Message]")</code>
	 
	 * <li>2) Authentication Mode (<code>uname!=null||pass!=null</code>)
	 * <li>if the authentication was OK
	 * <br> <code>return: StatusMessage(true,"Check: Authentication is OK, Token was initialized/reset")</code>
	 
	 * <li>if the authentication was NG
	 * <br> <code>return: StatusMessage(true,"Check: Authentication is NG, Require username/password authentication");</code>
	 
	 * <li>if there was an error (Internet connection etc.)
	 * <br> <code>return: StatusMessage(false,"Check: [Error Message]")</code>

	 * @see <a href="http://developer.android.com/reference/android/content/Context.html"><code>android.content.Context</code></a>
	 * @see StatusMessage
	 */
	public static StatusMessage check(String uid,String uname,String pass,Object androidcontext){
		if(uname==null&&pass==null)
			return check(uid,androidcontext);
		
		Context context = (Context) androidcontext;
		DeviceStorage devicestorage = new DeviceStorage();
		DeviceConnection deviceconnection = new DeviceConnection();
		
		String token = ConToken.random(ConTokenValidator.TOKEN_LENGTH);
		StatusMessage sm = deviceconnection.check(uid, token ,uname,pass,context); 
		
		if(sm.succeed){
			devicestorage.saveToken(token,context);
			devicestorage.saveRcvFlag(true, context); 
			try {
				System.out.println(sm.msg);
				JSONObject json = new JSONObject(sm.msg);
				sm.msg = json.getString("msg");
				//NG Authentication failed
				if(sm.msg.contains("NG")){
					System.out.println("Authentication failed");
					sm = new StatusMessage(true,"Check: Authentication is NG, Require username/password authentication");
					//promptUsername();
				//OK
				}else{
					System.out.println("found correct");
					System.out.println(sm.msg);
					//rcvFlag = true;
					//devicestorage.saveGenFlag(genFlag, context);
					sm = new StatusMessage(true,"Check: Authentication is OK, Token was initialized/reset");
					//RcvFlagTV.setText(devicestorage.getGenFlag(this).msg);
				}
			}catch (JSONException e) {
				e.printStackTrace();
				sm = new StatusMessage(false,"Check: server response was not correct (JSON message was broken)");
			}
		//FAILED: SND_FLAG = FALSE
		}else{
			System.out.println("connection error");
			sm = new StatusMessage(false,"Check: connection was error");
		}
		return sm;
	}
}
