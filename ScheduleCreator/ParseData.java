package ScheduleCreator;

/**
 * Parses a text file, and returns the parsed data
 *
 * @author Ilyass Sfar, Jamison Valentine, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 3/2/2020
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.*;

public class ParseData {

    //Regular expression, includes full class name, abbreviated class name with section, the instructor, crn, time and day
    final static String regexEverything = "((.+?(?= - (?:[0-9]{5}))[ ]))|([ ][0-9]{5}[ ])|([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)|(((?:(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b).-.(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b))|\\b([ ](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b))\\s|((?<=Lecture|Lab|Individual Study|Seminar|Clinical|Colloquia|Dissertation or Thesis|Ensemble|Internship, Field Exp, Coop Ed|Lecture and lab|Performance|Physical Activity|Practicum - Dlvrd Ind Setting|Recitations|Student Teaching).+?(?=\\(P\\)E-mail))";
    //A less poweful regex then above, that grabs less things, but it is more useful for the actually schedule maker (Abbreviated class name, time and day)
    final static String regexUsefulInfo = "[ ](\\b[A-Z]{3}\\b.((\\b[0-9]{3}\\b)|(\\b[0-9]{3}\\w)).+ (([0-9]{2}\\b)|([0-9]{2}(\\w)|([A-Z][0-9])(\\w))))|([	]((?:(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b).-.(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b))|\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b";

    /**
     * Applies regex to a text file.
     *
     * @param _semester - This determines which file will be parsed, based on
     * semester.
     * @param _pickRegex - Determines which regex will be used based on which
     * information is needed
     * @throws IOException
     */
    protected static void applyRegex(String _semester, String _pickRegex) throws IOException {
        {
            // get fulltext of the semester text file.
            String content = Boilerplate.GetResourceString(_semester);

            //Give the matcher both the text and the regex expression so it can parse.
            Matcher match = Pattern.compile(_pickRegex).matcher(content);

            //go through all matches, put them in a string and send it to another method to be formatted
            while (match.find()) {

                //put all matching results to new string
                String input = (match.group(0));

                /**
                 * This if else statement will format the output based on which regex was used, currently
                 * there is only a method to format one of the regexs outputs.
                 */
                if (_pickRegex == regexUsefulInfo) {
                    //If the regexUsefulInfo was used pass the string to anoter method to be formated
                    ParseData.formatRegex(input);
                } else {
                    //If the regexEverything was used, it should call a new format method like above, for now it just prints
                    System.out.println(input);
                }

            }

        }
    }

    //Format the reults from the regexUsefulInfo regex output to some degree so it can be worked with
    protected static void formatRegex(String input) throws IOException {
        //Puts every class on a line of its own with time and day following
        //replaceAll is used to break to a new line where needed
        String newresult = input.replaceAll("\\b((TR\\b|MW\\b|MWF\\b|WF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b", "$1 \n");
        System.out.print(newresult);

    }

    /**
     * Returns all parsed data using the "regexEverything"(refer to the "regexEverything" variable comment)
     *
     * @param semester - this determines which file will be parsed, based on
     * semester.
     * @throws IOException
     */
    protected static void getAllData(String _semester) throws IOException {
        /**
         * passes the semester to the ApplyRegex method to do the parsing also
         * passes which regex will be used
         */
        applyRegex(_semester, regexEverything);
    }

    /**
     * Returns only class names with time and day, this can be used for making
     * the schedule
     *
     * @param semester - this determines which file will be parsed, based on
     * semester.
     * @throws IOException
     */
    protected static void getClassData(String _semester) throws IOException {
        /**
         * passes the semester to the ApplyRegex method to do the parsing also
         * passes which regex will be used
         */
        applyRegex(_semester, regexUsefulInfo);
    }
}
