package ScheduleCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility classes for generating the DB (these do NOT work at runtime) TODO: -
 * pull out hardcoded paths -
 *
 * @author Nick Econopouly, Jamison Valentine, Ilyass Sfar
 *
 * Last Updated: 3/16/2020
 */
public class Admin {

    /**
     * Type of DB File to create
     */
    public enum outputType {
        USEFULINFO,
        ALLDATA,
        COURSES
    }

    /**
     * Regenerate a list of semesters based on the resources/raw dir NOTE: not
     * for use during runtime
     *
     * @return updated semester list
     * @throws java.io.IOException
     */
    public static List<String> regenSemesterList() throws IOException {

        // ("raw" directory contains raw data files for each semester)
        File rawDir = new File("src/ScheduleCreator/resources/raw");

        List<String> semesters = new ArrayList<>();

        // Add output file
        File semesterListFile = new File("src/ScheduleCreator/resources/DB/semester_list");
        semesterListFile.getParentFile().mkdirs();

        // get path of each semester file
        String[] pathnames = rawDir.list();

        // 'pathnames.length - 1' so we don't count the raw directory itself
        try ( FileWriter outputFile = new FileWriter(semesterListFile.getPath())) {
            for (String pathname : pathnames) {
                // add filename to list of semesters
                outputFile.append(pathname);
                outputFile.append("\n");
                semesters.add(pathname);
            }
            outputFile.flush();
            outputFile.close();
        }
        return semesters;
    }

    /**
     * Regenerate database files in DB/
     *
     * @throws IOException
     */
    public static void regenDB() throws IOException {
        String DBPrefix = "src/ScheduleCreator/resources/DB/";
        String rawPrefix = "src/ScheduleCreator/resources/raw/";

        // clear the current DB
        File DBFolder = new File(DBPrefix);
        Admin.deleteDirectory(DBFolder);

        // first generate the list of semesters
        List<String> semesters = Admin.regenSemesterList();

        File db = new File("");
        for (int i = 0; i < semesters.size(); i++) {

            String semester = semesters.get(i);
            String inputFilepath = rawPrefix + semester;

            // generate day and time files
            //generateDayTime(inputFilepath, DBPrefix + semester + "/times_and_dates");
            // generate "all info" files
            generateAllInfo(inputFilepath, DBPrefix + semester + "/all_info");

            // generate courselists
            generateCourseList(inputFilepath, DBPrefix + semester + "/courses");
        }
    }

    protected static void generateDayTime(String _inputFilepath, String _outputFilepath) throws IOException {
        // initial regex
        String regex = "(\\b[A-Z]{3}\\b.((\\b[0-9]{3}\\b)|(\\b[0-9]{3}\\w)).+ (([0-9]{2}\\b)))|[	](((?:(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b).-.(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b))|[	](\\b((TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b)[	]";

        String output = Admin.runRegexOnFile(regex, _inputFilepath);

        // second pass through to better format the results,
        //Puts every class on a line of its own with time and day following
        //replaceAll is used to break to a new line where needed
        String finalOutput = output.replaceAll("(\\b((TR\\b|MW\\b|MWF\\b|WF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA	 	TBA\\b)))\\b)", "$1 \n");
        // write the file
        Admin.writeNewFile(_outputFilepath, finalOutput);

    }

