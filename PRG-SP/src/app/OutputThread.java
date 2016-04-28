package app;

import java.util.Arrays;

import static java.lang.String.format;

public class OutputThread extends InputThread {
    public OutputThread(Data data, int threadNo) {
        super(data, threadNo);
    }

    @Override
    public void run() {
        System.out.println((format("T%d: started", threadNo)));
        getInput();
        data.inputSignal();

        //wait for tasks 1 and 4 to finish input
        data.waitInput();
        System.out.println(format("T%d: input finished", threadNo));

        getMax();
        computeMatrix();

        data.waitComputations();
        printOutput();
        System.out.println(format("T%d: finished", threadNo));
    }

    private void printOutput() {
        switch (threadNo) {
            case 4:
                int i = 0, n = 15;
                int diff = Integer.MAX_VALUE;
                while(diff >= n) {
                    System.out.println(Arrays.toString(Arrays.copyOfRange(data.A, i, i+15)));
                    i += n;
                    diff = data.A.length - i;
                }
                System.out.println(Arrays.toString(Arrays.copyOfRange(data.A, i, i+diff)));
                break;
        }
    }
}
