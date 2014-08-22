import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Make a panel specifically for file opening, closing, and editing
public class MainFrame extends JFrame
{

    JPanel currentPanel;
    SplitFile currentSplit;

    // Buttons

    JButton fileButton;
    JButton splitButton;

    public MainFrame(final SplitFile split)
    {
        currentSplit = split;
        setLayout(new BorderLayout());

        CreateMenu();

        final SplitPanel frame = new SplitPanel(split);

        add(frame, BorderLayout.CENTER);
    }

    public MainFrame()
    {
        setLayout(new BorderLayout());

        CreateMenu();
    }


    private void CreateMenu()
    {
        final JToolBar jToolBar = new JToolBar();

        jToolBar.setFloatable(false);

        ActionListener fileButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                FilePanel filePanel = new FilePanel(currentSplit);
                currentSplit = filePanel.currentSplit;
                SwapPanels(filePanel);
            }
        };

        fileButton = new JButton("File");
        fileButton.addActionListener(fileButtonAction);
        jToolBar.add(fileButton);

        ActionListener splitButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SwapPanels(new SplitPanel(currentSplit));
            }
        };

        splitButton = new JButton("Timer");
        splitButton.addActionListener(splitButtonAction);
        jToolBar.add(splitButton);
        splitButton.setEnabled(false);


        jToolBar.add(Box.createHorizontalGlue());
        // FIXME: Make this not a lie.
        final JLabel hideNote = new JLabel("Press Esc to show or hide this bar.");
        jToolBar.add(hideNote);

        add(jToolBar, BorderLayout.NORTH);

    }

    private void SwapPanels(JPanel newPanel)
    {
        if (currentPanel != null)
        {
            remove(currentPanel);
        }
        currentPanel = newPanel;
        add(currentPanel, BorderLayout.CENTER);
        setVisible(true);

        if (currentSplit == null)
        {
            splitButton.setEnabled(false);
        }
        else
        {
            splitButton.setEnabled(true);
        }
    }
}
