import java.util.ArrayList;
import java.util.List;


public class SplitTimer
{
    private long startTime;
    public List<Long> times;
    private int parts;
    private int i;

    public SplitTimer(int parts)
    {
        startTime = System.nanoTime();
        this.parts = parts;
        times = new ArrayList<Long>();
        i = 0;
    }

    public long TimeSegment()
    {
        if (i < parts)
        {
            times.add(i,(System.nanoTime() - startTime));
            i++;
            return times.get(i-1);
        }
        else
        {
            return -1;
        }
    }

    public boolean isCounting()
    {
        return i < parts;
    }
}
