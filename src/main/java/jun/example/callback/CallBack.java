package jun.example.callback;

/**
 * @author playjun
 * @since 2020 01 03
 */
@FunctionalInterface
public interface CallBack<T> {



    void invoke(T parameter);

}
