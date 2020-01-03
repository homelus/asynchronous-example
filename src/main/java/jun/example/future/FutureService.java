package jun.example.future;

import java.util.concurrent.TimeUnit;

/**
 * 쓰레드를 이용해 실행하고 Future 를 이용해 데이터를 보관하며 get() 메서드 호출 시 데이터가 입력될 때 까지 blocking 된다.
 */
public class FutureService {

    public Future execute() {
        final Future future = new Future();
        new Thread(() -> {
            System.out.println("executed start!");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {}
            // 프로세스 수행
            future.setValue("wow!");
            System.out.println("executed end!");
        }).start();
        return future;
    }

}
