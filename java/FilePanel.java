import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

// TODO: Open, Create, and Save As functions
public class FilePanel extends JPanel
{
    public SplitFile currentSplit;

    public FilePanel(SplitFile currentSplit)
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SplitFiles (.csv)", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            this.currentSplit = new SplitFile(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
