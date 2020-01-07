package jun.example.future;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Future 테스트
 */
public class FutureExample {

    @Test
    public void future() {
        FutureService futureService = new FutureService();
        System.out.println("before execute");
        Future<String> data1 = futureService.execute(() -> "result completed!");
        Future<String> data2 = futureService.execute(() -> "result completed!");
        Future<String> data3 = futureService.execute(() -> "result completed!");
        System.out.println("before get");
        String o1 = data1.get();
        String o2 = data2.get();
        String o3 = data3.get();
        System.out.println("after get");
        System.out.println("result :" + o1);
        System.out.println("result :" + o2);
        System.out.println("result :" + o3);
    }

    @Test
    public void futureWithTimeOut() throws TimeoutException {
        FutureService futureService = new FutureService();
        Future<String> execute = futureService.execute(() -> "result Completed");
        String o = execute.get(2, TimeUnit.SECONDS);
        System.out.println(o);
    }

}
