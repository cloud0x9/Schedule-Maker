package ScheduleCreator.admin;




import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GenerateCourseList {


    public static void main(String[] args) throws Exception {
        
        File file = new File("C:/Users/jamis/OneDrive/Documents/Java Projects/ScheduleCreator/src/ScheduleCreator/resources/raw/fall2020");
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
                course  = m.group(1);
                if (!allCourses.contains(m.group(1))) {
                    allCourses.add(m.group(3) + " - " + m.group(1));
                }
            }
        }
        FileWriter output = new FileWriter(new File("C:/Users/jamis/OneDrive/Documents/Java Projects/ScheduleCreator/src/ScheduleCreator/resources/raw/fall_2020_courses.txt"));
        for (String s: allCourses) {
            output.append(s + '\n');
        }
        output.close();
    }
    
}
