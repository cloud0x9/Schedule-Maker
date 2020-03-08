package ScheduleCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wrycode
 */
public class Tests {

    public static void main(String[] args) {

        // test how DBAdapter works with current semesters
        testSemester();
        
        // now something different - a fake new semester (copy of Spring 2020)
        Path source = Paths.get("src/ScheduleCreator/resources/raw/Spring 2020");
        Path destination = Paths.get("src/ScheduleCreator/resources/raw/fall 2018");
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            DBAdapter.regenDB();
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Testing DBAdapter with fake semester");
        testSemester();
        
        // delete the BS file we created for tests
        File file = new File("src/ScheduleCreator/resources/raw/fall 2018");
        if(file.delete()) { 
            System.out.println("File deleted successfully"); 
        } else { 
            System.out.println("Failed to delete the file"); 
        }         
    }

    public static void testSemester() {
        // Example usage of DBAdapter
        List<String> semesters = DBAdapter.getSemesters();

        System.out.println("Current Semesters are:");
        for (int i = 0; i < semesters.size(); i++) {
            System.out.println(semesters.get(i));
        }

        // choose a semester
        String semester = semesters.get(0);

        // get courses
        List<String> courses = DBAdapter.getCourses(semester);

        // example course from the semester
        String exampleCourse = courses.get(0);

        System.out.println("Example course is: " + exampleCourse);

        // dummy method - we still need to implement this (I think?)
        List<String> sections = DBAdapter.getSections(exampleCourse, semester);
        String section = sections.get(0);

        //should return real info for CSC 250 - 01
        System.out.println("Building for " + section + " is: ");
        System.out.println(DBAdapter.getSectionInfo(DBAdapter.choice.BUILDING, semester, section));

        System.out.println("CRN for " + section + " is: ");
        System.out.println(DBAdapter.getSectionInfo(DBAdapter.choice.CRN, semester, section));

    }
}
