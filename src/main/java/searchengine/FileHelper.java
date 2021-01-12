package searchengine;

// For reading database file.
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// For reading configuration file.
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * FileHelper contains all methods that help reading a database of
 * websites from a file.
 *
 * @author intial repository
 */
public class FileHelper {

    /**
     * Parses a file and extracts all the websites that are contained
     * in the file.
     *
     * Each file lists a number of websites including their URL, their
     * title, and the words that appear on a website. In particular, a
     * website starts with a line "*PAGE:" that is followed by the URL
     * of the website. The next line represents the title of the
     * website in natural language.  This line is followed by a list
     * of words that occur on the page.
     *
     * @param filename The filename of the file that we want to
     * load. Needs to include the directory path as well.
     * @return The list of websites that contain all websites that
     * were found in the file.
     */
    public static List<Website> parseFile(String filename) {
        // Will contain all the websites that we have found in the file.
        List<Website> sites = new ArrayList<>();

        // We use these variables to store the url, title, and the
        // words that we find for a website in the file.
        String url = null, title = null;
        List<String> listOfWords = null;

        // foundFirstPage is true as soon as we found the first "*PAGE:" line
        // and is used to skip any erroneous lines at the beginning of the file.
        boolean foundFirstPage = false;
        // Idea: isNextLineTitle distinguishes if the line is the title or if the line is a word
        // set it to true after reading a line starting with *PAGE:, set it to false in the next line.
        boolean isNextLineTitle = false;

        try {
            // Load the file, will throw a FileNotFoundException if the
            // filename doesn't point to an existing file.
            Scanner sc = new Scanner(new File(filename), "UTF-8");
            // As long as we are not done with reading the file.
            while (sc.hasNext()) {
                // Get the next line from the file.
                String line = sc.nextLine();

                // Check status and the content of the line to figure out if this line is
                // the url, the title, or a word.
                if (line.startsWith("*PAGE:")) {
                    // New website entry starts, so create previous website from data gathered
                    // (if data is correct)
                    if (url != null && title != null && listOfWords != null) {
                        sites.add(new Website(url, title, listOfWords));
                    }

                    // Clear all variables to start new website entry.
                    // 6 is length of "*PAGE:"; get rest of line to capture url.
                    url = line.substring(6);
                    // The title and the list of words should be null.
                    title = null;
                    listOfWords = null;

                    foundFirstPage = true;
                    isNextLineTitle = true;
                } else if (foundFirstPage && isNextLineTitle) {
                    // This is the title of the website.
                    title = line;
                    // The subsequent lines are the words of the website.
                    isNextLineTitle = false;
                } else if (foundFirstPage) {
                    // If this is the first word on the website, we have to initialize listOfWords.
                    if (listOfWords == null) {
                        listOfWords = new ArrayList<>();
                    }
                    listOfWords.add(line);
                }
            }
            // When we have read the whole file, we have to create the very last website manually.
            if (url != null && title != null && listOfWords != null) {
                sites.add(new Website(url, title, listOfWords));
            }
        } catch (FileNotFoundException e) {
            System.out.println("error while reading file: " + e.getMessage());
        }

        return sites;
    }

    /**
     * Reads a standard Java config file {@code config.properties},
     * expecting it to contain a line of the form
     * {@code database=path}. It then returns {@code path}.
     *
     * @return The path assigned to the {@code database} attribute in
     * {@code config.properties}.
     */
    public static String readConfig(){
        String config   = "config.properties";
        Properties prop = new Properties();
        String database = null;

        try (InputStream inputStream = new FileInputStream(config)) {
            prop.load(inputStream);
            database = prop.getProperty("database");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return database;
    }

    /**
     * Calls the original parseFile method only if the args are correct.
     *
     * @param args The arguments receied by the entry point.
     * @return The list of websites that contain all websites that
     * were found in the file.
     */
    public static List<Website> parseFile(String[] args) {
        String database;

        if (args.length < 1) {
            database = FileHelper.readConfig();
            if (database == null || database.isEmpty()) {
                System.out.println("Error: Filename is missing");
                System.exit(1);
            } else {
                System.out.println("Path \"" + database + "\" from config.properties.");
            }
        } else {
            database = args[0];
            System.out.println("Path \"" + database + "\" as program argument.");
        }

        return parseFile(database);
    }
}
