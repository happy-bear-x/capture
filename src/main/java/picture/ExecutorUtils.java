package picture;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtils {

    public static ExecutorService getExecService() {
        return Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    }
}
