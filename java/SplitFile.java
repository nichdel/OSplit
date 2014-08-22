import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SplitFile {
    final static String separator = ",";

    public String name;
    public List<String> parts;

    public SplitFile(String filename)
    {
        // FIXME: Refer to name and absolute path separately
        name = filename;

        try
        {
            Scanner in = new Scanner(new File(name));
            parts = Arrays.asList(in.next().split(separator));
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
                badHack = new SplitFile(filename, Arrays.asList(in.readLine().split(separator)));
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

        FileWriter out;
        try
        {
            out = new FileWriter(name);

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

    private List<Long> Separate(String line)
    {
        final List<String> separated_strings = Arrays.asList(line.split(separator));

        List<Long> separated_longs = new ArrayList<Long>();

        for (String separated_string : separated_strings) {
            separated_longs.add(Long.valueOf(separated_string));
        }

        return separated_longs;
    }

    public List<List<Long>> Trials()
    {
        List<List<Long>> list_of_line_lists = new ArrayList<List<Long>>();
        try
        {
            Scanner in = new Scanner(new File(name));
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
            FileWriter out = new FileWriter(name, true);

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

    public static List<Double> segmentsInSeconds(List<Long> trial)
    {
        List<Double> inSeconds = new ArrayList<Double>();

        for (Long segment : trial)
        {
            inSeconds.add((double) segment / 1000000000);
        }

        return inSeconds;
    }

    public static List<List<Double>> splitsInSeconds(List<List<Long>> trials)
    {
        List<List<Double>> inSeconds = new ArrayList<List<Double>>();

        for (List<Long> trial : trials)
        {
            inSeconds.add(segmentsInSeconds(trial));
        }

        return inSeconds;
    }

    public static List<Long> timeBetweenSegments(List<Long> split)
    {
        List<Long> segments_by_time = new ArrayList<Long>();

        segments_by_time.add(split.get(0));

        for (int i=1; i < split.size(); i++)
        {
            segments_by_time.add(split.get(i)-split.get(i-1));
        }

        return segments_by_time;
    }

    public static List<List<Long>> timeBetweenSegmentsForSplits(List<List<Long>> splits)
    {
        List<List<Long>> betweenSegments = new ArrayList<List<Long>>();

        for (List<Long> split : splits)
        {
            betweenSegments.add(timeBetweenSegments(split));
        }

        return betweenSegments;
    }
}
