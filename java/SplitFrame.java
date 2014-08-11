import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SplitFrame extends JFrame
{
    private JPanel panel;
    private JPanel split_panel;
    private JLabel title;
    private List<JLabel> label_list;
    private List<JLabel> time_list;
    private int current;
    private long time;
    private JLabel total;
    private Timer timer;
    public boolean started = false;
    public boolean finished = false;

    public SplitFrame(SplitFile split)
    {
        final int rows = split.parts.size();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        title = new JLabel(split.name, JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);

        split_panel = new JPanel();
        split_panel.setLayout(new GridLayout(rows, 2));

        label_list = new ArrayList<JLabel>();
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

        total = new JLabel("", JLabel.CENTER);
        panel.add(total, BorderLayout.SOUTH);

        add(panel);
    }

    public void StartTiming()
    {
        timer = new Timer(10, null);
        time = 10000000;
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time += 10000000;
                String timed = NanoToString(time);
                total.setText(timed);
                time_list.get(current).setText(timed);
                }
            };
        timer.addActionListener(listener);
        timer.start();
        started = true;
    }

    public void AdvanceSplit(long final_time)
    {
        time_list.get(current).setText(NanoToString(final_time));
        current++;

        if (current >= time_list.size())
        {
            timer.stop();
            finished = true;
            total.setText(NanoToString(final_time));
        }
    }

    private static String NanoToString(long nanoseconds)
    {
        final long CENTISECOND = 10000000;
        final long SECOND = 100 * CENTISECOND;
        final long MINUTE = 60 * SECOND;
        final long HOUR = 60 * MINUTE;
        final long DAY = 24 * HOUR;
        long ns = nanoseconds;

        String in_string = "";

        long milliseconds, seconds, minutes, hours, days;

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
}
