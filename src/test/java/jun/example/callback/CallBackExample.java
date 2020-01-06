package jun.example.callback;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Callback 테스트
 */
public class CallBackExample {

    @Test
    public void callbackTest() throws InterruptedException {
        CallBackService callBackService = new CallBackService();
        System.out.println("before service execute");
        IntStream.range(0, 10).forEach(i -> {
            callBackService.execute(() -> i + " CallBack Completed", arg -> System.out.println("result: " + arg));
        });

        System.out.println("after service execute");

        TimeUnit.SECONDS.sleep(5);
    }

}
