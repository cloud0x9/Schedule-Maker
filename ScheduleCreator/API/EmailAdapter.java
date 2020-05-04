package ScheduleCreator.API;

/**
 * Adapter class for EmailAPI, so it doesn't have to be called directly.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 4/21/2020
 */
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public class EmailAdapter implements APIInterface {

    protected static final APIInterface thisApi = new EmailAPI();

    /**
     * Send emails using an API.
     *
     * @param _email The email that is being sent to, this comes from the user.
     * @param _message This is what is being sent in the email.
     * @throws MailjetException
     * @throws MailjetSocketTimeoutException
     */
    @Override
    public void sendEmail(String _email, String _message) throws MailjetException, MailjetSocketTimeoutException {
        EmailAdapter.thisApi.sendEmail(_email, _message);
    }

    /**
     * Validates whether the email is valid.
     *
     * @param _email
     * @return true if the email is valid, else false
     */
    public static boolean validate(String _email) {
        return EmailAPI.validate(_email);
    }

}
