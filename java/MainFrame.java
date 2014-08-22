import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class MainFrame extends JFrame
{
    public MainFrame(final SplitFile split)
    {
        setLayout(new BorderLayout());

        CreateMenu();

        final SplitPanel frame = new SplitPanel(split);

        add(frame, BorderLayout.CENTER);
    }

    public MainFrame()
    {
        setLayout(new BorderLayout());

        CreateMenu();

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SplitFiles (.csv)", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            final SplitPanel frame = new SplitPanel(new SplitFile(chooser.getSelectedFile().getAbsolutePath()));
            add(frame, BorderLayout.CENTER);
        }
    }



    private void CreateMenu()
    {

        final JToolBar jToolBar = new JToolBar();

        jToolBar.setFloatable(false);
        jToolBar.add(new JButton("File"));

        add(jToolBar, BorderLayout.NORTH);

    }
}
