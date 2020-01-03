# asynchronous 라이브러리 개념 및 예제 정리

## Future 와 CallBack

많은 라이브러리는 두 가지 방법을 이용해 비동기를 제공한다.
**Future 를 반환**하거나 **Callback 파라미터**를 넘기는 방법이다.

Future 는 `pull` 로 Callback 은 `push` 로 생각해 볼 수 있다.

인스턴스에서 공유된 데이터를 이용해 프로세스가 처리되어 데이터가 들어올 때 까지 pull 하는 기능에서 
인스턴스를 Future 로 볼 수 있다.

프로세스가 처리되어 데이터가 들어오는 과정을 `쓰레드를 만들어 실행`시키고 결과를 조회할 때 데이터가 들어올 때 까지
`사용중인 쓰레드를 blocking` 할 수 있다.

```java
public class Future {

    private Object value = null;
    private int state = 0;

    public void setValue(Object value) {
        this.value = value;
        this.state++;
    }

    public Object get() {
        while (state < 1) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        return value;
    }
}
``` 

메서드를 인자로 받아 프로세스가 처리끝나면 메서드에 push 하는 기능을 callback 이라고 볼 수 있다.

```java
@FunctionalInterface
public interface CallBack {

    void invoke(Object parameter);

}
```


