package app;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Павел on 12.12.2015.
 */
public class Main {

    public static final int QUEUE_CAPACITY = 100;

    public static void main(String[] args) {
        ArrayBlockingQueue<Task> queue = new ArrayBlockingQueue<Task>(QUEUE_CAPACITY);

        Worker worker = new Worker(queue);
        worker.start();

        TaskGenerator taskGenerator = new TaskGenerator(queue);
        taskGenerator.start();
    }
}

