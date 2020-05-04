package ScheduleCreator.API;

/**
 * Interface for email API calls for MailJet.
 *
 * @author Ilyass Sfar
 *
 * Last Updated: 4/21/2020
 */
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public interface APIInterface {

    /**
     * Send emails using an API.
     *
     * @param _email The email that is being sent to, this comes from the user.
     * @param _message This is what is being sent in the email.
     * @throws MailjetException
     * @throws MailjetSocketTimeoutException
     */
    public void sendEmail(String _email, String _message) throws MailjetException, MailjetSocketTimeoutException;
}
