package ScheduleCreator;

/**
 * Parses a text file, and returns the parsed data
 * updated: February, 27, 2020
 *
 * @author Sfar
 */
import java.io.IOException;
import java.util.regex.*;

public class Translator {

    /**
     *Applies regex to a text file.
     * 
     * @param _semester
     * @param _group
     * @throws IOException
     */
    protected static void applyRegex(String _semester, int _group) throws IOException {
        {
            //Regular expression, includes full class name, abbreviated class name with section, the instructor, crn, time and day
            final String RegexExpression = "(.*?(?= - (?:[0-9]{5}))|[0-9]{5})|(\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.* [0-9]{2}\\b)|(([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].([AaPp][Mm])\\b).-.([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].([AaPp][Mm])\\b|\\b(TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b)\\s|(?<=Lecture|Lab|Individual Study|Seminar|Clinical|Colloquia|Dissertation or Thesis|Ensemble|Internship, Field Exp, Coop Ed|Lecture and lab|Performance|Physical Activity|Practicum - Dlvrd Ind Setting|Recitations|Student Teaching).*?(?=\\(P\\)E-mail)";

            // get fulltext of the semester
            String content = Boilerplate.GetResourceString(_semester);

            //Give the matcher both the text and the regex expression so it can parse.
            Matcher match = Pattern.compile(RegexExpression).matcher(content);

            while (match.find()) {
                //Should be a return statment, but is sout for now
                //print matches, based on which method calls it
                System.out.println(match.group(_group));

            }
        }
    }

    /**
     * Returns all parsed data
     *
     * @param _semester
     * @throws IOException
     */
    protected static void getAllData(String _semester) throws IOException {
        //passes the semster to the ApplyRegex method to do the parseing
        //The number that is passed is the group number, which determines what will be output, 0 means everything
        applyRegex(_semester, 0);
    }

    /**
     * Returns only class names with sections, also returns a bunch of null
     * values which can be ignored (this can be used in the class lookup)
     *
     * @param _semester
     * @throws IOException
     */
    protected static void getClassData(String _semester) throws IOException {
        //passes the semster to the ApplyRegex method to do the parseing
        //The number that is passed is the group number, which determines what will be output
        //1 means the first set of capturing parthesses
        applyRegex(_semester, 2);
    }

}
