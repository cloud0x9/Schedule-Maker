package ScheduleCreator.API;

/**
 * Adapter class for EmailAPI, so it doesn't have to be called directly.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 3/29/2020
 */
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public class EmailAdapter implements APIInterface {

    protected static final APIInterface thisApi = new EmailAPI();

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
        EmailAdapter.thisApi.sendEmail(_email, _message);
    }
    
    public static boolean validate(String emailStr) {
        if(EmailAPI.validate(emailStr)){
        return true;}
        else{
        return false;
        }
    }

}
