# asynchronous 라이브러리 개념 및 예제 정리

## Future 와 CallBack

자바에서 비동기를 구현하는 두 가지 방법을 살펴보고자 합니다.
이들의 공통점은 다른 쓰레드를 통해 함수를 실행한다는 점 입니다.
결과값을 처리하는 방법에 따라 `Future 와 Callback` 으로 나눌 수 있습니다.

### Future

외부 스레드에서 메서드를 실행 후 결과값을 공유 자원에 저장하는 방법을 `Future` 라고 합니다.
일반적으로 메서드 실행 후 공유 저장소로 사용되는 Future 인스턴스를 반환하도록 구현합니다.
  
반환 받은 Future 에서 get() 메서드를 사용하면 앞으로 도착할 결과값을 기다렸다가 받을 수 있습니다.
이때 사용중인 스레드는 **차단**됩니다.

이를 다음과 같이 간단한 코드로 작성할 수 있습니다.

```java
public class FutureService {

    public Future execute() {
        final Future future = new Future(); // 공유 데이터를 가진 Future 를 반환한다.
        new Thread(() -> { 
            System.out.println("executed start!");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {}
            // 프로세스 수행
            future.setValue("result!"); // 공유 데이터를 저장한다.
            System.out.println("executed end!");
        }).start(); // 외부 쓰레드에서 실행한다.
        return future;
    }

}

public class Future {

    private Object value = null;
    private int state = 0;

    public void setValue(Object value) {
        this.value = value;
        this.state++;
    }

    public Object get() {
        while (state < 1) { // 데이터가 존재할 때 까지 쓰레드를 블락한다.
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        return value;
    }
}
``` 

### Callback

Callback 은 이와 다르게 스레드에서 메서드를 실행 후 파라미터로 전달받은 콜백 클래스의 메서드를 실행하는 방법입니다.
이 방법의 예제는 다음과 같습니다.

```java
public class CallBackService {
    public void execute(CallBack callBack) { // 비동기 처리 후 실행할 콜백함수를 받는다.
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ignored) {}
            // 프로세스 수행
            String parameter = "name";
            callBack.invoke(parameter);
        }).start(); // 다른 워커 쓰레드에서 비동기로 작업을 실행한다.
    }
}

@FunctionalInterface // 익명클래스로 전달하는데 FunctionalInterface(메서드가 한개) 로 지정하면 Lambda 를 사용할 수 있다.
public interface CallBack {

    void invoke(Object parameter);

}
```

### Future vs Callback

이 둘은 다른 스레드에서 작업한 결과를 사용중인 스레드에서 처리할 것이냐, 해당 스레드에서 처리할 것이냐 라는 차이점이 있습니다.
만약 결과가 현재 실행되고 있는 쓰레드에 영향을 준다면 Future 그렇지 않다면 Callback 을 사용하면 됩니다.

## ListenableFuture

guava 에는 이 둘을 혼합해 구현한 [ListenableFuture](https://github.com/google/guava/wiki/ListenableFutureExplained) 가 있습니다.
스프링에서도 4.0 부터 제공되고 있습니다.

이 클래스의 핵심은 작업이 완료되었을 때 실행되는 `callback 을 나중에 등록하는 것`입니다.
이를 통해 callback 함수를 별도로 추가할 수 있고 예외가 발생했을 때 장애상황을 극복할 수 있습니다.

다음의 예제를 보고 추가로 설명하겠습니다.

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


 




