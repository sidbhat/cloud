 
package com.oneapp.cloud.core
import java.util.regex.*
import com.oneapp.cloud.core.log.*

class UtilService {

    static transactional = false

    static String toCamelCase(String text) {
        def r = text.charAt(0).toUpperCase() + text.substring(1).toLowerCase()
        println "UtilService-toCamelCase: "+r
    }
    
  static String convertURLToLink(String msg, boolean showText)
	{
	
		try{
			String str = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>?Â«Â»â€œâ€�â€˜â€™]))";
			Pattern patt = Pattern.compile(str);
			Matcher matcher = patt.matcher(msg);
			def plain
			if(showText){
				if("\$1".indexOf("http") > -1)
				   plain = matcher.replaceAll("<a href=\"\$1\" target=\"_new\">\$1</a>");
				else{
					if(msg.indexOf("https") > -1)
						plain = matcher.replaceAll("<a href=\"https://\$1\" target=\"_new\">\$1</a>");
					else
						plain = matcher.replaceAll("<a href=\"http://\$1\" target=\"_new\">\$1</a>");
				}
			}else
				plain = matcher.replaceAll("<a href=\"\$1\" target=\"_new\">Link</a>");
				if(msg.indexOf("https:") > -1){
					while(plain.indexOf('https://https://')!=-1 ){
						plain = plain.replace('https://https://','https://');
					}
				}else{
					while(plain.indexOf('http://http://')!=-1 ){
						plain = plain.replace('http://http://','http://');
					}
				}
			return plain
		}catch ( Exception e){
			return msg
		}
	
	}
	
	def getURL(String id)
    {
    				try{
						String str = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>?Â«Â»â€œâ€�â€˜â€™]))";
						Pattern patt = Pattern.compile(str);
						def companyActivityInstance = CompanyActivity.get(id)
						def msg
     					if ( companyActivityInstance.feedContent != null ){
     						msg =  companyActivityInstance.feedContent
     					}else 
     						return
						Matcher matcher = patt.matcher(msg);
						def plain = matcher.replaceAll("\$1");
						response.outputStream  plain
					}catch ( Exception e){
						return null
					}
	
    
    }
	
	def appLog(def ip, def forwardURI, def user, def msg, def deviceType)
	{
	
				AppLog.withNewSession {
					    def alog = new com.oneapp.cloud.core.log.AppLog()
			  		    alog.ip = ip
                        alog.uri = forwardURI
                        alog.user = user
                        alog.msgType="E"
                        alog.msg = msg
                 	    alog.deviceType = deviceType
                    	alog.save()
                    }
	
	}
	static String shorten(String longUrl){
   		 def addr = "http://tinyurl.com/api-create.php?url=${longUrl}"
    	 return addr.toURL().text
  	}
    
    

}
