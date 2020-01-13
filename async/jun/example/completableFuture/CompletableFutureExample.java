package jun.example.completableFuture;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * @author playjun
 * @since 2020 01 13
 */
public class CompletableFutureExample {

    public static void main(String[] args) {
        String future = supplyAsync(() -> "jun")
                .thenApply(name -> name + " 13")
                .thenApplyAsync(nameAge -> nameAge + " seoul")
                .thenApply(nameAgeAddr -> nameAgeAddr + " 179")
                .join();

        System.out.println(future);
    }

}
