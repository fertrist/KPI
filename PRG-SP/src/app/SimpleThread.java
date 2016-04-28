package app;

import static java.lang.String.format;

public class SimpleThread extends Thread {

    public static int N = Launcher.N;
    public static int H = Launcher.H;

    protected int threadNo;
    Data data;
    int start, end;
    public SimpleThread(Data data, int threadNo) {
        this.data = data;
        this.start = H * (threadNo - 1);
        this.end = H * threadNo;
        this.threadNo = threadNo;
    }

    public void run() {
        System.out.println((format("T%d: started", threadNo)));

        //wait for tasks 1 and 4 to finish input
        data.waitInput();
        System.out.println(format("T%d: input finished", threadNo));

        getMax();
        computeMatrix();

        System.out.println(format("T%d: finished", threadNo));
    }

    void computeMatrix() {
        int alpha, max;
        int[] C;
        int[][] MO;

        //copy data, critical section
        alpha = data.copyAlpha();
        C = data.copyC();
        MO = data.copyMO();
        max = data.copyMax();
        System.out.println(format("T%d: Data copied.", threadNo));

        // computations
        for (int i = start; i < end; i++)
        {
            int CH = 0;
            for (int x = 0; x < N; x++)
            {
                int MOK = 0;
                for (int y = 0; y < N; y++)
                {
                    MOK = MOK + MO[x][y] * data.MK[y][i];
                }
                CH = CH + alpha * C[x] * MOK;
            }
            data.A[i] = max * data.B[i] + CH;
        }

        // сигнал про окончание вычислений
        System.out.println(format("T%d: matrix computed.", threadNo));
        data.computationsSignal();
    }

    int getMax() {
        int max = -1000;
        for (int i = start; i < end; i++)
        {
            if (data.Z[i] > max)
            {
                max = data.Z[i];
            }
        }
        // write max, critical section
        data.setMax(max);
        data.maxSignal();
        //wait for max
        data.waitMax();
        System.out.println(format("T%d: max found.", threadNo));
        return max;
    }

}
