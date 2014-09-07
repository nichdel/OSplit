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
    private SplitFile current_splitfile;

    private List<String> headerList;
    private List<List<Long>> segmentList;
    private List<JTextArea> headerEditors;
    private List<JButton> segmentRemovers;

    // These control (and set the defaults of) the way that split timings are viewed
    // TODO: Replace seconds with the human readable time function in SplitMath
    private boolean inSeconds = true;
    private boolean betweenSegments = true;

    JPanel filePanel;
    JPanel listingPanel;
    JPanel viewPanel;

    JButton save;

    /**
     *
     * @param current_splitfile
     */
    public FilePanel(SplitFile current_splitfile)
    {
        setLayout(new BorderLayout());

        if (current_splitfile != null) {
            // THIS IS AN INTENTIONAL HACK
            this.current_splitfile = current_splitfile;
            segmentList = new ArrayList<List<Long>>();

            for (List<Long> trial : current_splitfile.Trials()) {
                segmentList.add(trial);
            }

            headerList = new ArrayList<String>();

            for (String part : current_splitfile.headers) {
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
                        setCurrent_splitfile(new SplitFile(chooser.getSelectedFile().getAbsolutePath()));
                        segmentList = new ArrayList<List<Long>>();
                        for (List<Long> trial : getCurrent_splitfile().Trials()) {
                            segmentList.add(trial);
                        }
                        headerList = new ArrayList<String>(getCurrent_splitfile().headers);
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

                    setCurrent_splitfile(new SplitFile(chooser.getSelectedFile().getAbsolutePath(), headerList));

                    for (List<Long> segment : segmentList)
                    {
                        getCurrent_splitfile().AppendTrial(segment);
                    }

                    System.out.println("Saved.");
                    UpdateListings();
                }
            }

        };

        save = new JButton("Save");
        save.addActionListener(saveAction);
        filePanel.add(save);

        ActionListener CreateNew = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                headerList = new ArrayList<String>();
                headerList.add("Unknown");

                segmentList = new ArrayList<List<Long>>();

                setCurrent_splitfile(new SplitFile("temp_splits", headerList));

                UpdateListings();
            }
        };

        JButton create = new JButton("Create New");
        create.addActionListener(CreateNew);
        filePanel.add(create);

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


        if (getCurrent_splitfile() != null)
        {
            listingPanel = new JPanel();
            listingPanel.setLayout(new BorderLayout());

            // Headers

            final JPanel headers = new JPanel();
            headers.setLayout(new GridLayout(1, getCurrent_splitfile().headers.size()+1));

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

            List<List<Long>> trials = getCurrent_splitfile().Trials();

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

                List<List<Long>> runs = segmentList;

                if (betweenSegments)
                    runs = SplitMath.timeBetweenSplitsInRuns(runs);

                for (List<Long> run : runs)
                {
                    if (inSeconds)
                        for (Double segment : SplitMath.splitsInSeconds(run)) {
                            segments.add(new JLabel((String.valueOf((double) Math.round(segment * 1000) / 1000))));
                        }
                    else
                        for (Long segment : run) {
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
        if (getCurrent_splitfile() != null) {
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
        }
        else
        {
            save.setEnabled(false);
        }
    }

    public SplitFile getCurrent_splitfile() {
        return current_splitfile;
    }

    public void setCurrent_splitfile(SplitFile current_splitfile) {
        this.current_splitfile = current_splitfile;
        ((MainFrame) this.getTopLevelAncestor()).setCurrent_splitfile(current_splitfile);
        save.setEnabled(true);
    }
}