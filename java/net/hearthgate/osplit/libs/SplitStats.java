package net.hearthgate.osplit.libs;

import java.util.ArrayList;
import java.util.List;

// TODO: I need to consider standardizing how these are calculated and displayed.
// IE, getBestSplits comes out in time between segments, but Personal Best does not.

public class SplitStats {

  public static List<Long> getPersonalBest(List<List<Long>> splits) {
    final int splitLengths = splits.get(0).size();
    List<Long> bestSplit = splits.get(0);

    for (List<Long> split : splits) {
      if (split.get(splitLengths - 1) < bestSplit.get(splitLengths - 1)) {
        bestSplit = split;
      }
    }

    return bestSplit;
  }

  public static List<Long> getBestSplits(List<List<Long>> splits) {
    final int splitLength = splits.get(0).size();
    final List<List<Long>> inSeconds = SplitMath.getTimeBetweenSplitsInRuns(splits);

    List<Long> best = new ArrayList<Long>();

    for (int i=0; i < splits.get(0).size();i++) {
      best.add(inSeconds.get(0).get(i));
    }

    for (List<Long> split : inSeconds) {
      for (int j = 0; j < splitLength; j++) {
        if (split.get(j) < best.get(j)) {
          best.set(j, split.get(j));
        }
      }
    }

    return best;
  }

  public static List<Long> getSplitAverages(List<List<Long>> splits) {
    final int splitCount = splits.size();
    final int segments = splits.get(0).size();
    List<Long> averagedSplits = new ArrayList<Long>();
    final List<List<Long>> inSeconds = SplitMath.getTimeBetweenSplitsInRuns(splits);

    for (int i = 0; i < segments; i++) {
      long accumulator = 0;
      for (List<Long> split : inSeconds) {
        accumulator += split.get(i);
      }
      averagedSplits.add(accumulator / (long) splitCount);
    }

    return averagedSplits;
  }
}
