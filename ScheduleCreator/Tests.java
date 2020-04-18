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

        //regen databse
        Admin.regenDB();

        //test validate method
        //emailMethodTestData();

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

    public static void testSemester() throws IOException {

        // Example usage of DBAdapter
        List<String> semesters = new Translator().getSemesters();

        System.out.println("Current Semesters are:");
        for (int i = 0; i < semesters.size(); i++) {
            System.out.println(semesters.get(i));
        }

        // choose a semester
        String semester = semesters.get(0);

        // get courses
        List<String> courses = Tests.adapter.getCourses(semester);

        // example course from the semester
        String exampleCourse = courses.get(20);

        System.out.println("Example course is: " + exampleCourse);

        // dummy method - we still need to implement this (I think?)
        List<String> sections = new Translator().getSections(exampleCourse, semester);
        String section = sections.get(0);

        //should return real info for CSC 250 - 01
        System.out.println("Building for " + section + " is: ");
        System.out.println(new Translator().getSectionInfo(Translator.choice.BUILDING, semester, section));

        System.out.println("CRN for " + section + " is: ");
        System.out.println(new Translator().getSectionInfo(Translator.choice.CRN, semester, section));

    }
}
