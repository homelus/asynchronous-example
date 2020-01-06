# asynchronous 에 대한 개념과 예제 정리

## Future 와 CallBack

많은 라이브러리는 Future 와 Callback 두 가지 방법을 이용해 비동기를 제공한다.

첫번째 방법으로 함수를 실행하면 미래의 데이터를 가진 **Future** 를 반환할 수 있다.
이 방법은 다른 스레드에 비동기 작업을 위임하고 본 스레드에서는 다른 일들을 처리한다.
다른 스레드에서 위임한 작업이 완료되면 **Future 의 공유 데이터**에 처리가 완료된 값을 보관한다.
본 스레드에서 비동기 작업의 완료값이 필요할 때 get() 메서드를 호출해 공유 데이터에서 값을 가져오는데
이때 *블락킹*이 된다.

요약하면 비동기 작업을 `다른 쓰레드로 실행`시키고 필요할 때 공유 데이터를 조회하는데 이때 
`본 쓰레드가 blocking` 된다.

이 작업을 다음과 같이 간단한 코드로 작성할 수 있다. 추후에 개선 코드도 설명할 예정이다.

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

두번째는 Callback 파라미터를 이용한 비동기 처리이다.
비동기 처리 후 실행될 함수를 넘겨주는 방법으로 자바에서는 보통 익명 클래스를 사용한다.
메인 쓰레드에서 워커 쓰레드에게 작업을 비동기로 위임할 때 콜백 익명 클래스를 같이 전달한다.
워커 쓰레드에서 작업이 끝난 후 넘겨받은 익명 클래스의 메서드를 실행시킨다는 의미에서 push 로 비유할 수 있다.

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


