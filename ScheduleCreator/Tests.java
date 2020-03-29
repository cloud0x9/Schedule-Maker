package ScheduleCreator;

/**
 * Test class
 *
 * @author Nick Econopouly, Ilyass Sfar
 *
 * Last Updated: 3/27/2020
 */
import ScheduleCreator.API.ApiAdapter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tests {

    public static void main(String[] args) throws IOException {

        //regen databse
        Admin.regenDB();
        //test api call
        //ApiAdapter testAPI = new ApiAdapter();
        //System.out.println("API returned "+testAPI.decisionForce(""));
        //
        // TODO: add courselist regex;
        // fix regenDB() so that it doesn't rely on classpath resources (so tests always work the first time)
        // getSections()
        // regenerate the database
        //
        // test how DBAdapter works with current semesters
        //testSemester();
    }

    public static void testSemester() throws IOException {

        // Example usage of DBAdapter
        List<String> semesters = Translator.getSemesters();

        System.out.println("Current Semesters are:");
        for (int i = 0; i < semesters.size(); i++) {
            System.out.println(semesters.get(i));
        }

        // choose a semester
        String semester = semesters.get(0);

        // get courses
        List<String> courses = Translator.getCourses(semester);

        // example course from the semester
        String exampleCourse = courses.get(20);

        System.out.println("Example course is: " + exampleCourse);

        // dummy method - we still need to implement this (I think?)
        List<String> sections = Translator.getSections(exampleCourse, semester);
        String section = sections.get(0);

        //should return real info for CSC 250 - 01
        System.out.println("Building for " + section + " is: ");
        System.out.println(Translator.getSectionInfo(Translator.choice.BUILDING, semester, section));

        System.out.println("CRN for " + section + " is: ");
        System.out.println(Translator.getSectionInfo(Translator.choice.CRN, semester, section));

    }
}
