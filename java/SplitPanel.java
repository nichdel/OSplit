import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SplitPanel extends JPanel
{

    private List<JLabel> time_list;
    private int current;
    private long time;
    private JLabel total;
    private Timer timer;
    private SplitTimer splitTimer;
    private SplitFile split;
    public boolean started = false;
    public boolean finished = false;
    Provider provider = Provider.getCurrentProvider(true);

    /**
     * The panel that you use while splitting
     * @param split
     */
    public SplitPanel(SplitFile split)
    {
        this.split = split;
        final int rows = split.headers.size();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel(split.file_name, JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);

        JPanel split_panel = new JPanel();
        split_panel.setLayout(new GridLayout(rows, 2));

        List<JLabel> label_list = new ArrayList<JLabel>();
        time_list = new ArrayList<JLabel>();
        for (String segment : split.headers)
        {
            JLabel label = new JLabel(segment, JLabel.CENTER);
            label_list.add(label);
            split_panel.add(label);
            JLabel time = new JLabel("...", JLabel.LEFT);
            time_list.add(time);
            split_panel.add(time);
        }

        current = 0;

        panel.add(split_panel, BorderLayout.CENTER);

        total = new JLabel("...", JLabel.CENTER);
        panel.add(total, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        RegisterHotkeys();
    }

    /**
     * Starts both the real timer (SplitTimer) and the graphical one
     */
    public void StartTiming()
    {
        timer = new Timer(10, null);
        time = SplitMath.NANOSECONDS_PER_CENTISECONDS;
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time += SplitMath.NANOSECONDS_PER_CENTISECONDS;
                String timed = SplitMath.NanoToTimeString(time);
                total.setText(timed);
                time_list.get(current).setText(timed);
                }
            };
        timer.addActionListener(listener);
        timer.start();
        started = true;
    }

    // TODO: Rewrite this such that it returns a true or false dependent on whether there is a remaining time.

    /**
     * This advances the split and records to actual final time.
     * @param final_time
     * @return
     */
    public boolean AdvanceSplit(long final_time)
    {
        time_list.get(current).setText(SplitMath.NanoToTimeString(final_time));
        current++;

        if (current >= time_list.size())
        {
            timer.stop();
            finished = true;
            total.setText(SplitMath.NanoToTimeString(final_time));
        }

        return true;
    }

    // FIXME: Move most of this to MainFrame.
    /**
     * Registers the relevant hotkeys
     */
    public void RegisterHotkeys()
    {
        HotKeyListener listener = new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
                if (!started)
                {
                    splitTimer = new SplitTimer(split.headers.size());
                    StartTiming();
                }
                else
                {
                    if (!finished)
                    {
                        AdvanceSplit(splitTimer.TimeSegment());
                    }
                    else
                    {
                        split.AppendTrial(splitTimer.times);
                        provider.reset();
                    }
                }
            }
        };
        provider.register(KeyStroke.getKeyStroke("shift S"), listener);
    }

    public void UnregisterHotkeys()
    {
        provider.reset();
        provider.stop();
    }
}
