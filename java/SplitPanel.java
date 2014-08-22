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
    private static final long CENTISECOND = 10000000;

    private List<JLabel> time_list;
    private int current;
    private long time;
    private JLabel total;
    private Timer timer;
    private SplitTimer splitTimer;
    private SplitFile split;
    public boolean started = false;
    public boolean finished = false;

    public SplitPanel(SplitFile split)
    {
        this.split = split;
        final int rows = split.parts.size();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel(split.name, JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);

        JPanel split_panel = new JPanel();
        split_panel.setLayout(new GridLayout(rows, 2));

        List<JLabel> label_list = new ArrayList<JLabel>();
        time_list = new ArrayList<JLabel>();
        for (String segment : split.parts)
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

    public void StartTiming()
    {
        timer = new Timer(10, null);
        time = CENTISECOND;
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time += CENTISECOND;
                String timed = NanoToString(time);
                total.setText(timed);
                time_list.get(current).setText(timed);
                }
            };
        timer.addActionListener(listener);
        timer.start();
        started = true;
    }

    // FIXME: Rewrite this such that it returns a true or false dependent on whether there is a remaining time.
    public boolean AdvanceSplit(long final_time)
    {
        time_list.get(current).setText(NanoToString(final_time));
        current++;

        if (current >= time_list.size())
        {
            timer.stop();
            finished = true;
            total.setText(NanoToString(final_time));
        }

        return true;
    }

    private static String NanoToString(long nanoseconds)
    {
        // Constants for units of time. Self explanatory.
        final long SECOND = 100 * CENTISECOND;
        final long MINUTE = 60 * SECOND;
        final long HOUR = 60 * MINUTE;
        final long DAY = 24 * HOUR;

        // We subtract from this one
        // We need to continue to know the total to avoid, ie, omitting the seconds slot when there's exactly 4 minutes.
        long ns = nanoseconds;

        String in_string = "";

        long milliseconds, seconds, minutes, hours, days;

        // FIXME: Maybe this can be done in reverse, avoiding the two counts of nanoseconds.
        if (nanoseconds >= DAY)
        {
            days = ns / DAY;
            in_string += days + ":";
            ns %= DAY;
        }
        if (nanoseconds >= HOUR)
        {
            hours = ns / HOUR;
            in_string += AddLeadingZeros(hours, 2) + ":";
            ns %= HOUR;
        }
        if (nanoseconds >= MINUTE)
        {
            minutes = ns / MINUTE;
            in_string += AddLeadingZeros(minutes, 2) + ":";
            ns %= MINUTE;
        }
        if (nanoseconds >= SECOND)
        {
            seconds = ns / SECOND;
            in_string += AddLeadingZeros(seconds, 2) + ".";
            ns %= SECOND;
        }
        if (nanoseconds >= CENTISECOND)
        {
            milliseconds = ns / CENTISECOND;
            in_string += AddLeadingZeros(milliseconds, 2);
        }

        return in_string;
    }

    private static String AddLeadingZeros(long number, int digits)
    {
        String num = String.valueOf(number);
        int zeros = digits - num.length();

        for (int i=0; i < zeros; i++)
        {
            num = 0 + num;
        }

        return num;
    }

    public void RegisterHotkeys()
    {
        // FIXME: Am I supposed to release these when this panel closes? Probably.
        final Provider provider = Provider.getCurrentProvider(false);

        HotKeyListener listener = new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
                if (!started)
                {
                    splitTimer = new SplitTimer(split.parts.size());
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
                        split.AppendLine(splitTimer.times);
                        provider.reset();
                        provider.stop();
                        System.exit(0);
                    }
                }
            }
        };
        provider.register(KeyStroke.getKeyStroke("control 0"), listener);
    }
}
