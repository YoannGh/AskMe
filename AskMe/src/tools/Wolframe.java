package tools;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;



public class Wolframe {

	// PUT YOUR APPID HERE:
    private static String appid = "XXXXX";

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
        	WAQueryResult queryResult = engine.performQuery(query);
            
        	// Catch errors
            if (queryResult.isError()) {
                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");
            }
            // If everything works
            else {
                System.out.println("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                    	
                        System.out.println(pod.getTitle());
                        System.out.println("------------");
                        
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                            	
                                if (element instanceof WAPlainText) {
                                    System.out.println(((WAPlainText) element).getText());
                                    System.out.println("");
                                }
                                
                            }
                        }
                        
                        System.out.println("");
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
    }
	
}
