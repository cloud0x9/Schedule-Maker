package ScheduleCreator;

/**
 * Creates DB files by parsing raw semester files
 *
 * @author Ilyass Sfar, Jamison Valentine, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 3/2/2020
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.*;
import java.util.function.Function;

public class ParseData {
    
    /**
     * Type of DB File to create
     */
    public enum outputType {
        USEFULINFO,
        ALLDATA
    }

    /**
     * Applies regex to a text file.
     * @param _semesterFile path of file to be parsed
     * @param _outputFile path of output file
     * @param _outputType type of output
     * @throws IOException
     */
    protected static void applyRegex(String _semesterFile, String _outputFile, ParseData.outputType _outputType) throws IOException {
        {
            // initial regex 
            String regex = null; 
            
            // function used to format text after the initial regex
            Function<String,String> formatFunction = null;
            
            // get fulltext of the semester text file.
//            System.out.println(_semesterFile);
            String content = new Scanner(new File(_semesterFile)).useDelimiter("\\Z").next();
//            String content = DBAdapter.getFullText(_semesterFile);
            
            switch (_outputType) {
                // abbreviated class name, time, and day
                case USEFULINFO: regex = "[ ](\\b[A-Z]{3}\\b.((\\b[0-9]{3}\\b)|(\\b[0-9]{3}\\w)).+ (([0-9]{2}\\b)|([0-9]{2}(\\w)|([A-Z][0-9])(\\w))))|([	]((?:(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b).-.(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b))|\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b";
                formatFunction = ParseData::formatUsefulInfo;
                break;
                // full class name, abbreviated class name with section, instructor, crn, time, and day
                case ALLDATA: regex = "(.+?(?= - (?:[0-9]{5}))[ ])|([ ][0-9]{5}[ ])|([ ]\\b[A-Z]{3}\\b.\\b[0-9]{3}\\b.+ [0-9]{2}\\b)|([	](?:(?:(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b).-.(?:[0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].(?:[AaPp][Mm])\\b))|(\\b([	](TR\\b|MW\\b|MWF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b)|(	.*([0-9]{3}))(?=	[A-z]{3} [0-9]{2})|((?=(	Lecture|	Lab|	Individual Study|	Seminar|	Clinical|Colloquia|	Dissertation or Thesis|	Ensemble|	Internship, Field Exp, Coop Ed|	Lecture and lab|	Performance|	Physical Activity|	Practicum - Dlvrd Ind Setting|	Recitations|	Student Teaching|	Studio|	Lecture and Lab|	Dissertation or Thesis)).+?(?<=((\\(P\\)E-mail)|(\\(P\\)))))|(((\\(P\\)E-mail)|(\\(P\\))))";
                formatFunction = ParseData::formatAllData;
                break;
            }

            //Give the matcher both the text and the regex expression so it can parse.
            Matcher match = Pattern.compile(regex).matcher(content);
            
            StringBuilder output = new StringBuilder(""); 

            //go through all matches, put them in a string and send it to another method to be formatted
            while (match.find()) {

                //put all matching results to new string
                String input = (match.group(0));

                // append the formated output to the final StringBuilder
                output.append(formatFunction.apply(input));
            }

            // Open file and make parent directories
            File file = new File(_outputFile);
            file.getParentFile().mkdirs();

            // write file
            try (FileWriter outputFile = new FileWriter(file, true)) {
                // write file
                output.append(output.toString());
            }
        }
    }

    //Format the reults from the regexUsefulInfo regex output to some degree so it can be worked with
    protected static String formatUsefulInfo(String _input) {
        //Puts every class on a line of its own with time and day following
        //replaceAll is used to break to a new line where needed
        return _input.replaceAll("\\b((TR\\b|MW\\b|MWF\\b|WF\\b|M\\b|T\\b|W\\b|R\\b|F\\b|(TBA.*TBA\\b)))\\b", "$1 \n");
    }

    protected static String formatAllData(String _input) {
        /*
         Puts every class on a line of its own with time and day following.
         Break to a new line where needed.
         */
        String results = _input.replaceAll("\\(P\\)", "\n");
        /*
        Insert a equal sign inebtween the Building name and the instructor
        This is done since a regex cant be made to stricly get a persons or building
        name or since both lacks defined structure, so a = is used as a barrier
         */
        return results.replaceAll("(	Lecture|	Lab|	Individual Study|	Seminar|	Clinical|Colloquia|	Dissertation or Thesis|	Ensemble|	Internship, Field Exp, Coop Ed|	Lecture and lab|	Performance|	Physical Activity|	Practicum - Dlvrd Ind Setting|	Recitations|	Student Teaching|	Studio|	Lecture and Lab|	Dissertation or Thesis)", " = ");
    }
}
