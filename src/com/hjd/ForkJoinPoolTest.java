package com.hjd;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

//RecursiveAction为ForkJoinTask的抽象子类，没有返回值的任务  
class PrintTask extends  RecursiveTask<Integer> {
    // 每个"小任务"最多只打印50个数  
    private static final int MAX = 50;

    private int start;
    private int end;
    private int args[];

    PrintTask(int start, int end,int[] args) {
        this.start = start;
        this.end = end;
        this.args = args;
    }

    @Override
    protected Integer compute() {
        int sum = 0 ;
        // 当end-start的值小于MAX时候，开始打印
        if ((end - start) < MAX) {
            for (int i = start; i < end; i++) {
                sum+=args[i];
            }
            return sum;
        } else {
            // 将大任务分解成两个小任务
            int middle = (start + end) / 2;
            PrintTask left = new PrintTask(start, middle,args);
            PrintTask right = new PrintTask(middle, end,args);
            // 并行执行两个小任务
            left.fork();
            right.fork();
            // 把两个小任务累加的结果合并起来
            return left.join() + right.join();
        }
    }
}

public class ForkJoinPoolTest {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool  
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int arr[] = new int[100];
        Random random = new Random();
        int total = 0;
        // 初始化100个数字元素
        for (int i = 0; i < arr.length; i++) {
            int temp = random.nextInt(100);
            // 对数组元素赋值,并将数组元素的值添加到total总和中
            total += (arr[i] = temp);
        }
        // 提交可分解的PrintTask任务  
        Integer as = forkJoinPool.submit(new PrintTask(0, arr.length,arr)).get();
        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);//阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
        System.out.println("计算出来的总和=" + as);
        // 关闭线程池  
        forkJoinPool.shutdown();
    }

}