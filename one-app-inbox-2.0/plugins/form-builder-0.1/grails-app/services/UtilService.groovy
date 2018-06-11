 import java.util.regex.*

class UtilService {

    static transactional = false

	static String shorten(String longUrl){
   		 def addr = "http://tinyurl.com/api-create.php?url=${longUrl}"
    	 return addr.toURL().text
  	}
    
    

}
