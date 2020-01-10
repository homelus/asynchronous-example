# asynchronous 라이브러리 개념 및 예제 정리

## Future 와 CallBack

자바에서 비동기를 구현하는 두 가지 방법을 간단히 살펴보고자 합니다.
실제로 효율적인 방법을 이용해 실용적으로 구현되겠지만 최대한 간단히 공부하려고 합니다.

이들의 공통점은 다른 쓰레드를 통해 함수를 실행한다는 점 입니다.
결과값을 처리하는 방법에 따라 `Future` 와 `Callback` 으로 나눌 수 있습니다.

### Future

외부 스레드에서 메서드를 실행 후 결과값을 공유 자원에 저장하고 가져오는 방법을 `Future` 라고 합니다.
자바에서는 메서드를 실행 후 공유 저장소로 사용되는 Future 인스턴스를 반환하도록 구현합니다.
  
반환 받은 Future 에서 **get()** 메서드를 사용하면 앞으로 도착할 결과값을 기다렸다가 받을 수 있습니다.
이때 사용중인 스레드는 **차단**됩니다.

Future 의 간단한 코드 구현 [Future-example-code]()

### Callback

Callback 은 **다른 스레드**에서 프로세스를 실행 후 파라미터로 전달받은 **콜백 클래스의 메서드를 실행**하는 방법입니다.
**메인 스레드는 차단되지 않고** 본 작업을 계속 수행합니다.

이 방법의 예제는 다음과 같습니다.

Callback 의 간단한 코드 구현 [Callback-example-code]()

### Future vs Callback

다른 스레드에서 작업한 결과를 처리하는 주체가 `사용중인 쓰레드`인지 `해당 쓰레드`인지가 가장 큰 차이점입니다.
만약 연산 결과를 주 실행 쓰레드에서 사용한다면 `Future`, 영향이 없는 경우 `Callback` 을 사용합니다.

## ListenableFuture

guava 는 이 둘을 혼합한 [ListenableFuture](https://github.com/google/guava/wiki/ListenableFutureExplained) 를 제공합니다.
(스프링에서는 4.0 부터 제공하고 있습니다.)

이 방법은 작업이 완료되었을 때 실행되는 `callback 을 나중에 등록하는 것` 이 핵심입니다.
외부의 쓰레드에서 프로세스 처리 후 바로 실행하는 callback 과 달리 future 방식을 이용해 **실행을 지연**시킬 수 있습니다.
이를 통해 callback 함수를 별도로 추가할 수 있고 더 나아가 여러 작업들을 *구성*하여 사용할 수 있습니다.

ListenableFuture 의 간단한 코드 구현 [ListenableFuture-example-code]()

```java
public interface FutureCallback<V> {
    void onSuccess(V result); // 성공 시

    void onFailure(Throwable failure); // 실패 시
}

public class ListenableFuture<V> {

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

    private void resolve() {
        if (callback != null && isCompleted) {
            if (failure == null) {
                callback.onSuccess(result);
            } else {
                callback.onFailure(failure);
            }
        }
    }
}

public class ListenableFutureService {
    public <V> ListenableFuture<V> submit(final Callable<V> callable) { // 함수를 받아 처리한다.
        final ListenableFuture<V> future = new ListenableFuture<>();
        new Thread(() -> {
            try {
                V result = callable.call();
                future.setResult(result);
            } catch (Exception e) {
                future.setFailure(e);
            }
        }).start();
        return future; // 미래의 결과값을 저장소를 반환
    }
}
```


 




