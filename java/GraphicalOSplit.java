import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;

public class GraphicalOSplit
{
    static SplitTimer timer;
    static SplitFile split;
    static Provider provider;

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Too few or too many arguments. Please run with exactly one argument, the name of a .csv file containing split information.");
        }
        else
        {
            timer = null;
            split = new SplitFile(args[0]);

            if (!split.Trials().isEmpty())
            {
                System.out.print("Personal Best:");
                System.out.println(SplitFile.segmentsInSeconds(SplitFile.timeBetweenSegments(SplitStats.PersonalBest(split.Trials()))));
                System.out.print("Best of Segments:");
                System.out.println(SplitFile.segmentsInSeconds(SplitStats.BestSegments(split.Trials())));
                System.out.print("Averages:");
                System.out.println(SplitFile.segmentsInSeconds(SplitStats.SegmentAverages(split.Trials())));

            }
        }

        final SplitFrame frame = new SplitFrame(split);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);

        frame.setVisible(true);

        provider = Provider.getCurrentProvider(false);

        HotKeyListener listener = new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
                if (!frame.started)
                {
                    timer = new SplitTimer(split.parts.size());
                    frame.StartTiming();
                }
                else
                {
                    if (!frame.finished)
                    {
                        long result = timer.TimeSegment();
                        frame.AdvanceSplit(result);
                    }
                    else
                    {
                        split.AppendLine(timer.times);
                        provider.reset();
                        provider.stop();
                        System.exit(0);
                    }
                }
            }
        };

        provider.register(KeyStroke.getKeyStroke("control 0"), listener);
    }
}
