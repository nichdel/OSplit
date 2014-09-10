package net.hearthgate.osplit.gui;

import net.hearthgate.osplit.libs.SplitFile;
import net.hearthgate.osplit.libs.SplitMath;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FilePanel extends JPanel
{
  private SplitFile currentSplitfile;

  private List<String> headerList;
  private List<List<Long>> segmentList;
  private List<JTextArea> headerEditors;
  private List<JButton> segmentRemovers;

  // These control (and set the defaults of) the way that split timings are viewed
  private boolean inHumanReadable = true;
  private boolean betweenSegments = true;

  private JPanel filePanel;
  private JPanel listingPanel;
  private JPanel viewPanel;

  JButton save;

  /**
   *
   * @param currentSplitfile
   */
  public FilePanel(SplitFile currentSplitfile) {
    setLayout(new BorderLayout());

    if (currentSplitfile != null) {
      // THIS IS AN INTENTIONAL HACK
      this.currentSplitfile = currentSplitfile;
      segmentList = new ArrayList<List<Long>>();

      for (List<Long> trial : currentSplitfile.getTrials()) {
        segmentList.add(trial);
      }

      headerList = new ArrayList<String>();

      for (String part : currentSplitfile.headers) {
        headerList.add(part);
      }
    }

    // FILE IO
    add(makeFileControls(), BorderLayout.NORTH);

    // makeListings
    add(makeListings(), BorderLayout.CENTER);

    // View Controls
    add(makeViewControls(), BorderLayout.SOUTH);
  }

  /**
   *
   * @return
   */
  private JPanel makeFileControls() {
    filePanel = new JPanel();

    // Opening
    ActionListener openAction = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("SplitFiles (.csv)", "csv"));
        int returnVal = chooser.showOpenDialog((java.awt.Component) e.getSource());
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          File f = new File(chooser.getSelectedFile().getAbsolutePath());
          if (f.exists()) {
            try {
              setCurrentSplitfile(new SplitFile(chooser.getSelectedFile().getAbsolutePath()));
            } catch (FileNotFoundException e1) {
              e1.printStackTrace();
            }
            segmentList = new ArrayList<List<Long>>();
            for (List<Long> trial : getCurrentSplitfile().getTrials()) {
              segmentList.add(trial);
            }
            headerList = new ArrayList<String>(getCurrentSplitfile().headers);
            updateListings();
          }
        }
      }
    };

    JButton open = new JButton("Open");
    open.addActionListener(openAction);
    filePanel.add(open);

    // Saving
    ActionListener saveAction = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Specify a file to save");
        chooser.setFileFilter(new FileNameExtensionFilter("Splitfiles (.csv)", "csv"));

        int returnValue = chooser.showSaveDialog((java.awt.Component) e.getSource());

        if (returnValue == JFileChooser.APPROVE_OPTION) {
          for (int i=0; i < headerList.size(); i++) {
            headerList.set(i, headerEditors.get(i).getText());
          }

          setCurrentSplitfile(new SplitFile(chooser.getSelectedFile().getAbsolutePath(), headerList));

          for (List<Long> segment : segmentList) {
            getCurrentSplitfile().appendTrial(segment);
          }

          System.out.println("Saved.");
          updateListings();
        }
      }
    };

    save = new JButton("Save");
    save.addActionListener(saveAction);
    filePanel.add(save);

    ActionListener createNew = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        headerList = new ArrayList<String>();
        headerList.add("Unknown");

        segmentList = new ArrayList<List<Long>>();

        setCurrentSplitfile(new SplitFile("temp_splits", headerList));

        updateListings();
      }
    };

    JButton create = new JButton("Create New");
    create.addActionListener(createNew);
    filePanel.add(create);

    return filePanel;

  }

  /**
   *
   * @return
   */
  private JPanel makeListings()
  {

    JPanel listingWrapPanel = new JPanel();
    listingWrapPanel.setLayout(new BorderLayout());

    if (listingPanel != null) {
      listingPanel.getParent().remove(listingPanel);
    }


    if (getCurrentSplitfile() != null) {
      listingPanel = new JPanel();
      listingPanel.setLayout(new BorderLayout());

      // Headers

      final JPanel headers = new JPanel();
      headers.setLayout(new GridLayout(1, getCurrentSplitfile().headers.size()+1));

      headerEditors = new ArrayList<JTextArea>();

      for (String aHeaderList : headerList) {
        JTextArea jTextArea = new JTextArea(aHeaderList);
        headerEditors.add(jTextArea);

        headers.add(jTextArea);
      }

      JPanel addRemove = new JPanel();
      addRemove.setLayout(new GridLayout(2,1));

      ActionListener addHeader = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          headerList.add("Unnamed");
          for (List<Long> trial : segmentList) {
            trial.add((long) 0);
          }
          updateListings();
        }
      };

      JButton add = new JButton("+");
      add.addActionListener(addHeader);

      ActionListener removeHeader = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          headerList.remove(headerList.size() - 1);
          for (List<Long> trial : segmentList) {
            trial.remove(trial.size() - 1);
          }
          updateListings();
        }
      };

      JButton rem = new JButton("-");
      rem.addActionListener(removeHeader);

      addRemove.add(add);
      addRemove.add(rem);

      headers.add(addRemove);

      listingPanel.add(headers, BorderLayout.NORTH);

      // Segments

      JPanel segments = new JPanel();

      List<List<Long>> trials = getCurrentSplitfile().getTrials();

      if (trials.size() > 0) {
        segments.setLayout(new GridLayout(segmentList.size(), trials.get(0).size() + 1));

        ActionListener segmentRemove = new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            segmentList.remove(segmentRemovers.indexOf(e.getSource()));
            updateListings();
          }
        };

        segmentRemovers = new ArrayList<JButton>();

        List<List<Long>> runs = segmentList;

        if (betweenSegments) {
          runs = SplitMath.getTimeBetweenSplitsInRuns(runs);
        }

        for (List<Long> run : runs) {
          if (inHumanReadable) {
            for (Long segment : run) {
              segments.add(new JLabel(SplitMath.convertNanoToTimeString(segment)));
            }
          } else {
            for (Long segment : run) {
              segments.add(new JLabel(segment.toString()));
            }
          }

          JButton jButton = new JButton("-");
          jButton.addActionListener(segmentRemove);
          segmentRemovers.add(jButton);
          segments.add(jButton);
        }
      }
      listingPanel.add(segments, BorderLayout.CENTER);
      listingWrapPanel.add(listingPanel, BorderLayout.NORTH);
    } else {
      save.setEnabled(false);
    }

    return listingWrapPanel;
  }

   /**
   *
   * @return
   */
  private JPanel makeViewControls()
  {
    viewPanel = new JPanel();

    ActionListener precisionToggler = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        inHumanReadable = (!inHumanReadable);
        updateListings();
      }
    };

    ActionListener modeToggler = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        betweenSegments = (!betweenSegments);
        updateListings();
      }
    };

    JButton precisionButton = new JButton("Readable/Nanoseconds");
    precisionButton.addActionListener(precisionToggler);
    viewPanel.add(precisionButton);

    JButton modeButton = new JButton("Total/Per Segment");
    modeButton.addActionListener(modeToggler);
    viewPanel.add(modeButton);

    return viewPanel;
  }

  /**
   *
   */
  private void updateListings()
  {
    if (getCurrentSplitfile() != null) {
      if (headerEditors == null)
      {
        headerEditors = new ArrayList<JTextArea>();
      }
      for (int i = 0; i < headerEditors.size() && i < headerList.size(); i++) {
        headerList.set(i, headerEditors.get(i).getText());
      }
      if (listingPanel != null)
        remove(listingPanel);
      add(makeListings(), BorderLayout.CENTER);
      revalidate();
    }
    else
    {
      save.setEnabled(false);
    }
  }

  public SplitFile getCurrentSplitfile() {
    return currentSplitfile;
  }

  public void setCurrentSplitfile(SplitFile currentSplitfile) {
    this.currentSplitfile = currentSplitfile;
    ((MainFrame) this.getTopLevelAncestor()).setCurrentSplitfile(currentSplitfile);
    save.setEnabled(true);
  }
}