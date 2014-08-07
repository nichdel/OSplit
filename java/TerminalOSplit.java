import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;

public class TerminalOSplit
{
    static SplitTimer timer;
    static SplitFile split;
    static long time;

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

            final Provider provider = Provider.getCurrentProvider(false);

            HotKeyListener listener = new HotKeyListener() {
                public void onHotKey(HotKey hotKey) {
                    Pressed();
                }
            };

            provider.register(KeyStroke.getKeyStroke("control 0"), listener);
        }

    }

    private static boolean Pressed()
    {
        if (timer == null)
        {
            timer = new SplitTimer(split.parts.size());
            System.out.println("TIMER BEGAN");
            return false;
        }
        else
        {
            if (timer.isCounting())
            {
                time = timer.TimeSegment();
                double timeInSeconds = ((double) time) / 1000000000.0;
                System.out.print("TIME: ");
                System.out.println(timeInSeconds);
            }
            else
            {
                System.out.print("TIMER RESULTS: ");
                System.out.println(timer.times);
                split.AppendLine(timer.times);
            }
            return true;
        }
    }
}