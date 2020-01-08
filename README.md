# asynchronous 라이브러리 개념 및 예제 정리

## Future 와 CallBack

비동기를 제공하기 라이브러리에서는 두 가지 방법을 사용합니다.
이 두 방법은 새로운 스레드를 만들어 함수를 실행시킨 후 결과값을 어떻게 처리하냐에 따라 Future 와 Callback 으로 나뉩니다.

Future 방식은 외부의 스레드에서 메서드를 실행 후 반환되는 결과값을 공유 자원에 저장하는 프로세스입니다.
미래의 결과값을 공유 저장소에 보관할 것이라는 의미로 Future 클래스를 사용합니다.  
메서드를 실행 후 공유 저장소로 사용되는 Future 인스턴스를 반환받습니다.
  
이 클래스의 get() 메서드를 사용한다면 앞으로 도착할 결과값을 기다렸다가 받을 수 있습니다.
이때 사용중인 스레드는 **차단**됩니다.

이러한 방법을 다음과 같이 간단한 코드로 작성할 수 있습니다.

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

Callback 은 외부의 스레드에서 메서드를 실행 후 후처리 메서드를 파라미터로 받아 완료 받은 결과값과 함께 미래에 실행하는 프로세스입니다.

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


