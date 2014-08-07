import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nichdel on 8/6/14.
 */
public class SplitFile {
    final static String file_type = ".csv";
    final static String separator = ",";

    public String name;
    public List<String> parts;

    public SplitFile(String filename)
    {
        name = filename;

        try
        {
            Scanner in = new Scanner(new File(name + file_type));
            parts = Separate(in.next());
            in.close();
        }
        catch (IOException exception)
        {
            System.out.println("Could not open file, it may not exist.");
            System.out.println("Attempting to create a new file with the given name. Please provide the headers");
            try
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                SplitFile badHack;
                badHack = new SplitFile(filename, Separate(in.readLine()));
                this.parts = badHack.parts;

            }
            catch (IOException exception2)
            {
                System.out.println("Admittedly I'm not very good at handling Java exceptions yet. Sorry!");
            }
        }
    }
    public SplitFile(String filename, List<String> parts)
    {
        name = filename;
        this.parts = parts;

        FileWriter out = null;
        try
        {
            out = new FileWriter(name + file_type);

            for (int i = 0; i < parts.size(); i++)
            {
                out.write(parts.get(i));
                if (i != parts.size()-1)
                {
                    out.write(separator);
                }
            }
            out.write("\n");
            out.close();
        }
        catch (IOException exception)
        {
            System.out.println("Sorry, there was a problem creating the file. It may already exist.");
        }
    }

    private List<String> Separate(String line)
    {
        return Arrays.asList(line.split(separator));
    }

    public List<List<String>> Trials()
    {
        List<List<String>> list_of_line_lists = new ArrayList<List<String>>();
        try
        {
            Scanner in = new Scanner(new File(name + file_type));
            in.next(); // Disregard the first line.
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

    public boolean AppendLine(List<Long> entries)
    {
        try
        {
            FileWriter out = new FileWriter(name + file_type, true);

            for (int i = 0; i < entries.size(); i++)
            {
                out.write(String.valueOf(entries.get(i)));

                if (i != entries.size()-1)
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
