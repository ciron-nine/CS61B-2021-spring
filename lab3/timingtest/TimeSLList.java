package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns=new AList();
        AList<Double> times=new AList();
        AList<Integer> Opcounts=new AList();

        Ns.addLast(1000);
        int opcount=10000;
        Opcounts.addLast(opcount);
        SLList<Integer> test1=new SLList<>();
        for(int i=0;i<1000;i++){
            test1.addLast(1);
        }
        Stopwatch sw1=new Stopwatch();
        for(int i=0;i<opcount;i++){
            test1.getLast();
        }
        double sec1=sw1.elapsedTime();
        times.addLast(sec1);

        Ns.addLast(8000);
        Opcounts.addLast(opcount);
        SLList<Integer> test2=new SLList<>();
        for(int i=0;i<8000;i++){
            test2.addLast(1);
        }
        Stopwatch sw2=new Stopwatch();
        for(int i=0;i<opcount;i++){
            test2.getLast();
        }
        double sec2=sw2.elapsedTime();
        times.addLast(sec2);

        Ns.addLast(32000);
        Opcounts.addLast(opcount);
        SLList<Integer> test3=new SLList<>();
        for(int i=0;i<32000;i++){
            test3.addLast(1);
        }
        Stopwatch sw3=new Stopwatch();
        for(int i=0;i<opcount;i++){
            test3.getLast();
        }
        double sec3=sw3.elapsedTime();
        times.addLast(sec3);

        Ns.addLast(128000);
        Opcounts.addLast(opcount);
        SLList<Integer> test4=new SLList<>();
        for(int i=0;i<128000;i++){
            test4.addLast(1);
        }
        Stopwatch sw4=new Stopwatch();
        for(int i=0;i<opcount;i++){
            test4.getLast();
        }
        double sec4=sw4.elapsedTime();
        times.addLast(sec4);

        printTimingTable(Ns,times,Opcounts);
    }

}
