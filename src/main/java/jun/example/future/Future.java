package jun.example.future;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Future<T> {

    private T value = null;
    private int state = 0;

    public void setValue(T value) {
        this.value = value;
        this.state++;
    }

    public Object get() {
        while (state < 1) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        return value;
    }

}
