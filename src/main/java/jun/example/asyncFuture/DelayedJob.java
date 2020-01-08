package jun.example.asyncFuture;

import java.util.concurrent.Callable;

/**
 * @author playjun
 * @since 2020 01 08
 */
public class DelayedJob implements Callable<String> {

    private int delay;

    public DelayedJob(int delay) {
        this.delay = delay;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(delay);
        return "Jun";
    }
}
