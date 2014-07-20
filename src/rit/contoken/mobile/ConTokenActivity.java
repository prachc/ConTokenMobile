package rit.contoken.mobile;

import org.json.JSONObject;

import rit.contoken.StatusMessage;
import rit.mobile.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The mobile client prototype of the Continuous Token system
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public class ConTokenActivity extends Activity {
	/**
	 * A <code>TextView</code> to display status
	 */
	public TextView StatusTV;
	/**
	 * A <code>TextView</code> to display Token Storage (<code>DeviceStorage.getToken(this)</code>)
	 */
	public TextView TokenStorageTV;
	/**
	 * A <code>TextView</code> to display the Previous Receive Status Flag (<code>DeviceStorage.getRcvFlag(this)</code>)
	 */
	public TextView RcvFlagTV;
	/**
	 * an object for internet connection (verify signal status, send status, receive status)
	 */
	public DeviceConnection deviceconnection = new DeviceConnection();
	/**
	 * an object to parse JSON message returned from the server
	 */
	public JSONObject json;
	/**
	 * an object for private device storage (save and get <code>token</code> or <code>rcvFlag</code>)
	 */
	public DeviceStorage devicestorage = new DeviceStorage();
	/**
	 * a temporary <code>StatusMessage</code> object
	 */
	public StatusMessage sm;
	/**
	 * the current Token (before it is sent or stored) 
	 */
	public String token;
	/**
	 * the device id (statically assigned in the test)
	 */
	public String uid = "001";
	/**
	 * user name (when the Token reset operation is required) (may link to Rakuten account username)
	 */
	public String uname;
	/**
	 * password (when the Token reset operation is required) (may link to Rakuten account password)
	 */
	public String passwd;
	/**
	 * the current Receive Status Flag (before it is stored) 
	 */
	public boolean rcvFlag = true;
	/**
	 * the current connection status (simulation only) 
	 */
	public boolean conFlag = true;
	/** 
	 * Android Override: Called when the activity is first created.
	 * <br><li> initialize <code>TokenStorageTV</code>,<code>RcvFlagTV</code>,<code>StatusTV</code>
	 * <br><li> <code>TokenStorageTV</code> is set to <code>devicestorage.getToken(this).msg</code>
	 * <br><li> <code>RcvFlagTV</code> is set to <code>devicestorage.getRcvFlag(this).msg</code>
	 * <br><li> if there is no Token in the device storage
	 * <br> call <code>promptUsername()</code> to request username/password authentication and reset the Token 
	 **/ 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TokenStorageTV = (TextView) findViewById(R.id.token_storage); 
		RcvFlagTV = (TextView) findViewById(R.id.rcvflag); 
		StatusTV = (TextView) findViewById(R.id.status); 
		
		TokenStorageTV.setText(devicestorage.getToken(this).msg);
		RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
	}
	/** 
	 * Android Override
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.emu_menu, menu);
		return true;
	}
	
	/** 
	 * check Token by connecting to the Continuous server using <code>ConTokenValidator.check(uid,context)</code>
	 * <br> will prompt for Username and Password if <code>ConTokenValidator.check(uid,context)</code> return <code>StatusMessage(true,"/*sometext/NG/*sometext/")</code>
	 */
	public void check(){
		//deviceconnection.setIPAddress("10.0.2.2");
		//deviceconnection.setHTTP_PORT(8080);
		//deviceconnection.setHTTPS_PORT(8443);
		
		sm = ConTokenValidator.check(uid,null,null,this);
		StatusTV.setText(sm.msg);
		if(sm.succeed&&sm.msg.contains("NG"))
			promptUsername();
		refresh();
	}
	
	/*public void connect(){
		//devicestorage.saveToken("myHuhOwa", this);
		//devicestorage.saveRcvFlag(true, this);
		//sm = devicestorage.getToken(this);
		//TokenStorageTV.setText(devicestorage.getToken(this).msg);
		TokenStorageTV.setText(devicestorage.getToken(this).msg);
		
		rcvFlag = Boolean.parseBoolean(devicestorage.getRcvFlag(this).msg);
		token = ConToken.gen(devicestorage.getToken(this).msg,rcvFlag);
		
		//TokenTV.setText(token);
		deviceconnection.setIPAddress("10.0.2.2");
		deviceconnection.setHTTP_PORT(8080);
		deviceconnection.setHTTPS_PORT(8443);
		sm = deviceconnection.checktoken(uid,token,this); 
		
		if(sm.succeed){
			devicestorage.saveToken(token,rcvFlag,this);
			try {
				System.out.println(sm.msg);
				json = new JSONObject(sm.msg);
				StatusTV.setText(json.getString("msg"));
				
				if(sm.msg.contains("duplicate")){
					System.out.println("found duplicate");
					rcvFlag = true;
					devicestorage.saveRcvFlag(rcvFlag, this);
					RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
					connect2();
				}else if(sm.msg.contains("incorrect")){
					System.out.println("found incorrect");
					promptUsername();
				}else if(sm.msg.contains("is not existed")){
					System.out.println("found not existed");
					promptUsername();
				}else{
					System.out.println("found correct");
					System.out.println(sm.msg);
					rcvFlag = true;
					devicestorage.saveRcvFlag(rcvFlag, this);
					RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
				}
	
			} catch (JSONException e) {
				e.printStackTrace();
				sm.msg = "Problem: cannot pase the server response";
				rcvFlag = false;
				devicestorage.saveRcvFlag(rcvFlag, this);
				RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
				StatusTV.setText(sm.msg);
			}
		}else{
			System.out.println("connection error");
			rcvFlag = false;
			devicestorage.saveRcvFlag(rcvFlag, this);
			RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
			StatusTV.setText(sm.msg);
		}
	}*/
	
	/**
	 * Android Override: initialize the option menu (Menu--><code>Options</code>)according to the device storage status
	 * <br><li> the <code>ConFlag</code> radio button is selected if it's value is <code>true</code> (the connection status)
	 * <br><li> the <code>RcvFlag</code> radio button is selected if it's value is <code>true</code> (the previous receive status flag)
	 * <br><li> the <code>token_storage</code> radio button is selected if the file named <code>token_storage</code> is existed
	 * <br><li> the <code>rcvflag_storage</code> radio button is selected if the file named <code>rcvflag_storage</code> is existed
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    
	    menu.findItem(R.id.conflag).setChecked(conFlag);
	    
	    StatusMessage sm = devicestorage.getToken(this);
	    menu.findItem(R.id.file1).setChecked(sm.succeed);

		sm = devicestorage.getRcvFlag(this);
		menu.findItem(R.id.file2).setChecked(sm.succeed);

		if(sm.msg.equals("true")){
			menu.findItem(R.id.rcvflag).setChecked(true);
		}else{
			menu.findItem(R.id.rcvflag).setChecked(false);
		}

		return true;
	}

	/**
	 * Android Override: define an action when the menu element is clicked
	 * <br><li> click Menu--><code>Connect</code> to check the current Token via the server communication
	 * <br><li> click Menu--><code>Options</code>--><code>ConFlag</code> to override the value of the connection status
	 * <br><li> click Menu--><code>Options</code>--><code>RcvFlag</code> to override the value of the previous receive status flag (<code>rcvFlag</code>)
	 * <br><li> click Menu--><code>Options</code>--><code>token_storage</code> create/delete the file named <code>token_storage</code>
	 * <br> enter the new <code>token</code> (<code>String</code>) as the method <code>promptInput()</code> is called
	 * <br><li> click Menu--><code>Options</code>--><code>rcvflag_storage</code> create/delete the file named <code>rcvflag_storage</code>
	 * <br> enter the new <code>rcvFlag</code> (<code>true</code> or <code>false</code>) as the method <code>promptTF()</code> is called
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.connect:
			check();
			return true;
		case R.id.conflag:
			if (item.isChecked()) {
				conFlag = false;
				item.setChecked(false);
			}else {
				conFlag = true;
				item.setChecked(true);
			}
			return true;
		case R.id.rcvflag:
			if (item.isChecked()) {
				rcvFlag = false;
				item.setChecked(false);
				devicestorage.saveRcvFlag(false, this);
				RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
			}else{ 
				item.setChecked(true);
				rcvFlag = true;
				devicestorage.saveRcvFlag(true, this);
				RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
			}
			return true;
		case R.id.file1:
			if (item.isChecked()){
				item.setChecked(!deleteFile("token_storage"));
				TokenStorageTV.setText("NULL");
				StatusTV.setText("NULL");
			}else{
				deleteFile("token_storage");
				promptInput(); 
			
				item.setChecked(true);
			}
			return true;
		case R.id.file2:
			if (item.isChecked()){ 
				item.setChecked(!deleteFile("rcvflag_storage"));
				RcvFlagTV.setText("NULL");
			}else{
				deleteFile("rcvflag_storage");
				devicestorage.saveRcvFlag(true, this);
				item.setChecked(rcvFlag);
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * prompt the user with the Token Input Box (to override the value of <code>token</code>)
	 */
	public void promptInput(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Token Override");
		alert.setMessage("Please enter a Token");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				token = input.getText().toString();
				overridesavetoken();
				refresh();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				token = null;
				refresh();
			}
		});

		alert.show();
	}
	
	/**
	 * prompt the user with the Username Input Box (for username/password authentication process)
	 */
	public void promptUsername(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Username");
		alert.setMessage("Please enter your username");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				uname = input.getText().toString();
				promptPassword();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				uname = null;
			}
		});

		alert.show();
	}
	
	/**
	 * prompt the user with the Password Input Box (for username/password authentication process)
	 */
	public void promptPassword(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Password");
		alert.setMessage("Please enter your username");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setTransformationMethod(PasswordTransformationMethod.getInstance());
		alert.setView(input);

		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				passwd = input.getText().toString();
				checkAuthen();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				passwd = null;
			}
		});

		alert.show();
	}

	/**
	 * update the data in all display elements (<code>TextBox</code>s)
	 */
	public void refresh(){
		rcvFlag = Boolean.parseBoolean(devicestorage.getRcvFlag(this).msg);
		RcvFlagTV.setText(devicestorage.getRcvFlag(this).msg);
		
		token = devicestorage.getToken(this).msg;
		TokenStorageTV.setText(token);
	}
	
	private void overridesavetoken(){
		devicestorage.saveToken(token, this);
	}
	
	/** 
	 * check username/password by connecting to the Continuous server using <code>ConTokenValidator.check(uid,uname,passwd,context)</code>
	 * <br> will prompt for Username and Password again if <code>ConTokenValidator.check(uid,context)</code> return <code>StatusMessage(true,"/*sometext/NG/*sometext/")</code>
	 */
	private void checkAuthen(){
		sm = ConTokenValidator.check(uid,uname,passwd,this);
		StatusTV.setText(sm.msg);
		if(sm.succeed&&sm.msg.contains("NG")){
			Toast.makeText(this, "Username or Password incorrect", Toast.LENGTH_SHORT).show();
			promptUsername();
		}
		refresh();
	}
}