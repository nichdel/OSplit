import javax.swing.*;

public class GraphicalOSplit
{
    static SplitTimer timer;
    static SplitFile split;

    public static void main(String[] args)
    {
        if (args.length > 1)
        {
            System.out.println("Too few or too many arguments. Please run with exactly one argument, the name of a .csv file containing split information.");
        }
        else if (args.length == 0)
        {
            final MainFrame frame = new MainFrame();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500,500);
            frame.setVisible(true);
        }
        else
        {
            timer = null;
            split = new SplitFile(args[0]);

            if (!split.Trials().isEmpty())
            {
                // TODO: Standardize this shit.
                System.out.print("Personal Best:");
                System.out.println(SplitFile.segmentsInSeconds(SplitFile.timeBetweenSegments(SplitStats.PersonalBest(split.Trials()))));
                System.out.print("Best of Segments:");
                System.out.println(SplitFile.segmentsInSeconds(SplitStats.BestSegments(split.Trials())));
                System.out.print("Averages:");
                System.out.println(SplitFile.segmentsInSeconds(SplitStats.SegmentAverages(split.Trials())));

            }
        }


        final MainFrame frame = new MainFrame(split);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
    }
}
