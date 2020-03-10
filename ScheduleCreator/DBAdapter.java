package ScheduleCreator;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 3/6/2020
 */
public class DBAdapter {

    protected static File selectedCourseFile = new File("src/ScheduleCreator/resources/raw/user_selected_courses.txt");
    //Regex expression to get day from the current format.
    final static String getDay = "\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA\\b)))\\b";
    //Regex expression to get time from the current format.
    final static String getTime = "(?<=([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)).+?(?=\\b((TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(	 	TBA\\b))))";
    //Regex expression to get CRN from the current format.
    final static String getCRN = "[0-9]{5}";
    //Regex expression to get building from the current format.
    final static String getBuilding = "(?<=\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA\\b)))\\b).*(?=\\=)";
    //Regex expression to get instructor from the current format.
    final static String getInstructor = "(?<=\\=).*(?= )";
    //Temporary holding place for course information while being parsed for requested information.
    private static List<String> lines;

    /**
     * Saves the selected course (abbreviation and number) and saves to
     * database.
     *
     * @param _course
     * @throws Exception
     */
    public static void saveCourse(String _course) throws Exception {

        //Open file to add new classes.
        FileWriter output = new FileWriter(selectedCourseFile, true);

        //Adds new selected course to new line.
        output.append(_course + "\n");

        output.close();
    }

    /**
     * Removes the selected course from the database.
     *
     * @param _course
     * @throws Exception
     */
    public static void removeCourse(String _course) throws Exception {

        Scanner input = new Scanner(selectedCourseFile);
        StringBuffer newContents = new StringBuffer();
        String line = "";

        /**
         * Gets all of the courses except the selected one and appends to a new
         * file to be saved. *
         */
        while (input.hasNext()) {
            line = input.nextLine();

            if (!line.contains(_course)) {
                newContents.append(_course + '\n');
            }

        }

        FileWriter writer = new FileWriter(selectedCourseFile);
        writer.append(newContents.toString());
        writer.close();

    }

    // get a list of semesters (which can be used as an argument to DBAdapter.getCourses)
    public static List<String> getSemesters() {
        List<String> semesters = new ArrayList();

        File dir = ScheduleCreator.Boilerplate.GetResourceFile("raw/semesters");
        String[] pathnames;
        pathnames = dir.list();

        for (String pathname : pathnames) {
            System.out.println(pathname);
            File f = new File(pathname);
            semesters.add(f.getName());
        }
        return semesters;
    }

    /**
     * Returns a list of the selected courses.
     *
     * @return
     * @throws Exception
     */
    public static List<String> getSelectedCourses() throws Exception {

        Scanner input = new Scanner(selectedCourseFile);

        //Load courses from text file to be returned as a list.
        ArrayList<String> selectedCourses = new ArrayList();
        String line = "";
        while (input.hasNext()) {
            line = input.nextLine();
            selectedCourses.add(line.trim());
        }

        input.close();
        return selectedCourses;
    }

    public static List<String> getCourses(String _semester) throws Exception {

        File file = new File("src/ScheduleCreator/resources/raw/" + _semester + "courses.txt");
        Scanner input = new Scanner(file);
        input.useDelimiter("\\Z");
        String contents = input.next();

        List<String> courses = Arrays.asList(contents.split("\n"));
        return courses;
    }

    /**
     * Returns the time for a given course in a given semester. If a online
     * course is requested TBA is returned.
     *
     * @param _abbreviation Course name that the time is being requested for.
     * @param _semester Semester the course being requested is in.
     * @throws Exception
     */
    public static void getTime(String _abbreviation, String _semester) throws Exception {
        String TimeCourse = getInfoBase(_abbreviation, _semester, getTime);
        //Should be return, for now print.
        System.out.println(TimeCourse);

    }

    /**
     * Returns the day(s) for a given course in a given semester. If a online
     * course is requested TBA is returned.
     *
     * @param _abbreviation Course name that the day is being requested for.
     * @param _semester Semester the course being requested is in.
     * @throws Exception
     */
    public static void getDay(String _abbreviation, String _semester) throws Exception {
        String DayCourse = getInfoBase(_abbreviation, _semester, getDay);
        //Should be return, for now print.
        System.out.println(DayCourse);
    }

    /**
     * Returns the CRN for a given course in a given semester.
     *
     * @param _abbreviation Course name that the CRN is being requested for.
     * @param _semester Semester the course being requested is in.
     * @throws Exception
     */
    public static void getCRN(String _abbreviation, String _semester) throws Exception {
        String CRNCourse = getInfoBase(_abbreviation, _semester, getCRN);
        //Should be return, for now print.
        System.out.println(CRNCourse);
    }

    /**
     * Returns the instructor for a given course in a given semester.
     *
     * @param _abbreviation Course name that the instructor is being requested
     * for.
     * @param _semester Semester the course being requested is in.
     * @throws Exception
     */
    public static void getInstructor(String _abbreviation, String _semester) throws Exception {
        String InstructorCourse = getInfoBase(_abbreviation, _semester, getInstructor);
        //Should be return, for now print.
        System.out.println(InstructorCourse);
    }

    /**
     * Returns the building name and room number for a given course in a given
     * semester. Currently there is no safe guard against online class which
     * have no building, so be careful when calling this method.
     *
     * @param _abbreviation Course name that the building is being requested
     * for.
     * @param _semester Semester the course being requested is in.
     * @throws Exception
     */
    public static void getBuilding(String _abbreviation, String _semester) throws Exception {
        String BuildingCourse = getInfoBase(_abbreviation, _semester, getBuilding);
        //Should be return, for now print.
        System.out.println(BuildingCourse);
    }

    /**
     * Returns information on a given course in a given semester based on which
     * method class. This is the base method for other get methods for class
     * information.
     *
     * @param _abbreviation Course name that information is being requested for.
     * @param _semester Semester the course being requested is in.
     * @param _whichRegex
     * @throws Exception
     */
    public static String getInfoBase(String _abbreviation, String _semester, String _whichRegex) throws Exception {
        /**
         * Decides which file will be used, this is done for speed Since the
         * time and day will be the most request, both are in a separate file
         * for a little speed
         */
        if (_whichRegex == getDay | _whichRegex == getTime) {
            lines = Files.readAllLines(Paths.get(ScheduleCreator.Boilerplate.GetResourceUrl("raw/" + _semester + "coursesTimeDate.txt")));

        } else {
            lines = Files.readAllLines(Paths.get(ScheduleCreator.Boilerplate.GetResourceUrl("raw/" + _semester + "coursesAllInfo.txt")));

        }

        /**
         * iterate over text to see what lines match, if a line matches the
         * given courses, it it copied and the requested information for the
         * course(s) is extracted using a regex according to which method calls
         */
        for (String line : lines) {
            if (line.contains(_abbreviation)) {
                String matchedLines = line;
                /*
                If a line matches from above then a regex is applied to that line.
                The regex being used is decided by which method calls.
                 */
                Matcher match = Pattern.compile(_whichRegex).matcher(matchedLines);
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

}