    protected static void generateAllInfo(String _inputFilepath, String _outputFilepath) throws IOException {
        // initial regex
        String regex = "(.+?(?= - (?:[0-9]{5}))[ ])|([ ][0-9]{5}[ ])|([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)|([\\t](?:(?:(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\\\b).-.(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b))|(\\b([\\t](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b)|(\\t.*([0-9]{3}))(?=\\t[A-z]{3} [0-9]{2})|((?=(\\tLecture|\\tLab|\\tIndividual Study|\\tSeminar|\\tClinical|Colloquia|\\tDissertation or Thesis|\\tEnsemble|\\tInternship, Field Exp, Coop Ed|\\tLecture and lab|\\tPerformance|\\tPhysical Activity|\\tPracticum - Dlvrd Ind Setting|\\tPracticum - Dlvrd Org Course|\\tRecitations|\\tStudent Teaching|\\tStudio|\\tLecture and Lab|\\tDissertation or Thesis)).+?(?<=(((\\(P\\)E-mail)|(\\(P\\))|(TBA)))))";

        String output = Admin.runRegexOnFile(regex, _inputFilepath);

        //Puts every class on a line of its own with time and day following.
        //Break to a new line where needed.
        String pass1 = output.replaceAll("(\\(P\\))", "\n");
        //same as above (new lines where needed) but another pass to handle some special cases that aren't handled by the previous regex
        String pass2 = pass1.replaceAll("((?=(\\tLecture|\\tLab|\\tIndividual Study|\\tSeminar|\\tClinical|Colloquia|\\tDissertation or Thesis|\\tEnsemble|\\tInternship, Field Exp, Coop Ed|\\tLecture and lab|\\tPerformance|\\tPhysical Activity|\\tPracticum - Dlvrd Ind Setting|\\tPracticum - Dlvrd Org Course|\\tRecitations|\\tStudent Teaching|\\tStudio|\\tLecture and Lab|\\tDissertation or Thesis)).*?TBA)", "$1\n");

        // Insert a equal sign inebtween the Building name and the instructor
        // This is done since a regex cant be made to stricly get a persons or building
        // name or since both lacks defined structure, so a = is used as a barrier
        String finalOutput = pass2.replaceAll("(\tLecture|\tLab|\tIndividual Study|\tSeminar|\tClinical|Colloquia|\tDissertation or Thesis|\tEnsemble|\tInternship, Field Exp, Coop Ed|\tLecture and lab|\tPerformance|\tPhysical Activity|\tPracticum - Dlvrd Ind Setting|\\tPracticum - Dlvrd Org Course|\tRecitations|\tStudent Teaching|\tStudio|\tLecture and Lab|\tDissertation or Thesis)", " = ");

        // write the file
        Admin.writeNewFile(_outputFilepath, finalOutput);
    }

    protected static void generateCourseList(String _inputFilepath, String _outputFilepath) throws IOException {

        File file = new File(_inputFilepath);
        Scanner input = new Scanner(file);

        String line = "";

        Pattern p = Pattern.compile("(.*)-(.*)- (.*) -.{3}");
        Matcher m = p.matcher(line);

        TreeSet<String> allCourses = new TreeSet();
        String course = "";
        while (input.hasNext()) {
            line = input.nextLine();

            m = p.matcher(line);
            if (m.matches()) {
                course = m.group(1);
                if (!allCourses.contains(m.group(1))) {
                    allCourses.add(m.group(3) + " - " + m.group(1).trim());
                }
            }
        }
        File outputFile = new File(_outputFilepath);
        try ( FileWriter output = new FileWriter(outputFile)) {
            for (String s : allCourses) {
                output.append(s + '\n');
            }
        }
    }

    protected static String runRegexOnFile(String _regex, String _filepath) throws IOException {
        // get fulltext of the semester text file.
        String content = Admin.getFullText(_filepath);

        // Give the matcher both the text and the regex expression so it can parse.
        Matcher match = Pattern.compile(_regex).matcher(content);
        StringBuilder output = new StringBuilder("");
        //go through all matches, put them in a string and send it to another method to be formatted
        while (match.find()) {
            //put all matching results to new string
            String input = match.group(0);
            output.append(input);
        }

        return output.toString();

    }

    protected static String getFullText(String _filepath) throws FileNotFoundException, IOException {
        String content;
        File file = new File(_filepath);
        InputStream stream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufreader = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = bufreader.readLine()) != null) {
            sb.append(str);
            sb.append("\n");
        }
        content = sb.toString();

        return content;
    }

    protected static void writeNewFile(String _filepath, String _contents) throws IOException {
        // Open file and make parent directories
        File file = new File(_filepath);
        file.getParentFile().mkdirs();
        // write file
        try (final FileWriter outputFileWriter = new FileWriter(file, true)) {
            // write file
            outputFileWriter.append(_contents);
        }
    }

// copied from https://www.baeldung.com/java-delete-directory
    protected static void deleteDirectory(File _dir) {
        File[] allContents = _dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        _dir.delete();
    }
}
