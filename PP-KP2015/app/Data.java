package app;

import java.util.Arrays;

public class Data {

    int [] A = new int[Launcher.N];
    int [] B = new int[Launcher.N];
    int [] Z = new int[Launcher.N];
    int [][] MK = new int[Launcher.N][Launcher.N];
    int alpha;
    int m = Integer.MIN_VALUE;
    int [] C = new int[Launcher.N];
    int [][] MO = new int[Launcher.N][Launcher.N];
    int F1 = 0, F2 = 0, F3 = 0;

    public synchronized void waitInput() {
        try{
            while (F1 != Launcher.INPUTS) wait();
        } catch (Exception e)  { }
    }

    public synchronized void inputSignal() {
        notify();
        F1++;
    }

    public synchronized void waitMax() {
        try{
            while (F2 != Launcher.P) wait();
        } catch (Exception e)  { }
    }

    public synchronized void maxSignal() {
        notify();
        F2++;
    }

    public synchronized void waitComputations() {
        try{
            while (F3 != Launcher.P) {
                wait();
            }
        } catch (Exception e)  { }
    }

    public synchronized void computationsSignal() {
        notify();
        F3++;
    }

    public synchronized void setMax(int m) {
        if (this.m < m) {
            this.m = m;
        }
    }

    public synchronized int copyMax() {
        return m;
    }

    public synchronized int[] copyC() {
        return Arrays.copyOf(C, C.length);
    }

    public synchronized int[][] copyMO() {
        return Arrays.copyOf(MO, MO.length);
    }

    public synchronized int copyAlpha() {
        return alpha;
    }
}
