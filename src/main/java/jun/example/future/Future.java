package jun.example.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Future<T> {

    private T value = null;
    private volatile int state = 0;

    private static final int NEW = 0;
    private static final int COMPLETING = 1;
    private static final int NORMAL = 2;

    public Future() {
        state = NEW;
    }

    public void setValue(T value) {
        this.value = value;
        this.state = NORMAL;
    }

    public T get() {
        while (state == NORMAL) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        return value;
    }

    public T get(long timeout, TimeUnit unit) throws TimeoutException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        for (; ; ) {
            int s = state;
            if (s == NORMAL) {
                return value;
            } else if (0 >= deadline - System.nanoTime()) {
                throw new TimeoutException();
            }
        }
    }

}
