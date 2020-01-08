package jun.example.asyncFuture;

/**
 * @author playjun
 * @since 2020 01 08
 */
public class ListenableFuture<V> {

    private FutureCallback<V> callback;
    private V result;
    private Throwable failure;
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
