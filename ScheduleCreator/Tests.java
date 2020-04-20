package ScheduleCreator;

/**
 * Test class
 *
 * @author Nick Econopouly, Ilyass Sfar
 *
 * Last Updated: 4/6/2020
 */
import ScheduleCreator.API.EmailAdapter;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import java.io.IOException;
import java.util.List;

public class Tests {

    protected static Adapter adapter = new Adapter();

    public static void main(String[] args) throws IOException, MailjetException, MailjetSocketTimeoutException {

        // uncomment and run this once when we add a new semester
        // Admin.regenDB();

        //test validate method
        emailMethodTestData();

    }

    /**
     * Test data for method testing.
     */
    private static void emailMethodTestData() {
        //edge cases
        emailValidationTest(" ", false);
        emailValidationTest("@", false);
        emailValidationTest(".com", false);
        emailValidationTest("@.com", false);
        emailValidationTest("@.com", false);
        emailValidationTest("!@#.gov", false);
        emailValidationTest("123!ABC@test.co", false);
        emailValidationTest("aBCdE@12AbC.edu", true);
        //normal cases
        emailValidationTest("test@test.edu", true);
        emailValidationTest("test@test.gov", true);
        emailValidationTest("123@123.co", true);
        emailValidationTest("ABC@123.io", true);
        emailValidationTest("123@ABC.net", true);
        emailValidationTest("123@ABC.org", true);

    }

    /**
     * Calls the "validate" method with test data and prints what was returned
     * and what was expected.
     *
     * @param _email Email that is being tested for validity.
     * @param _expectedResults What the result should be.
     * @return
     */
    private static void emailValidationTest(String _email, Boolean _expectedResults) {

        String status = (ScheduleCreator.API.EmailAPI.validate(_email) == _expectedResults) ? "Test Passed:"
                : "Test Failed:";

        System.out.println(status + " value of EmailAPI.validate(\"" + _email + "\")" + "should be " + _expectedResults);

    }
}
