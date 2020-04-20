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
        emailValidationTest(" ", "FAIL");
        emailValidationTest("@", "FAIL");
        emailValidationTest(".com", "FAIL");
        emailValidationTest("@.com", "FAIL");
        emailValidationTest("@.com", "FAIL");
        emailValidationTest("!@#.gov", "FAIL");
        emailValidationTest("123!ABC@test.co", "FAIL");
        emailValidationTest("aBCdE@12AbC.edu", "PASS");
        //normal cases
        emailValidationTest("test@test.edu", "PASS");
        emailValidationTest("test@test.gov", "PASS");
        emailValidationTest("123@123.co", "PASS");
        emailValidationTest("ABC@123.io", "PASS");
        emailValidationTest("123@ABC.net", "PASS");
        emailValidationTest("123@ABC.org", "PASS");

    }

    /**
     * Calls the "validate" method with test data and prints what was returned
     * and what was expected.
     *
     * @param _email Email that is being tested for validity.
     * @param _expectedResults What the result should be.
     * @return
     */
    private static String emailValidationTest(String _email, String _expectedResults) {

        if (ScheduleCreator.API.EmailAPI.validate(_email)) {
            System.out.println("PASSED : \"" + _email + "\"  Is a valid email. EXPECTED: " + _expectedResults);
        } else {
            System.out.println("FAILED : \"" + _email + "\" Is NOT a valid email. EXPECTED: " + _expectedResults);
        }
        return null;

    }
}
