package jun.example.callback;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Callback 테스트
 */
public class CallBackExample {

    @Test
    public void callbackTest() throws InterruptedException {
        CallBackService callBackService = new CallBackService();
        System.out.println("before service execute");
        callBackService.execute(arg -> System.out.println("result: " + arg));
        System.out.println("after service execute");

        TimeUnit.SECONDS.sleep(5);
    }

}
