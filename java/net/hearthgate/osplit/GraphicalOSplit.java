package net.hearthgate.osplit;

import net.hearthgate.osplit.gui.MainFrame;
import net.hearthgate.osplit.libs.SplitConfig;
import net.hearthgate.osplit.libs.SplitFile;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GraphicalOSplit {
  /**
   * The main method for the GUI-Driven version of OSplit
   * @param args At most one argument, specifying the location of a splitfile
   */
  public static void main(String[] args) {
    // Ensure Correct Call Signature
    if (args.length > 1) {
      System.out.println("Too many arguments. \n" +
          "Usage: OSplit.jar [splitfile.csv]");
      System.exit(-1);
    }

    // Load configuration
    try {
      SplitConfig.readConfig("src/main/config.txt");
    } catch (IOException e) {
      SplitConfig.setDefaults();
    }

    // Start the Main GUI, with the splitfile if specified
    MainFrame frame;
    if (args.length == 0) {
      frame = new MainFrame();
    } else {
      try {
        frame = new MainFrame(new SplitFile(args[0]));
      } catch (FileNotFoundException e) {
        System.out.println("File does not exist. Starting empty session");
        frame = new MainFrame();
      }
    }

    // TODO: Is it best practice to put these here or in the Frame constructor?
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // TODO: More appropriate dimensions (for the 99% that use a floating wm)
    frame.setSize(500, 500);
    frame.setVisible(true);
  }
}
