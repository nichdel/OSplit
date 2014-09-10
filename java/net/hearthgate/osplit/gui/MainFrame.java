package net.hearthgate.osplit.gui;

import com.tulskiy.keymaster.common.Provider;
import net.hearthgate.osplit.libs.SplitFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Start preparing for config files and a config panel
public class MainFrame extends JFrame
{

  // Hotkey Provider
  final Provider provider = Provider.getCurrentProvider(true);

  JPanel currentPanel;
  private SplitFile currentSplitfile;

  // Buttons

  JButton fileButton;
  JButton splitButton;
  JButton statsButton;

  public MainFrame(SplitFile currentSplitfile) {
    setCurrentSplitfile(currentSplitfile);
    setLayout(new BorderLayout());

    createMenu();
  }

  /**
   * This will leave an intentionally null currentSplitfile variable
   */
  public MainFrame()
  {
    this(null);
  }

  /**
   * The menu leads to all other panels
   */
  private void createMenu() {
    final JToolBar jToolBar = new JToolBar();

    jToolBar.setFloatable(false);

    // FilePanel

    ActionListener fileButtonAction = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        swapPanels(new FilePanel(getCurrentSplitfile()));
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
        swapPanels(new SplitPanel(getCurrentSplitfile(), provider));
      }
    };

    splitButton = new JButton("Timer");
    splitButton.addActionListener(splitButtonAction);
    jToolBar.add(splitButton);
    splitButton.setEnabled(getCurrentSplitfile() != null);

    // StatsPanel

    statsButton = new JButton("Stats");
    // statsButton.addActionListener();
    jToolBar.add(statsButton);
    statsButton.setEnabled(getCurrentSplitfile() != null);
    statsButton.setVisible(false);

    // Hide/Show Button

    jToolBar.add(Box.createHorizontalGlue());
    // TODO: A way to hide the top bar
    final JLabel hideNote = new JLabel("");
    jToolBar.add(hideNote);

    add(jToolBar, BorderLayout.NORTH);

    if (currentSplitfile == null) {
      statsButton.setEnabled(false);
      splitButton.setEnabled(false);
    }
  }

  /**
   * Functions to clean up when a panel is switched.
   * @param newPanel
   */
  private void swapPanels(JPanel newPanel) {
    if (currentPanel != null) {
      remove(currentPanel);
    }

    resetProvider();
    currentPanel = newPanel;
    add(currentPanel, BorderLayout.CENTER);
    revalidate();
  }

  public SplitFile getCurrentSplitfile() {
    return currentSplitfile;
  }

  /**
   * Sets currentSplitfile as well as enabling menus.
   * @param currentSplitfile
   */
  public void setCurrentSplitfile(SplitFile currentSplitfile) {
    if (currentSplitfile != null) {
      this.currentSplitfile = currentSplitfile;
      splitButton.setEnabled(true);
      statsButton.setEnabled(true);
    }
  }

  private void resetProvider()
  {
    provider.reset();
  }
}
