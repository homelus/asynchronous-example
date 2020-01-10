package jun.example.callback;

import java.util.concurrent.TimeUnit;

/**
 * 다른 쓰레드에서 프로세스를 수행하고 결과를 콜백으로 실행시킨다.
 */
public class CallBackExample {

    public static void main(String[] args) throws InterruptedException {
        new CallBackExample()
                .execute("parameter", arg -> System.out.println("[" + Thread.currentThread().getName() + "] callback result: " + arg));
        TimeUnit.SECONDS.sleep(2);
    }

    public void execute(String parameter, CallBack callBack) { // 비동기 처리 후 실행할 콜백함수를 받는다.
        new Thread(() -> { // 프로세스 수행 소요 시간: 1초
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {}
            callBack.invoke(parameter + " Completed");
        }).start(); // 다른 워커 쓰레드에서 비동기로 작업을 실행한다.
    }
}

@FunctionalInterface
interface CallBack {
    void invoke(Object parameter);
}
