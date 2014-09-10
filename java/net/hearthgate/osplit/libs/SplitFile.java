package net.hearthgate.osplit.libs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SplitFile {
  final static String separator = ",";

  public String filePath;
  public String fileName;
  public List<String> headers;

  /**
   * Attempts to open an existing SplitFile
   * @param filePath Absolute file path
   */
  public SplitFile(String filePath) throws FileNotFoundException {
    this.filePath = filePath;
    fileName = (new File(filePath)).getName().split("\\.")[0];

    Scanner in = new Scanner(new File(this.filePath));
    headers = Arrays.asList(in.nextLine().split(separator));
    in.close();
  }

  /**
   * Attempts to create a new file
   * @param filePath Absolute file path, if it doesn't end in .csv we add it
   * @param headers List of headers
   */
  public SplitFile(String filePath, List<String> headers) {
    this.filePath = filePath;
    if (!this.filePath.contains(".csv")) {
      this.filePath += ".csv";
    }
    this.headers = headers;

    FileWriter out;
    try {
      out = new FileWriter(this.filePath);

      for (int i = 0; i < headers.size(); i++) {
        out.write(headers.get(i));
        if (i != headers.size()-1) {
          out.write(separator);
        }
      }
      out.write("\n");
      out.close();
    } catch (IOException exception) {
      System.out.println("Something went very wrong. If this happens more than once go to " +
          "https://github.com/nichdel/OSplit/issues and chew my ear off.");
    }
  }

  /**
   * Helper function to separate lines correctly.
   * @param line A string such as "Header 1,Header 2,Header 3"
   * @return A list such as ["Header 1", "Header 2", "Header 3"]
   */
  private static List<Long> separate(String line) {
    final List<String> separatedStrings = Arrays.asList(line.split(separator));

    List<Long> separated_longs = new ArrayList<Long>();

    for (String separatedString : separatedStrings) {
      separated_longs.add(Long.valueOf(separatedString));
    }

    return separated_longs;
  }

  /**
   * Check the file for a list of trials
   * @return A list of trials, which are lists of nanoseconds from start.
   */
  public List<List<Long>> getTrials() {
    // TODO: Rename this
    List<List<Long>> listOfLineLists = new ArrayList<List<Long>>();
    try {
      Scanner in = new Scanner(new File(filePath));
      in.nextLine(); // Disregard the first line.
      while (in.hasNext()) {
        listOfLineLists.add(separate(in.next()));
      }
      in.close();
    } catch (IOException exception) {
      System.out.println("oops");
    }
    return listOfLineLists;
  }

  /**
   * Add a trial to the end of the file.
   * @param trial A set of timings for a single trial
   * @return whether successful
   */
  public boolean appendTrial(List<Long> trial) {
    try {
      FileWriter out = new FileWriter(filePath, true);

      for (int i = 0; i < trial.size(); i++) {
        out.write(String.valueOf(trial.get(i)));

        if (i != trial.size()-1) {
          out.write(separator);
        }
      }
      out.write("\n");
      out.close();
      return true;
    } catch (IOException exception) {
      return false;
    }
  }
}