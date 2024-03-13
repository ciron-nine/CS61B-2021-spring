package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        AList <Integer> Ns=new AList ();
        AList <Double>  times=new AList();
        Ns.addLast(1000);
        AList <Integer>test1=new AList();
        Stopwatch sw1=new Stopwatch();
        for(int i=0;i<1000;i++){
            test1.addLast(1);
        }
        double sec1=sw1.elapsedTime();
        times.addLast(sec1);

        AList <Integer>test2=new AList();
        Ns.addLast(10000);
        Stopwatch sw2=new Stopwatch();
        for(int i=0;i<10000;i++){
            test2.addLast(1);
        }
        double sec2=sw2.elapsedTime();
        times.addLast(sec2);

        AList <Integer>test3=new AList();
        Ns.addLast(64000);
        Stopwatch sw3=new Stopwatch();
        for(int i=0;i<64000;i++){
            test3.addLast(1);
        }
        double sec3=sw3.elapsedTime();
        times.addLast(sec3);

        AList <Integer>test4=new AList();
        Ns.addLast(128000);
        Stopwatch sw4=new Stopwatch();
        for(int i=0;i<128000;i++){
            test4.addLast(1);
        }
        double sec4=sw4.elapsedTime();
        times.addLast(sec4);

        printTimingTable(Ns,times,Ns);
        // TODO: YOUR CODE HERE
    }
}
