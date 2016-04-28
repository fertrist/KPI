package app;

/**
 * -------------------------------
 -- ������������ ������ �5
 -- Java. ��������..
 -- ������� ����� �����������
 -- ��-�21, ����
 -- A = max(Z)*B + alpha*C*(MO*MK)
 -------------------------------
 */
public class Launcher {

    public static final int N = 1500;
    public static final int P = 10;
    public static final int H = N/P;
    public static final int INPUTS = 4;

    public static void main(String[] args) throws InterruptedException {
        Data data = new Data();
        Thread [] tasks = new Thread[]{
                new InputThread(data, 1),
                new SimpleThread(data, 2),
                new SimpleThread(data, 3),
                new OutputThread(data, 4),
                new SimpleThread(data, 5),
                new InputThread(data, 6),
                new SimpleThread(data, 7),
                new InputThread(data, 8),
                new SimpleThread(data, 9),
                new SimpleThread(data, 10)
        };
        long start = System.currentTimeMillis();
        for (Thread task : tasks) {
            task.start();
        }
        for (Thread task : tasks) {
            task.join();
        }
        System.out.println("Launcher finished. " + (System.currentTimeMillis() - start) + " millis.");
    }

}
