import java.util.ArrayList;
import java.util.List;

// TODO: I need to consider standardizing how these are calculated and displayed.
// IE, BestSegments comes out in time between segments, but Personal Best does not.

public class SplitStats
{
    public static List<Long> PersonalBest(List<List<Long>> splits)
    {
        final int split_lengths = splits.get(0).size();
        List<Long> best_split = splits.get(0);

        for (List<Long> split : splits) {
            if (split.get(split_lengths - 1) < best_split.get(split_lengths - 1)) {
                best_split = split;
            }
        }

        return best_split;
    }

    public static List<Long> BestSegments(List<List<Long>> splits)
    {
        final int split_length = splits.get(0).size();
        final List<List<Long>> inSeconds = SplitFile.timeBetweenSegmentsForSplits(splits);

        List<Long> best = new ArrayList<Long>();

        for (int i=0; i < splits.get(0).size();i++)
        {
            best.add(inSeconds.get(0).get(i));
        }

        for (List<Long> split : inSeconds) {
            for (int j = 0; j < split_length; j++) {
                if (split.get(j) < best.get(j)) {
                    best.set(j, split.get(j));
                }
            }
        }

        return best;
    }
}
