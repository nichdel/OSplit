package net.hearthgate.osplit;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import net.hearthgate.osplit.libs.SplitFile;
import net.hearthgate.osplit.libs.SplitMath;
import net.hearthgate.osplit.libs.SplitStats;
import net.hearthgate.osplit.libs.SplitTimer;

import javax.swing.*;
import java.io.FileNotFoundException;

public class TerminalOSplit {
  static SplitTimer timer;
  static SplitFile split;

  static long time;

  static Provider provider;

  /**
   * The terminal version of this software.
   * @param args At most one argument, specifying the location of a splitfile
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Too few or too many arguments. Please run with exactly one argument, " +
          "the filePath of a .csv file containing split information.");
    } else {
      timer = null;
      try {
        split = new SplitFile(args[0]);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      provider = Provider.getCurrentProvider(false);

      HotKeyListener listener = new HotKeyListener() {
        public void onHotKey(HotKey hotKey) {
          pressHotkey();
        }
      };

      provider.register(KeyStroke.getKeyStroke("control 0"), listener);

      if (!split.getTrials().isEmpty()) {
        System.out.print("Personal Best:");
        System.out.println(SplitMath.convertSplitsToSeconds(SplitMath.getTimeBetweenSplits(SplitStats.getPersonalBest(split.getTrials()))));
        System.out.print("Best of Segments:");
        System.out.println(SplitMath.convertSplitsToSeconds(SplitStats.getBestSplits(split.getTrials())));
        System.out.print("Averages:");
        System.out.println(SplitMath.convertSplitsToSeconds(SplitStats.getSplitAverages(split.getTrials())));
      }
    }
  }

  private static boolean pressHotkey() {
    if (timer == null) {
      timer = new SplitTimer(split.headers.size());
      System.out.println("TIMER STARTED");
      return false;
    } else {
      if (timer.isCounting()) {
        time = timer.timeSegment();
        double timeInSeconds = ((double) time) / 1000000000.0;
        System.out.print("TIME: ");
        System.out.println(timeInSeconds);
      } else {
        System.out.print("TIMER RESULTS: ");
        System.out.println(timer.times);
        split.appendTrial(timer.times);
        provider.reset();
        provider.stop();
        System.exit(0);
      }
      return true;
    }
  }
}
