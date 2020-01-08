package jun.example.asyncFuture;

/**
 * @author playjun
 * @since 2020 01 08
 */
public interface FutureCallback<V> {
    void onSuccess(V result);

    void onFailure(Throwable failure);
}
