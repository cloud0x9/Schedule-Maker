package ScheduleCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    // this is in the working directory, not the .jar
    protected static File selectedCourseFile = new File("user_selected_courses.txt");

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

        String contents = DBAdapter.getFullText(path);

        List<String> semesters = Arrays.asList(contents.split("\n"));

        return semesters;
    }

    // DUMMY
    public static List<String> getSections(String _course, String _semesterName) {
        ArrayList<String> sections = new ArrayList<>();
        sections.add("CSC 250 - 01");
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
        InputStream stream = DBAdapter.class.getResourceAsStream(path);
        
        InputStreamReader reader = new InputStreamReader(stream);
        
        BufferedReader bufreader = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String str;
        while((str = bufreader.readLine())!= null){
            sb.append(str);
            sb.append("\n");
        }
        String content = sb.toString();
        reader.close();
        stream.close();
        return content;
        
    }

    /**
     *
     * @param _semesterName
     * @return a List of courses (as Strings) available for the semester
     * @throws java.io.FileNotFoundException
     */
    public static List<String> getCourses(String _semesterName) throws FileNotFoundException, IOException {
        String contents = DBAdapter.getFullText("DB/" + _semesterName + "/" + "courses");
        List<String> courses = Arrays.asList(contents.split("\n"));
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
    protected static String getSectionInfo(DBAdapter.choice _choice, String _semesterName, String _section) throws FileNotFoundException, IOException {
        String regex = null;
        String dataFileType = "all_info";

        switch (_choice) {
            // this still needs to be finished, we need to account for different times on different days			
            case TIME:
                regex = "(?<=([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)).+?(?=\\b((TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(	 	TBA\\b))))";
                dataFileType = "times_and_dates";
                break;
            case DAY:
                regex = "\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA\\b)))\\b";
                dataFileType = "times_and_dates";
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
        String text = DBAdapter.getFullText("DB/" + _semesterName + "/" + dataFileType);

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
    public static void saveCourse(String _course) throws Exception {
        //Adds new selected course to new line.
        try ( //Open file to add new classes.
                FileWriter output = new FileWriter(selectedCourseFile, true)) {
            //Adds new selected course to new line.
            output.append(_course + "\n");
        }
    }

    /**
     * Removes the selected course from the database.
     *
     * @param _course
     * @throws Exception
     */
    public static void removeCourse(String _course) throws Exception {

        Scanner input = new Scanner(selectedCourseFile);
        StringBuilder newContents = new StringBuilder();
        String line = "";

        /**
         * Gets all of the courses except the selected one and appends to a new
         * file to be saved. *
         */
        while (input.hasNext()) {
            line = input.nextLine();

            if (!line.contains(_course)) {
                newContents.append(_course).append('\n');
            }

        }

        try (FileWriter writer = new FileWriter(selectedCourseFile)) {
            writer.append(newContents.toString());
        }

    }

    /**
     * Returns a list of the selected courses.
     *
     * @return
     * @throws Exception
     */
    public static List<String> getSelectedCourses() throws Exception {

        ArrayList<String> selectedCourses;
        //Load courses from text file to be returned as a list.
        try (Scanner input = new Scanner(selectedCourseFile)) {
            //Load courses from text file to be returned as a list.
            selectedCourses = new ArrayList();
            String line;
            while (input.hasNext()) {
                line = input.nextLine();
                selectedCourses.add(line.trim());
            }
        }
        return selectedCourses;
    }

    /**
     * Regenerate database files in DB/ (not for use during runtime)
     *
     * @throws IOException
     */
    public static void regenDB() throws IOException {

        // first generate the list of semesters
        regenSemesterList();
                
        List<String> semesters = getSemesters();

        String DBPrefix = "src/ScheduleCreator/resources/DB/";
        String rawPrefix = "raw/";

        File db = new File("");
        for (int i = 0; i < semesters.size(); i++) {

            String semester = semesters.get(i);

            // generate day and time
            ParseData.applyRegex(rawPrefix + semester, DBPrefix + semester + "/times_and_dates", ParseData.outputType.USEFULINFO);

            // generate all info
            ParseData.applyRegex(rawPrefix + semester, DBPrefix + semester + "/all_info", ParseData.outputType.ALLDATA);

            //TODO: generate courselist
        }
    }

    /**
     * Regenerate a list of semesters based on the resources/raw dir
     * NOTE: not for use during runtime
     * @throws java.io.IOException
     */
    public static void regenSemesterList() throws IOException {

        // ("raw" directory contains raw data files for each semester)
        File rawDir = new File("src/ScheduleCreator/resources/raw");

        // clear old semester list
        File semesterListFile = new File("src/ScheduleCreator/resources/DB/semester_list");
        semesterListFile.delete();
        semesterListFile.getParentFile().mkdirs();

        // get path of each semester file
        String[] pathnames = rawDir.list();
        
        // 'pathnames.length - 1' so we don't count the raw directory itself
        try (FileWriter outputFile = new FileWriter(semesterListFile.getPath())) {
            for (String pathname : pathnames) {
                // add filename to list of semesters
                outputFile.append(pathname);
                outputFile.append("\n");
            }
            outputFile.flush();
            outputFile.close();
        }
    }
}
