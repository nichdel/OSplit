import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SplitFile {
    final static String separator = ",";

    public String file_path;
    public String file_name;
    public List<String> headers;

    /**
     * Attempts to open an existing SplitFile
     * @param file_path Absolute file path
     */
    public SplitFile(String file_path)
    {
        this.file_path = file_path;
        file_name = (new File(file_path)).getName().split("\\.")[0];

        try
        {
            Scanner in = new Scanner(new File(this.file_path));
            headers = Arrays.asList(in.nextLine().split(separator));
            in.close();
        }
        catch (IOException exception)
        {
            // TODO: Instead of defaulting to this, make the caller choose to
            System.out.println("Could not open file, it may not exist.");
            System.out.println("Attempting to create a new file with the given file_path. Please provide the headers");
            try
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                SplitFile badHack;
                badHack = new SplitFile(file_path, Arrays.asList(in.readLine().split(separator)));
                this.headers = badHack.headers;

            }
            catch (IOException exception2)
            {
                System.out.println("Admittedly I'm not very good at handling Java exceptions yet. Sorry!");
            }
        }
    }

    /**
     * Attempts to create a new file
     * @param file_path Absolute file path, if it doesn't end in .csv we add it
     * @param headers List of headers
     */
    public SplitFile(String file_path, List<String> headers)
    {
        this.file_path = file_path;
        if (!this.file_path.contains(".csv"))
        {
            this.file_path += ".csv";
        }
        this.headers = headers;

        FileWriter out;
        try
        {
            out = new FileWriter(this.file_path);

            for (int i = 0; i < headers.size(); i++)
            {
                out.write(headers.get(i));
                if (i != headers.size()-1)
                {
                    out.write(separator);
                }
            }
            out.write("\n");
            out.close();
        }
        catch (IOException exception)
        {
            System.out.println("Something went very wrong. If this happens more than once go to " +
                    "https://github.com/nichdel/OSplit/issues and chew my ear off.");
        }
    }

    /**
     * Helper function to separate lines correctly.
     * @param line A string such as "Header 1,Header 2,Header 3"
     * @return A list such as ["Header 1", "Header 2", "Header 3"]
     */
    private static List<Long> Separate(String line)
    {
        final List<String> separated_strings = Arrays.asList(line.split(separator));

        List<Long> separated_longs = new ArrayList<Long>();

        for (String separated_string : separated_strings) {
            separated_longs.add(Long.valueOf(separated_string));
        }

        return separated_longs;
    }

    /**
     * Check the file for a list of trials
     * @return A list of trials, which are lists of nanoseconds from start.
     */
    public List<List<Long>> Trials()
    {
        List<List<Long>> list_of_line_lists = new ArrayList<List<Long>>();
        try
        {
            Scanner in = new Scanner(new File(file_path));
            in.nextLine(); // Disregard the first line.
            while (in.hasNext())
            {
                list_of_line_lists.add(Separate(in.next()));
            }
            in.close();
        }
        catch (IOException exception)
        {
            System.out.println("oops");
        }
        return list_of_line_lists;
    }

    /**
     * Add a trial to the end of the file.
     * @param trial A set of timings for a single trial
     * @return whether successful
     */
    public boolean AppendTrial(List<Long> trial)
    {
        try
        {
            FileWriter out = new FileWriter(file_path, true);

            for (int i = 0; i < trial.size(); i++)
            {
                out.write(String.valueOf(trial.get(i)));

                if (i != trial.size()-1)
                {
                    out.write(separator);
                }
            }
            out.write("\n");
            out.close();
            return true;
        }
        catch (IOException exception)
        {
            return false;
        }
    }
}
