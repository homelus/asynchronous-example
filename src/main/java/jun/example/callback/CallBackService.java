package jun.example.callback;

import java.util.concurrent.TimeUnit;

/**
 * 다른 쓰레드에서 프로세스를 수행하고 결과를 콜백으로 실행시킨다.
 */
public class CallBackService {

    public void execute(CallBack callBack) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ignored) {}
            // 프로세스 수행
            String parameter = "name";
            callBack.invoke(parameter);
        }).start();
    }

}
