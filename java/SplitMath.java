import java.util.ArrayList;
import java.util.List;

public class SplitMath
{
    static final long NANOSECONDS_PER_CENTISECONDS = 10000000;

    /**
     * The splits of a run, in seconds.
     * @param run
     * @return
     */
    public static List<Double> splitsInSeconds(List<Long> run)
    {
        List<Double> inSeconds = new ArrayList<Double>();

        for (Long split : run)
        {
            inSeconds.add((double) split / 1000000000);
        }

        return inSeconds;
    }

    /**
     * The splits for each run in a list of runs
     * @param list_of_runs
     * @return
     */
    public static List<List<Double>> runsInSeconds(List<List<Long>> list_of_runs)
    {
        List<List<Double>> inSeconds = new ArrayList<List<Double>>();

        for (List<Long> run : list_of_runs)
        {
            inSeconds.add(splitsInSeconds(run));
        }

        return inSeconds;
    }

    /**
     * Return a list representing the time each split took
     * @param run
     * @return
     */
    public static List<Long> timeBetweenSplits(List<Long> run)
    {
        List<Long> segments_by_time = new ArrayList<Long>();

        segments_by_time.add(run.get(0));

        for (int i=1; i < run.size(); i++)
        {
            segments_by_time.add(run.get(i)-run.get(i-1));
        }

        return segments_by_time;
    }

    /**
     * Returns a set of runs with the splits represented in time they took
     * @param list_of_runs
     * @return
     */
    public static List<List<Long>> timeBetweenSplitsInRuns(List<List<Long>> list_of_runs)
    {
        List<List<Long>> betweenSegments = new ArrayList<List<Long>>();

        for (List<Long> run : list_of_runs)
        {
            betweenSegments.add(timeBetweenSplits(run));
        }

        return betweenSegments;
    }

    /**
     * Converts nanoseconds to a human-readable format.
     * @param nanoseconds
     * @return
     */
    static String NanoToTimeString(long nanoseconds)
    {
        // Constants for units of time. Self explanatory.
        final long SECOND = 100 * NANOSECONDS_PER_CENTISECONDS;
        final long MINUTE = 60 * SECOND;
        final long HOUR = 60 * MINUTE;
        final long DAY = 24 * HOUR;

        // We subtract from this one
        // We need to continue to know the total to avoid, ie, omitting the seconds slot when there's exactly 4 minutes.
        long ns = nanoseconds;

        String in_string = "";

        long milliseconds, seconds, minutes, hours, days;

        // TODO: Maybe this can be done in reverse, avoiding the two counts of nanoseconds.
        if (nanoseconds >= DAY)
        {
            days = ns / DAY;
            in_string += days + ":";
            ns %= DAY;
        }
        if (nanoseconds >= HOUR)
        {
            hours = ns / HOUR;
            in_string += AddLeadingZeros(hours, 2) + ":";
            ns %= HOUR;
        }
        if (nanoseconds >= MINUTE)
        {
            minutes = ns / MINUTE;
            in_string += AddLeadingZeros(minutes, 2) + ":";
            ns %= MINUTE;
        }
        if (nanoseconds >= SECOND)
        {
            seconds = ns / SECOND;
            in_string += AddLeadingZeros(seconds, 2) + ".";
            ns %= SECOND;
        }
        if (nanoseconds >= NANOSECONDS_PER_CENTISECONDS)
        {
            milliseconds = ns / NANOSECONDS_PER_CENTISECONDS;
            in_string += AddLeadingZeros(milliseconds, 2);
        }

        return in_string;
    }

    /**
     * Helper function to add the appropriate zeroes for formatting.
     * @param number
     * @param digits
     * @return
     */
    private static String AddLeadingZeros(long number, int digits)
    {
        String num = String.valueOf(number);
        int zeros = digits - num.length();

        for (int i = 0; i < zeros; i++) {
            num = 0 + num;
        }
        return num;
    }
}
