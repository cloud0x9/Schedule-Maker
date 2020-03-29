package ScheduleCreator.API;

/**
 * Makes direct calls to that "YesNoApi" using https calls, and JSON to extract
 * fields.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 3/27/2020
 */
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
//https://github.com/stleary/JSON-java
import org.json.JSONObject;

public class YesNoApi implements ApiInterface {

    //refrence https://yesno.wtf#api for api docs
    //base url for api calls
    private static final String baseURL = "https://yesno.wtf/api/?force={";

    @Override
    public String decisionForce(String _decision) {
        //inline will store the JSON data streamed in string format
        String inline = "";
        //placeholder for answer from the api
        String answer = null;
        try {
            //add a forced decision if applicable
            URL url = new URL(baseURL + _decision + "}");

            //set protocols being used, I ran into some issues and added this, but currently not needed
            //System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
            //Pass url so the connection can be set up
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            //Set the request for api to GET
            connection.setRequestMethod("GET");
            //make this connection to the api website
            connection.connect();
            //Get the response status of the  API, used to failsafe
            int responsecode = connection.getResponseCode();
            System.out.println("Response code is: " + responsecode);

            /**
             * If response code is not 200, something went wrong, throw
             * Exception and fail safely else get the JSON from the API and
             * extract necessary fields
             */
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                //Read JSON from API website
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                }
                //Create a JSON object from the string from the website
                JSONObject jsonObject = new JSONObject(inline);
                //Get nesseary feild from the JSON, and cast back into a String
                String decision = (String) jsonObject.get("answer");
                answer = decision;
                //Close the stream when reading the data has been finished
                sc.close();
            }
            //Disconnect the HttpURLConnection stream
            connection.disconnect();
        } catch (IOException | RuntimeException e) {
        }
        //return the answer that the API gave
        return answer;

    }

}
