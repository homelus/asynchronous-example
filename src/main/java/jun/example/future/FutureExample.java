package jun.example.future;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 쓰레드를 이용해 실행하고 Future 를 이용해 데이터를 보관하며 get() 메서드 호출 시 데이터가 입력될 때 까지 blocking 된다.
 */
public class FutureExample {

    public static void main(String[] args) {
        Future future = new FutureExample().execute(() -> "result completed!");
        Object result = future.get(); // 블락킹
        System.out.println("[" + Thread.currentThread().getName() + "] result :" + result);
    }

    public Future execute(final Supplier<Object> supplier) {
        final Future future = new Future(); // 공유 데이터를 가진 Future 를 반환한다.
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {}
            // 프로세스 수행
            future.setValue(supplier.get()); // 공유 데이터를 저장한다.
            System.out.println("[" + Thread.currentThread().getName() + "] executed end!");
        }).start(); // 외부 쓰레드에서 실행한다.
        return future;
    }

}

class Future {

    private Object value = null;
    private int state = 0;

    public void setValue(Object value) {
        this.value = value;
        this.state++;
    }

    public Object get() {
        while (state < 1) { // 데이터가 존재할 때 까지 쓰레드를 블락한다.
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        return value;
    }

}