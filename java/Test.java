import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args)
    {

        final Provider provider = Provider.getCurrentProvider(false);
        final Long start = System.currentTimeMillis();

        HotKeyListener listener = new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {
                System.out.println("Time Since Start (in ms):");
                System.out.println(System.currentTimeMillis()-start);
            }
        };
        List<String> parts = Arrays.asList("One", "Two", "Three");
        provider.register(KeyStroke.getKeyStroke("control 0"), listener);
        //SplitFile s = new SplitFile("Rugrats", parts);
        SplitFile s2 = new SplitFile("Rugrats");
        //s2.AppendLine(Arrays.asList("1","2.5","77.5"));
        System.out.println(s2.Trials());
    }
}