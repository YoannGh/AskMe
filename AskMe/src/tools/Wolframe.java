package tools;

import android.util.Log;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;




public class Wolframe {

	// PUT YOUR APPID HERE:
    private static String appid = "W82RQ4-VW5975UY64";
    private static WAQueryResult lastQueryResult;
    
    public static void request(String req) {
        
        // The WAEngine is a factory for creating WAQuery objects,
        // and it also used to perform those queries. You can set properties of
        // the WAEngine (such as the desired API output format types) that will
        // be inherited by all WAQuery objects created from it. Most applications
        // will only need to crete one WAEngine object, which is used throughout
        // the life of the application.
      
    	// This is the object to use WolframeAlpha
    	WAEngine engine = new WAEngine();
        
        engine.setAppID(appid);
        engine.addFormat("plaintext");

        WAQuery query = engine.createQuery();
        
        // Set properties of the query.
        query.setInput(req);
        
        try {
            // Execute query
        	Log.d("Wolframe", "querying : " + req);
        	lastQueryResult = engine.performQuery(query);
        } catch (WAException e) {
            e.printStackTrace();
        }
    }
    
    public static String getQueryResult(){
    	String ret = new String();
    	// Catch errors
        if (lastQueryResult.isError()) {
            ret += "Query error";
            ret +="  error code: " + lastQueryResult.getErrorCode();
            ret +="  error message: " + lastQueryResult.getErrorMessage().toString();
        } else if (!lastQueryResult.isSuccess()) {
        	ret += "Query was not understood; no results available.";
        }
        // If everything works
        else {
        	ret += "Successful query. Pods follow:\n";
            for (WAPod pod : lastQueryResult.getPods()) {
                if (!pod.isError()) {
                	
                	ret += pod.getTitle();
                	ret += "------------";
                    
                    for (WASubpod subpod : pod.getSubpods()) {
                        for (Object element : subpod.getContents()) {
                        	
                            if (element instanceof WAPlainText) {
                            	ret += ((WAPlainText) element).getText();
                            	ret += "";
                            }
                            
                        }
                    }
                    
                    ret += "";
                }
            }
        }
        return ret;
    }
	
}
