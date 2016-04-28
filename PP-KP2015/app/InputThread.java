package app;

import static java.lang.String.format;

public class InputThread extends SimpleThread{

    public InputThread(Data data, int threadNo) {
        super(data, threadNo);
    }

    public void run() {
        System.out.println((format("T%d: started", threadNo)));
        getInput();
        data.inputSignal();

        //wait for tasks 1 and 4 to finish input
        data.waitInput();
        System.out.println(format("T%d: input finished", threadNo));

        getMax();
        computeMatrix();

        System.out.println(format("T%d: finished", threadNo));
    }

    protected void getInput() {
        switch (threadNo) {
            case 1:
                //input alpha, C
                data.alpha = 1;
                for (int i = 0; i < N; i++) {
                    data.C[i] = 1;
                }
                //input finished
                break;
            case 4:
                //input B, Z
                for (int i = 0; i < N; i++) {
                    data.B[i] = 1;
                    data.Z[i] = 1;
                }
                break;
            case 6:
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        data.MO[i][j] = 1;
                    }
                }
                break;
            case 8:
                //MK
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        data.MK[i][j] = 1;
                    }
                }
                break;
        }
    }
}
