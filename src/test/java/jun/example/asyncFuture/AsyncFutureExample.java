package jun.example.asyncFuture;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author playjun
 * @since 2020 01 08
 */
public class AsyncFutureExample {

    @Test
    public void asyncTest() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(5);
        FutureExecutor executor = new FutureExecutor(service);
        ListenableFuture<String> future = executor.submit(new DelayedJob(1000));
        future.addCallback(new Printer());

        Thread.sleep(2000);
        service.shutdown();
    }

}
