package app;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import static java.lang.String.format;

/**
 * Created by Павел on 13.12.2015.
 */
public class Worker extends Thread{

    private static int workerCount = 1;
    private ArrayBlockingQueue<Task> queue;
    private int workerNumber;

    public Worker(ArrayBlockingQueue<Task> queue) {
        this.queue = queue;
        setWorkerNumber();
        System.out.println("Worker#" + workerNumber + " is created.");
    }

    private void setWorkerNumber() {
        workerNumber = workerCount;
        workerCount ++;
    }

    @Override
    public void run() {
        int timeout = 20;
        try {
            while(true) {
                Task task = queue.poll(timeout, TimeUnit.SECONDS);
                sleep((long) task.getWorkload() * 1000);
                System.out.println(format("Worker#%d...Finished task %s.",
                        workerNumber, task.toString()));
            }
        } catch (InterruptedException e) {
            System.out.println("Queue was empty more then " + timeout + " seconds.");
        }
    }

}
