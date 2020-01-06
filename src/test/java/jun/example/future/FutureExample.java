package jun.example.future;

import jun.example.future.Future;
import jun.example.future.FutureService;
import org.junit.Test;

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
        Object o1 = data1.get();
        Object o2 = data2.get();
        Object o3 = data3.get();
        System.out.println("after get");
        System.out.println("result :" + o1.toString());
        System.out.println("result :" + o2.toString());
        System.out.println("result :" + o3.toString());
    }

}
