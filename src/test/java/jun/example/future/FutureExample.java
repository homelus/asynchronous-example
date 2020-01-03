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
        Future data = futureService.execute();
        System.out.println("before get");
        Object o = data.get();
        System.out.println("after get");
        System.out.println("result :" + o.toString());
    }

}
