package ScheduleCreator.API;

/**
 * Makes direct calls to the MailJet API.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 3/29/2020
 */
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class EmailAPI implements APIInterface {

    private static final String apiPublicKey = "4ed58dd4eac88294f8decd6ebcb37802";
    private static final String apiPrivateKey = "2ebe9765bf49eeac4c25fa0436edbcbf";
    private static final String clientVersion = "v3.1";
    private static final String sendEmail = "ScheduleCreatorUNCG@gmail.com";
    public static final Pattern validEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Send email's using an API.
     *
     * @param _email The email that is being sent to, this comes from the user.
     * @param _message This is what is being sent in the email.
     * @return
     * @throws MailjetException
     * @throws MailjetSocketTimeoutException
     */
    @Override
    public void sendEmail(String _email, String _message) throws MailjetException, MailjetSocketTimeoutException {
        //Check if the email given by user is valid, if so then the api call is made.
        if(validate(_email)){
        //Initalize the API requirements
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        //Create a new client to send emails, using public and private api keys, and speify which version of the client is being used
        client = new MailjetClient((apiPublicKey), (apiPrivateKey), new ClientOptions(clientVersion));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                //The email that is being used to send, this is controled from the mailjet webiste
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", sendEmail)
                                        .put("Name", "Schedule Creator"))
                                //The email that is being send to
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", _email)
                                                .put("Name", "Student")))
                                //Subject line of email
                                .put(Emailv31.Message.SUBJECT, "Schedule Creator")
                                //Body of email
                                .put(Emailv31.Message.TEXTPART, _message)));
        response = client.post(request);

        //Print out what email was just sent, it was sent or not, and any errors from the API
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
        else{
          //if the email is INVALID then the api call is not made.
          System.out.println("\""+_email +"\""+ " is NOT a valid email....."+ "\n" +"Email was not sent, the email given is invalid.");

        }
    }
        //Suporting method to "sendEmail", to validate emails before a api call is made.
        public static boolean validate(String _email) {
        Matcher matcher = validEmail.matcher(_email);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}
