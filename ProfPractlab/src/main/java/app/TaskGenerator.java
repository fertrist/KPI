package app;

import app.Task.Priority;
import app.Task.Workload;
import org.apache.commons.math3.distribution.GammaDistribution;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Павел on 13.12.2015.
 */
public class TaskGenerator extends Thread {

    private static final int frequencyTimeUnit = 60;
    private ArrayBlockingQueue<Task> queue;
    public static GammaDistribution gammaDistribution = new GammaDistribution(1, 0.5);

    public TaskGenerator(ArrayBlockingQueue<Task> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (double x = 0.05; x < 1.6; x += 0.05) {
            if (x >= 1.5) { x = 0.05; }
            double interval = getRandomInterval(x);

            //generate tasks on a given interval of time
            for (double time = 0; time < 60; time += interval) {
                try {
                    Task task = new Task(getRandomPriority(), getRandomWorkload());
                    queue.add(task);
                    System.out.println(task.toString() + " arrived.");
                } catch (IllegalStateException e) {
                    System.out.println("Queue is full.");
                }
                try {
                    sleep((long) (interval * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get random time period for task generation based on Erlang distribution.
     * @param x value of variable in distribution
     * @return interval in which tasks are generated, evaluated based on cumulative probability of x
     */
    private double getRandomInterval(double x) {
        int density = (int) Math.round(gammaDistribution.cumulativeProbability(x) * 100);
        float interval = frequencyTimeUnit / density;
        System.out.println(String.format("Generating tasks every %.2f seconds", interval));
        return interval;
    }

    /**
     * Get random priority for task.
     * @return random priority.
     */
    private static Priority getRandomPriority() {
        return Priority.getPriorityByOrdinal(getRandom());
    }

    /**
     * Get random workload for task.
     * @return random workload.
     */
    private static Workload getRandomWorkload() {
        for (Workload workload : Workload.values()) {
            if (workload.ordinal() == getRandom()) {
                return workload;
            }
        }
        return Workload.NORMAL;
    }

    private static int getRandom() {
        Random random = new Random();
        return random.nextInt(Priority.values().length);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(getRandomPriority().toString());
            System.out.println(getRandomWorkload().toString());
        }
    }

}
