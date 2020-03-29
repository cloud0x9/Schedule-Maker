package ScheduleCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to retrieve and modify persistent data for the
 * application.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly
 *
 * Last Updated: 3/17/2020
 */
public class Translator {

    // this is in the working directory, not the .jar
    protected static File selectedCourseFile;

    /**
     * the type of data requested about a section
     */
    public enum choice {
        TIME,
        DAY,
        BUILDING,
        INSTRUCTOR,
        CRN
    }

    /**
     *
     * @return a list of semester names (Strings) that can be used as arguments
     * in all other DBAdapter methods
     * @throws java.io.FileNotFoundException
     */
    public static List<String> getSemesters() throws FileNotFoundException, IOException {
        String path = "DB/semester_list";

        String contents = Translator.getFullText(path);

        List<String> semesters = Arrays.asList(contents.split("\n"));

        return semesters;
    }

    // DUMMY
    public static List<String> getSections(String _courseNumber, String _semester) {

        String path = "DB/" + _semester + "/all_info";
        ArrayList<String> sections = new ArrayList();

        try {
            String content = Translator.getFullText(path);
            Scanner input = new Scanner(content).useDelimiter("\n");
            String line = "";

            while (input.hasNext()) {
                line = input.next();
                if (line.contains(_courseNumber)) {
                    sections.add(line);
                }
            }
        }
        catch (IOException ex) {}

        return sections;
    }

    /**
     * utility method to return the full text of the file using classpath
     * resource inside the jar.
     *
     * @param _resourceName name of the file in the resources directory (without
     * a leading /)
     * @return the fulltext as a String
     */

    protected static String getFullText(String _resourceName) throws FileNotFoundException, IOException {
        String path = "resources/" + _resourceName;
        String content;
        try ( InputStream stream = Translator.class.getResourceAsStream(path);  InputStreamReader reader = new InputStreamReader(stream)) {
            BufferedReader bufreader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = bufreader.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            content = sb.toString();
        }
        return content;

    }

    /**
     *
     * @param _semesterName
     * @return a List of courses (as Strings) available for the semester
     *
     */
    public static List<String> getCourses(String _semesterName)  {

        List<String> courses = new ArrayList();
        try {
        String contents = Translator.getFullText("DB/" + _semesterName + "/courses");
        courses = Arrays.asList(contents.split("\n"));
        }
        catch (Exception ex) {}

        return courses;
    }

    /**
     * Get specific information about a section
     *
     * @param _choice type of info wanted about section
     * @param _semesterName semester name from getSemesters()
     * @param _section section name e.g. "CSC 250 - 01"
     * @return requested info as a String
     * @throws java.io.FileNotFoundException
     */
    protected static String getSectionInfo(Translator.choice _choice, String _semesterName, String _section) throws FileNotFoundException, IOException {
        String regex = null;
        String dataFileType = "all_info";

        switch (_choice) {
            // this still needs to be finished, we need to account for different times on different days
            case TIME:
                regex = "(?<=([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)).+?(?=\\b((TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(	 	TBA\\b))))";
                //dataFileType = "times_and_dates";
                break;
            case DAY:
                regex = "\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA\\b)))\\b";
                //dataFileType = "times_and_dates";
                break;
            case BUILDING:
                regex = "(?<=\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA\\b)))\\b).*(?=\\=)";
                break;
            case INSTRUCTOR:
                regex = "(?<=\\=).*(?= )";
                break;
            case CRN:
                regex = "[0-9]{5}";
                break;
        }

        // get fulltext of the data we need
        String text = Translator.getFullText("DB/" + _semesterName + "/" + dataFileType);

        // create a List<String> of the lines
        List<String> lines = Arrays.asList(text.split("\n"));

        /**
         * iterate over text to see what lines match, if a line matches the
         * given courses, it it copied and the requested information for the
         * course(s) is extracted using a regex according to which method calls
         */
        for (String line : lines) {
            if (line.contains(_section)) {
                String matchedLines = line;
                /*
				  If a line matches from above then a regex is applied to that line.
				  The regex being used is decided by which method calls.
                 */
                Matcher match = Pattern.compile(regex).matcher(matchedLines);
                while (match.find()) {
                    String parsedLines = (match.group());
                    //return all results found.
                    return parsedLines;
                }
            }
        }
        //Required return, also failsafe.
        return "Error: Check ScheduleCreator.DBAdapter.getInfoBase";
    }

    // ===============================================
    // (DELETE THIS) methods above this point don't modify files
    /**
     * Saves the selected course (abbreviation and number) to persistent
     * database.
     *
     * @param _course
     * @throws Exception
     */

    public static void saveCourse(String _course, String _semester) {

        //Adds new selected course to new line.
        selectedCourseFile = new File(_semester + "_selected_courses.txt");
        try ( //Open file to add new classes.
                 FileWriter output = new FileWriter(new File(_semester + "_selected_courses.txt"), true)) {
            //Adds new selected course to new line.
            output.append(_course + "\n");
        }
        catch (Exception ex) {
            System.out.println("Course was not saved succesfully");
        }
    }

    /**
     * Removes the selected course from the database.
     *
     * @param _course, _semester
     * @throws Exception
     */
    public static void removeCourse(String _course, String _semester) {

        selectedCourseFile = new File(_semester + "_selected_courses.txt");

        try {
            List<String> courses = Files.readAllLines(selectedCourseFile.toPath());
            for (String course : courses) {
                if (course.equals(_course)) {
                    courses.remove(course);
                    break;
                }
            }

            try ( FileWriter writer = new FileWriter(selectedCourseFile)) {
                for (String course : courses) {
                    writer.append(course + "\n");
                }
                writer.close();
            }
            catch (Exception ex) {}
        }

        catch (IOException ex) {
        }

    }

    /**
     * Returns a list of the selected courses.
     *
     * @return
     * @throws Exception
     */
    public static List<String> getSelectedCourses(String _semester) {

        ArrayList<String> selectedCourses = new ArrayList();
        //Load courses from text file to be returned as a list.
        try ( Scanner input = new Scanner(new File(_semester + "_selected_courses.txt"))) {
            //Load courses from text file to be returned as a list.
            String line;
            while (input.hasNext()) {
                line = input.nextLine();
                selectedCourses.add(line);
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println(_semester + "user_selected_courses.txt file does not exist yet (no selected courses for semester)");
        }
        finally {
            return selectedCourses;
        }
    }

}
