package jun.example.callback;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 다른 쓰레드에서 프로세스를 수행하고 결과를 콜백으로 실행시킨다.
 */
public class CallBackService {


    private static final Executor executor = Executors.newFixedThreadPool(4);

    public <T> void execute(Supplier<T> supplier, CallBack<T> callBack) {
        executor.execute(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ignored) {}
            // 프로세스 수행
            callBack.invoke(supplier.get());
            System.out.println(Thread.currentThread().getName() + ": CallBack Completed");
        });
    }

}
