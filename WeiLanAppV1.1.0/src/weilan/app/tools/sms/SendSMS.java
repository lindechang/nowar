package weilan.app.tools.sms;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.telephony.SmsManager;

//import android.widget.Toast;
//短信发送查询目标地理位置


public class SendSMS {
	
	public SendSMS(String phoneNumber, String message){
		//super();
		    	SmsManager sms = SmsManager.getDefault();
		    	if (message.length() > 70) {
		    		List<String> msgs = sms.divideMessage(message);
		    		for (String msg : msgs) {
		    			sms.sendTextMessage(phoneNumber, null, msg, null, null);
		    		}
		    	} else {
		    		sms.sendTextMessage(phoneNumber, null, message, null, null);
		    	}
	}

public boolean validate(String telNo, String content){
    	
    	if((null==telNo)||("".equals(telNo.trim()))){
    		//Toast.makeText(this, "请输入手机号码.!",Toast.LENGTH_LONG).show();
    		return false;
    	}
    	if(!checkTelNo(telNo)){
    		//Toast.makeText(this, "请输入正确的手机号.!",Toast.LENGTH_LONG).show();
    		return false;
    	}
    	if((null==content)||("".equals(content.trim()))){
    		//Toast.makeText(this, "请输入手机内容!",Toast.LENGTH_LONG).show();
    		return false;
    	}
    	return true;
    }

/** 
 * 判别手机是否为正确手机号码； 
*号码段分配如下： 
*移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188  
*联通：130、131、132、152、155、156、185、186  
*电信：133、153、180、189、（1349卫通） 
 */ 
private  boolean checkTelNo(String telNo) {
	// TODO Auto-generated method stub
	Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");   
			//.compile("^((13[0-9])|(15[^4,//D])|(18[0-9]))\\d{8}$");   
		       //.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");  
		     Matcher m = p.matcher(telNo); 
		     System.out.println("_________telNo" +telNo);
		     System.out.println("_________m" +m);
		     System.out.println("_________m.matches()" +m.matches());
		     return m.matches(); 
	
}

public void SendSMS(String phoneNumber, String message) {
	// TODO Auto-generated method stub
	SmsManager sms = SmsManager.getDefault();
	if (message.length() > 70) {
		List<String> msgs = sms.divideMessage(message);
		for (String msg : msgs) {
			sms.sendTextMessage(phoneNumber, null, msg, null, null);
		}
	} else {
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
	
}

//public void SendSMS(String telephone_number, String telephone_maseger) {
//	// TODO Auto-generated method stub
//	
//}

}

