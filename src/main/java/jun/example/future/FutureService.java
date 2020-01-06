package jun.example.future;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 쓰레드를 이용해 실행하고 Future 를 이용해 데이터를 보관하며 get() 메서드 호출 시 데이터가 입력될 때 까지 blocking 된다.
 */
public class FutureService {

    private static final Executor executor = Executors.newFixedThreadPool(4);

    public <T> Future<T> execute(Supplier<T> supplier) {
        final Future<T> future = new Future();
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {}
            // 프로세스 수행
            future.setValue(supplier.get());
            System.out.println(Thread.currentThread().getName() + ": future executed");
        });
        return future;
    }

}
