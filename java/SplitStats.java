import java.util.ArrayList;
import java.util.List;

// FIXME: I need to consider standardizing how these are calculated and displayed.
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

    public static List<Long> SegmentAverages(List<List<Long>> splits)
    {
        final int split_count = splits.size();
        final int segments = splits.get(0).size();
        List<Long> averaged_segments = new ArrayList<Long>();
        final List<List<Long>> inSeconds = SplitFile.timeBetweenSegmentsForSplits(splits);

        for (int i = 0; i < segments; i++)
        {
            long accumulator = 0;
            for (List<Long> split : inSeconds)
            {
                accumulator += split.get(i);
            }
            averaged_segments.add(accumulator / (long) split_count);
        }

        return averaged_segments;
    }
}
