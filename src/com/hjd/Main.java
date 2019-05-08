package com.hjd;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjd on 2017/12/22.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 8. 创建 ForkJoinPool 对象，名为 pool。
        ForkJoinPool pool = new ForkJoinPool();

        // 9. 创建 10，000个元素的整数 array ，名为 array。
        int array[] = new int[10000];

        // 10. 创建新的 Task 对象来处理整个array。
        Task task1 = new Task(array, 0, array.length);

        // 11. 使用 execute() 方法 把任务发送到pool里执行。
        pool.execute(task1);

        // 12. 当任务还未结束执行，调用 showLog() 方法来把 ForkJoinPool 类的状态信息写入，然后让线程休眠一秒。
        while (!task1.isDone()) {
            showLog(pool);
            TimeUnit.SECONDS.sleep(1);
        }

        // 13. 使用 shutdown() 方法关闭pool。
        pool.shutdown();

        // 14. 使用 awaitTermination() 方法 等待pool的终结。
        pool.awaitTermination(1, TimeUnit.DAYS);

        // 15. 调用 showLog() 方法写关于 ForkJoinPool 类状态的信息并写一条信息到操控台表明结束程序。
        showLog(pool);
        System.out.printf("Main: End of the program.\n");
    }

    // 16. 实现 showLog() 方法。它接收 ForkJoinPool 对象作为参数和写关于线程和任务的执行的状态的信息。
    private static void showLog(ForkJoinPool pool) {
        System.out.printf("**********************\n");
        System.out.printf("Main: Fork/Join Pool log\n");
        System.out.printf("Main: Fork/Join Pool: Parallelism:%d\n",
                pool.getParallelism());
        System.out.printf("Main: Fork/Join Pool: Pool Size:%d\n",
                pool.getPoolSize());
        System.out.printf("Main: Fork/Join Pool: Active Thread Count:%d\n",
                pool.getActiveThreadCount());
        System.out.printf("Main: Fork/Join Pool: Running Thread Count:%d\n",
                pool.getRunningThreadCount());
        System.out.printf("Main: Fork/Join Pool: Queued Submission:%d\n",
                pool.getQueuedSubmissionCount());
        System.out.printf("Main: Fork/Join Pool: Queued Tasks:%d\n",
                pool.getQueuedTaskCount());
        System.out.printf("Main: Fork/Join Pool: Queued Submissions:%s\n",
                pool.hasQueuedSubmissions());
        System.out.printf("Main: Fork/Join Pool: Steal Count:%d\n",
                pool.getStealCount());
        System.out.printf("Main: Fork/Join Pool: Terminated :%s\n",
                pool.isTerminated());
        System.out.printf("**********************\n");
    }
}
