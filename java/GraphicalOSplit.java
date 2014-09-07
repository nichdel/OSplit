import javax.swing.*;
import java.io.File;

// TODO: Learn best practice for comments in Java.

public class GraphicalOSplit
{
    private static MainFrame frame;

    public static void main(String[] args)
    {
        if (args.length > 1)
            System.out.println("Too many arguments. Run with one argument (a splitfile)" +
                    " or no arguments and choose a file with the GUI.");
        else if (args.length == 0)
            frame = new MainFrame();
        else if (new File(args[0]).isFile())
            frame = new MainFrame(new SplitFile(args[0]));
        else
            System.out.println("File does not exist. Either specify an existing file or run without arguments" +
                    "and create a file within the GUI.");

        // TODO: Is it best practice to put these here or in the Frame constructor?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // TODO: More appropriate dimensions (for the 99% that use a floating wm)
        frame.setSize(500,500);
        frame.setVisible(true);
    }
}
