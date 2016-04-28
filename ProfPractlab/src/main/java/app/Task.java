package app;

import static java.lang.String.format;

/**
 * Created by Павел on 13.12.2015.
 */
public class Task implements Comparable<Task>{

    private Priority priority;
    private static int id_count = 0;
    private int id;

    //how long does task wait to be worked
    private long waitTime = 0;
    //how much time task needs to be complete
    private Workload workLoad;
    private static final int WAIT_TIMEOUT = 10;

    public Task(Priority priority, Workload workLoad) {
        this.priority = priority;
        this.workLoad = workLoad;
        setId();
    }

    private synchronized void setId() {
        this.id = id_count;
        id_count++;
    }

    public int compareTo(Task task) {
        int diff = getPriority().compareTo(task.getPriority());
        if (diff == 0) {
            diff = (int) (getWaitTime() - task.getWaitTime());
        }
        return diff;
    }

    public int getId() {
        return id;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public double getWorkload() {
        return workLoad.getWorkload();
    }

    public enum Priority{
        MINOR, NORMAL, MAJOR;

        public static Priority getPriorityByOrdinal(int ordinal) {
            ordinal = (ordinal < 0) ? 0 :
                    (ordinal >= values().length) ? values().length - 1 : ordinal;
            for (Priority priority : values()) {
                if (priority.ordinal() == ordinal) {
                    return priority;
                }
            }
            return NORMAL;
        }
    }

    public enum Workload {
        FAST(5f), NORMAL(7.5f), SLOW(10f);

        //how much time task needs to be complete
        private float seconds;

        Workload(float seconds) {
            this.seconds = seconds;
        }

        public double getWorkload() {
            return seconds;
        }
    }

    /**
     * Returns priority and notes where invocation was performed (when worker tried to get a task).
     * If priority is too small, worker doesn't work on current task causing it to wait.
     * If task waits long enough (WAIT_TIMEOUT), it's priority is increased.
     * @return priority
     */
    public Priority getPriority() {
        long diff = System.currentTimeMillis() - waitTime;
        if (diff/1000 >=  WAIT_TIMEOUT) {
            this.waitTime += diff;
            priority = Priority.getPriorityByOrdinal(priority.ordinal() + 1);
        }
        return priority;
    }

    @Override
    public String toString() {
        return  format("%10s - P:%s W:%.1f",
                "Task_" + getId(), priority.toString(), getWorkload());
    }

}
