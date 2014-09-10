package net.hearthgate.osplit.gui;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import net.hearthgate.osplit.libs.SplitConfig;
import net.hearthgate.osplit.libs.SplitFile;
import net.hearthgate.osplit.libs.SplitMath;
import net.hearthgate.osplit.libs.SplitTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SplitPanel extends JPanel
{

  private List<JLabel> timeList;
  private int current;
  private long time;
  private JLabel total;
  private Timer timer;
  private SplitTimer splitTimer;
  private SplitFile splitFile;

  public boolean hasStarted = false;
  public boolean hasFinished = false;

  Provider provider;

  /**
   * The panel that you use while splitting
   * @param splitFile
   */
  public SplitPanel(SplitFile splitFile, Provider provider) {
    this.provider = provider;
    this.splitFile = splitFile;
    final int rows = splitFile.headers.size();
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel title = new JLabel(splitFile.fileName, JLabel.CENTER);
    panel.add(title, BorderLayout.NORTH);

    JPanel split_panel = new JPanel();
    split_panel.setLayout(new GridLayout(rows, 2));

    List<JLabel> label_list = new ArrayList<JLabel>();
    timeList = new ArrayList<JLabel>();
    for (String segment : splitFile.headers) {
      JLabel label = new JLabel(segment, JLabel.CENTER);
      label_list.add(label);
      split_panel.add(label);
      JLabel time = new JLabel("...", JLabel.LEFT);
      timeList.add(time);
      split_panel.add(time);
    }

    current = 0;

    panel.add(split_panel, BorderLayout.CENTER);

    total = new JLabel("...", JLabel.CENTER);
    panel.add(total, BorderLayout.SOUTH);

    setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);

    registerHotkeys();
  }

  /**
   * Starts both the real timer (SplitTimer) and the graphical one
   */
  public void startTiming() {
    timer = new Timer(10, null);
    time = SplitMath.NANOSECONDS_PER_CENTISECONDS;
    ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        time += SplitMath.NANOSECONDS_PER_CENTISECONDS;
        String timed = SplitMath.convertNanoToTimeString(time);
        total.setText(timed);
        timeList.get(current).setText(timed);
      }
    };
    timer.addActionListener(listener);
    timer.start();
    hasStarted = true;
  }

  // TODO: Rewrite this such that it returns a true or false dependent on whether there is a remaining time.

  /**
   * This advances the splitFile and records to actual final time.
   * @param finalTime
   * @return
   */
  public boolean advanceSplit(long finalTime)
  {
    timeList.get(current).setText(SplitMath.convertNanoToTimeString(finalTime));
    current++;

    if (current >= timeList.size())
    {
      timer.stop();
      hasFinished = true;
      total.setText(SplitMath.convertNanoToTimeString(finalTime));
    }

    return true;
  }

  /**
   * Registers the relevant hotkeys
   */
  public void registerHotkeys()
  {
    HotKeyListener listener = new HotKeyListener() {
      public void onHotKey(HotKey hotKey) {
        if (!hasStarted) {
          splitTimer = new SplitTimer(splitFile.headers.size());
          startTiming();
        } else {
          if (!hasFinished) {
            advanceSplit(splitTimer.timeSegment());
          } else {
            splitFile.appendTrial(splitTimer.times);
          }
        }
      }
    };
    provider.register(KeyStroke.getKeyStroke(SplitConfig.getProperty(SplitConfig.TIMER_START)), listener);
  }
}
