package ScheduleCreator;

/**
 * Access files from a central location
 *
 * @author Nick Econopouly, Ilyass Sfar, Jamison Valentine, Nathan Tolodzieki
 *
 * Last Updated: 3/5/2020
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Boilerplate {

    // resourceName is the name of the file in the resources directory (without a leading /)
    protected static String GetResourceUrl(String resourceName) {
        return "src/ScheduleCreator/resources/" + resourceName;
    }

    // returns a file object
    protected static File GetResourceFile(String resourceName) {
        return new File(Boilerplate.GetResourceUrl(resourceName));
    }

    // returns fulltext of file
    protected static String GetResourceString(String resourceName) throws IOException {
        Path path = Paths.get(Boilerplate.GetResourceUrl(resourceName));
        return new String(Files.readAllBytes(path));
    }

}
