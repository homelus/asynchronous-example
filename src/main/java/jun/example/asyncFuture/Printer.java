package jun.example.asyncFuture;

/**
 * @author playjun
 * @since 2020 01 08
 */
public class Printer implements FutureCallback<String> {
    @Override
    public void onSuccess(String result) {
        System.out.println("Result: " + result);
    }

    @Override
    public void onFailure(Throwable failure) {
        failure.printStackTrace();
    }
}
