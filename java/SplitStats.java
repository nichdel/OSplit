import java.util.ArrayList;
import java.util.List;

public class SplitStats
{
    public static List<Float> PersonalBest(List<List<Float>> splits)
    {
        final int split_lengths = splits.get(0).size();
        List<Float> best_split = splits.get(0);

        for (List<Float> split : splits) {
            if (split.get(split_lengths - 1) < best_split.get(split_lengths - 1)) {
                best_split = split;
            }
        }

        return best_split;
    }

    public static List<Float> BestSegments(List<List<Float>> splits)
    {
        final int split_length = splits.get(0).size();
        List<Float> best = new ArrayList<Float>();

        for (int i=0; i < splits.get(0).size();i++)
        {
            best.add(splits.get(0).get(i));
        }

        for (List<Float> split : splits) {
            for (int j = 0; j < split_length; j++) {
                if (split.get(j) < best.get(j)) {
                    best.set(j, split.get(j));
                }
            }
        }

        return best;
    }
}
