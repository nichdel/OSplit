package net.hearthgate.osplit.libs;

import java.util.ArrayList;
import java.util.List;

public class SplitMath
{
  // Constants for units of time. Self explanatory.
  public static final long NANOSECONDS_PER_CENTISECONDS = 10000000;
  static final long SECOND = 100 * NANOSECONDS_PER_CENTISECONDS;
  static final long MINUTE = 60 * SECOND;
  static final long HOUR = 60 * MINUTE;
  static final long DAY = 24 * HOUR;

  /**
   * The splits of a run, in seconds.
   * @param run
   * @return
   */
  public static List<Double> convertSplitsToSeconds(List<Long> run) {
    List<Double> inSeconds = new ArrayList<Double>();

    for (Long split : run) {
      inSeconds.add((double) split / 1000000000);
    }

    return inSeconds;
  }

  /**
   * Return a list representing the time each split took
   * @param run
   * @return
   */
  public static List<Long> getTimeBetweenSplits(List<Long> run) {
    List<Long> splitsByTime = new ArrayList<Long>();

    splitsByTime.add(run.get(0));

    for (int i=1; i < run.size(); i++) {
      splitsByTime.add(run.get(i) - run.get(i - 1));
    }

    return splitsByTime;
  }

  /**
   * Returns a set of runs with the splits represented in time they took
   * @param list_of_runs
   * @return
   */
  public static List<List<Long>> getTimeBetweenSplitsInRuns(List<List<Long>> list_of_runs) {
    List<List<Long>> betweenSplits = new ArrayList<List<Long>>();

    for (List<Long> run : list_of_runs) {
      betweenSplits.add(getTimeBetweenSplits(run));
    }

    return betweenSplits;
  }

  /**
   * Converts nanoseconds to a human-readable format.
   * @param nanoseconds
   * @return
   */
  public static String convertNanoToTimeString(long nanoseconds) {

    // We subtract from this one
    // We need to continue to know the total to avoid, ie, omitting the seconds slot when there's exactly 4 minutes.
    long ns = nanoseconds;

    String inString = "";

    long milliseconds, seconds, minutes, hours, days;

    // TODO: Maybe this can be done in reverse, avoiding the two counts of nanoseconds.
    if (nanoseconds >= DAY) {
      days = ns / DAY;
      inString += days + ":";
      ns %= DAY;
    }

    if (nanoseconds >= HOUR) {
      hours = ns / HOUR;
      inString += addLeadingZeros(hours, 2) + ":";
      ns %= HOUR;
    }

    if (nanoseconds >= MINUTE) {
      minutes = ns / MINUTE;
      inString += addLeadingZeros(minutes, 2) + ":";
      ns %= MINUTE;
    }

    if (nanoseconds >= SECOND) {
      seconds = ns / SECOND;
      inString += addLeadingZeros(seconds, 2) + ".";
      ns %= SECOND;
    }

    if (nanoseconds >= NANOSECONDS_PER_CENTISECONDS) {
      milliseconds = ns / NANOSECONDS_PER_CENTISECONDS;
      inString += addLeadingZeros(milliseconds, 2);
    }

    return inString;
  }

  /**
   * Helper function to add the appropriate zeroes for formatting.
   * @param number
   * @param digits
   * @return
   */
  private static String addLeadingZeros(long number, int digits) {
    String num = String.valueOf(number);
    int zeros = digits - num.length();

    for (int i = 0; i < zeros; i++) {
      num = 0 + num;
    }
    return num;
  }
}
