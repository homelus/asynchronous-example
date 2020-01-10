package jun.example.asyncFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * callback 을 등록할 때 까지 실행을 지연 시킨다.
 * 공유 저장소를 사용한다는 관점에서 Future 의 패턴을 사용
 * 특정 이벤트에 함수를 실행하는 관점에서 Callback 의 개념을 사용
 */
public class ListenableFutureExample {

    public static void main(String[] args) throws InterruptedException {
        ListenableFuture<String> future = new ListenableFutureExample().submit(() -> {
            TimeUnit.SECONDS.sleep(1); return "completed";
        });

        future.addCallback(new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) { System.out.println("success: " + result); }
            @Override
            public void onFailure(Throwable failure) { failure.printStackTrace();  }
        });

        TimeUnit.SECONDS.sleep(2);
    }

    public <V> ListenableFuture<V> submit(final Callable<V> callable) { // 함수를 받아 처리한다.
        final ListenableFuture<V> future = new ListenableFuture<>();
        new Thread(() -> {
            try { // Future 의 개념을 사용
                V result = callable.call();
                future.setResult(result);
            } catch (Exception e) {
                future.setFailure(e);
            }
        }).start();
        return future; // 미래의 결과값을 저장소를 반환
    }
}

interface FutureCallback<V> {
    void onSuccess(V result); // 성공 시

    void onFailure(Throwable failure); // 실패 시
}

class ListenableFuture<V> {

    private FutureCallback<V> callback; // 콜백 저장소
    private V result; // 결과값 공유 저장소
    private Throwable failure; // 실패값 공유 저장소
    private boolean isCompleted;

    public void addCallback(FutureCallback<V> callback) {
        this.callback = callback;
        resolve();
    }

    public void setResult(V result) {
        this.result = result;
        isCompleted = true;
        resolve();
    }

    public void setFailure(Throwable failure) {
        this.failure = failure;
        isCompleted = true;
        resolve();
    }

    private void resolve() { // Callback 의 개념을 사용
        if (callback != null && isCompleted) {
            if (failure == null) {
                callback.onSuccess(result);
            } else {
                callback.onFailure(failure);
            }
        }
    }
}