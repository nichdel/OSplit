import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePanel extends JPanel
{
    public SplitFile currentSplit;

    private List<String> headerList;
    private List<List<Long>> segmentList;
    private List<JTextArea> headerEditors;
    private List<JButton> segmentRemovers;

    private boolean inSeconds = true;
    private boolean betweenSegments = true;

    JPanel filePanel;
    JPanel listingPanel;
    JPanel viewPanel;

    JButton save;

    public FilePanel(SplitFile currentSplit)
    {
        setLayout(new BorderLayout());

        this.currentSplit = currentSplit;

        if (currentSplit != null) {
            segmentList = new ArrayList<List<Long>>();

            for (List<Long> trial : currentSplit.Trials()) {
                segmentList.add(trial);
            }

            headerList = new ArrayList<String>();

            for (String part : currentSplit.parts) {
                headerList.add(part);
            }
        }
        // FILE IO
        add(FileControls(), BorderLayout.NORTH);

        // Listings
        add(Listings(), BorderLayout.CENTER);

        // View Controls
        add(ViewControls(), BorderLayout.SOUTH);
    }

    private JPanel FileControls()
    {
        filePanel = new JPanel();

        // Opening
        ActionListener openAction = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("SplitFiles (.csv)", "csv"));
                int returnVal = chooser.showOpenDialog((java.awt.Component) e.getSource());
                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File f = new File(chooser.getSelectedFile().getAbsolutePath());
                    if (f.exists())
                    {
                        currentSplit = new SplitFile(chooser.getSelectedFile().getAbsolutePath());
                        segmentList = new ArrayList<List<Long>>();
                        for (List<Long> trial : currentSplit.Trials()) {
                            segmentList.add(trial);
                        }
                        headerList = new ArrayList<String>(currentSplit.parts);
                        UpdateListings();
                    }
                }
            }

        };

        JButton open = new JButton("Open");
        open.addActionListener(openAction);
        filePanel.add(open);

        // Saving
        ActionListener saveAction = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Specify a file to save");
                chooser.setFileFilter(new FileNameExtensionFilter("Splitfiles (.csv)", "csv"));

                int returnValue = chooser.showSaveDialog((java.awt.Component) e.getSource());

                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    for (int i=0; i < headerList.size(); i++)
                    {
                        headerList.set(i, headerEditors.get(i).getText());
                    }

                    currentSplit = new SplitFile(chooser.getSelectedFile().getAbsolutePath(), headerList);

                    for (List<Long> segment : segmentList)
                    {
                        currentSplit.AppendLine(segment);
                    }

                    System.out.println("Saved.");
                    UpdateListings();
                }
            }

        };

        save = new JButton("Save");
        save.addActionListener(saveAction);
        filePanel.add(save);

        return filePanel;

    }

    private JPanel Listings()
    {

        JPanel listingWrapPanel = new JPanel();
        listingWrapPanel.setLayout(new BorderLayout());

        if (listingPanel != null)
        {
            listingPanel.getParent().remove(listingPanel);
        }


        if (currentSplit != null)
        {
            listingPanel = new JPanel();
            listingPanel.setLayout(new BorderLayout());

            // Headers

            final JPanel headers = new JPanel();
            headers.setLayout(new GridLayout(1, currentSplit.parts.size()+1));

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
                public void actionPerformed(ActionEvent e)
                {
                    headerList.add("Unnamed");
                    for (List<Long> trial : segmentList)
                    {
                        trial.add((long) 0);
                    }
                    UpdateListings();
                }
            };

            JButton add = new JButton("+");
            add.addActionListener(addHeader);

            ActionListener removeHeader = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    headerList.remove(headerList.size() - 1);
                    for (List<Long> trial : segmentList)
                    {
                        trial.remove(trial.size() - 1);
                    }
                    UpdateListings();
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

            List<List<Long>> trials = currentSplit.Trials();

            if (trials.size() > 0)
            {
                segments.setLayout(new GridLayout(segmentList.size(), trials.get(0).size() + 1));

                ActionListener segmentRemove = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        segmentList.remove(segmentRemovers.indexOf(e.getSource()));
                        UpdateListings();
                    }
                };

                segmentRemovers = new ArrayList<JButton>();

                List<List<Long>> converted_trials = segmentList;

                if (betweenSegments)
                    converted_trials = SplitFile.timeBetweenSegmentsForSplits(converted_trials);

                for (List<Long> trial : converted_trials)
                {
                    if (inSeconds)
                        for (Double segment : SplitFile.segmentsInSeconds(trial)) {
                            segments.add(new JLabel((String.valueOf((double) Math.round(segment * 1000) / 1000))));
                        }
                    else
                        for (Long segment : trial) {
                            segments.add(new JLabel(segment.toString()));
                        }

                    JButton jButton = new JButton("-");
                    jButton.addActionListener(segmentRemove);
                    segmentRemovers.add(jButton);
                    segments.add(jButton);
                }
            }
            listingPanel.add(segments, BorderLayout.CENTER);
            listingWrapPanel.add(listingPanel, BorderLayout.NORTH);
        }
        else
        {
            save.setEnabled(false);
        }

        return listingWrapPanel;
    }

    private JPanel ViewControls()
    {
        viewPanel = new JPanel();

        ActionListener precisionToggler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                inSeconds = (!inSeconds);
                UpdateListings();
            }
        };

        ActionListener modeToggler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                betweenSegments = (!betweenSegments);
                UpdateListings();
            }
        };

        JButton precisionButton = new JButton("Seconds/NanoSeconds");
        precisionButton.addActionListener(precisionToggler);
        viewPanel.add(precisionButton);

        JButton modeButton = new JButton("Total/Per Segment");
        modeButton.addActionListener(modeToggler);
        viewPanel.add(modeButton);

        return viewPanel;
    }

    private void UpdateListings()
    {
        if (currentSplit != null) {
            if (headerEditors == null)
            {
                headerEditors = new ArrayList<JTextArea>();
            }
            for (int i = 0; i < headerEditors.size() && i < headerList.size(); i++) {
                headerList.set(i, headerEditors.get(i).getText());
            }
            if (listingPanel != null)
                remove(listingPanel);
            add(Listings(), BorderLayout.CENTER);
            revalidate();
            save.setEnabled(true);
            ((MainFrame) this.getTopLevelAncestor()).splitButton.setEnabled(true);
            ((MainFrame) this.getTopLevelAncestor()).statsButton.setEnabled(true);
        }
        else
        {
            save.setEnabled(false);
        }
    }
}