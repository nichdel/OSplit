import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// FIXME: Move the provider declarations here so they're available for panels.
// TODO: Start preparing for config files and a config panel
public class MainFrame extends JFrame
{

    JPanel currentPanel;
    private SplitFile current_splitfile;

    // Buttons

    JButton fileButton;
    JButton splitButton;
    JButton statsButton;

    /**
     *
     * @param current_splitfile
     */
    public MainFrame(SplitFile current_splitfile)
    {
        setCurrent_splitfile(current_splitfile);
        setLayout(new BorderLayout());

        CreateMenu();
    }

    /**
     * This will leave an intentionally null current_splitfile variable
     */
    public MainFrame()
    {
        this(null);
    }

    /**
     * The menu leads to all other panels
     */
    private void CreateMenu()
    {
        final JToolBar jToolBar = new JToolBar();

        jToolBar.setFloatable(false);

        // FilePanel

        ActionListener fileButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SwapPanels(new FilePanel(getCurrent_splitfile()));
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
                SwapPanels(new SplitPanel(getCurrent_splitfile()));
            }
        };

        splitButton = new JButton("Timer");
        splitButton.addActionListener(splitButtonAction);
        jToolBar.add(splitButton);
        splitButton.setEnabled(getCurrent_splitfile() != null);

        // StatsPanel

        statsButton = new JButton("Stats");
        // statsButton.addActionListener();
        jToolBar.add(statsButton);
        statsButton.setEnabled(getCurrent_splitfile() != null);
        statsButton.setVisible(false);

        // Hide/Show Button

        jToolBar.add(Box.createHorizontalGlue());
        // TODO: A way to hide the top bar
        final JLabel hideNote = new JLabel("");
        jToolBar.add(hideNote);

        add(jToolBar, BorderLayout.NORTH);

        if (current_splitfile == null)
        {
            statsButton.setEnabled(false);
            splitButton.setEnabled(false);
        }
    }

    /**
     * Functions to clean up when a panel is switched.
     * @param newPanel
     */
    private void SwapPanels(JPanel newPanel)
    {
        if (currentPanel instanceof SplitPanel)
        {
            ((SplitPanel) currentPanel).UnregisterHotkeys();
        }

        if (currentPanel != null)
        {
            remove(currentPanel);
        }

        currentPanel = newPanel;
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
    }

    public SplitFile getCurrent_splitfile() {
        return current_splitfile;
    }

    public void setCurrent_splitfile(SplitFile current_splitfile) {
        if (current_splitfile != null) {
            this.current_splitfile = current_splitfile;
            splitButton.setEnabled(true);
            statsButton.setEnabled(true);
        }
    }
}
