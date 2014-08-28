import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// FIXME: Move the provider declarations here so they're available for panels.
public class MainFrame extends JFrame
{

    JPanel currentPanel;
    SplitFile currentSplit;

    // Buttons

    JButton fileButton;
    JButton splitButton;
    JButton statsButton;

    public MainFrame(final SplitFile split)
    {
        currentSplit = split;
        setLayout(new BorderLayout());

        CreateMenu();
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

        // FilePanel

        ActionListener fileButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SwapPanels();
                currentPanel = new FilePanel(currentSplit);
                add(currentPanel, BorderLayout.CENTER);
                revalidate();
            }
        };



        fileButton = new JButton("File");
        fileButton.addActionListener(fileButtonAction);
        jToolBar.add(fileButton);

        // SplitPanel

        ActionListener splitButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SwapPanels();
                currentPanel = new SplitPanel(currentSplit);
                add(currentPanel, BorderLayout.CENTER);
                setVisible(true);
                revalidate();
            }
        };

        splitButton = new JButton("Timer");
        splitButton.addActionListener(splitButtonAction);
        jToolBar.add(splitButton);
        splitButton.setEnabled(currentSplit != null);

        // StatsPanel

        statsButton = new JButton("Stats");
        // statsButton.addActionListener();
        jToolBar.add(statsButton);
        statsButton.setEnabled(currentSplit != null);
        statsButton.setVisible(false);

        // Hide/Show Button

        jToolBar.add(Box.createHorizontalGlue());
        // FIXME: Make this not a lie.
        final JLabel hideNote = new JLabel("");
        jToolBar.add(hideNote);

        add(jToolBar, BorderLayout.NORTH);

    }

    private void SwapPanels()
    {
        // FIXME: Figure out the best way to pass and instantiate classes with parameters
        if (currentPanel instanceof SplitPanel)
        {
            ((SplitPanel) currentPanel).UnregisterHotkeys();
        }
        else if (currentPanel instanceof FilePanel)
        {
            currentSplit = ((FilePanel) currentPanel).currentSplit;
        }
        if (currentPanel != null)
        {
            remove(currentPanel);
        }

        boolean splitOpened = (currentSplit != null);

        splitButton.setEnabled(splitOpened);
        statsButton.setEnabled(splitOpened);
    }
}
