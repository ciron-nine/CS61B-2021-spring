package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove(){
        BuggyAList<Integer> test1=new BuggyAList<>();
        AListNoResizing<Integer> true1=new AListNoResizing<>();
        test1.addLast(4);true1.addLast(4);
        test1.addLast(5);true1.addLast(5);
        test1.addLast(6);true1.addLast(6);
        assertEquals(test1.size(),true1.size());
        assertEquals(test1.removeLast(),true1.removeLast());
        assertEquals(test1.removeLast(),true1.removeLast());
        assertEquals(test1.removeLast(),true1.removeLast());

    }
    @Test
    public void randomsizeTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                assertEquals("addLast(" + randVal + ")","addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int sizeL = L.size();
                int sizeB = B.size();
                assertEquals("size: " + sizeL,"size: " + sizeB);
            } else if (operationNumber == 2){
                // removelist
                if (L.size()>0){
                    int lastL=L.removeLast();
                    int lastB=B.removeLast();
                    assertEquals("removeLast: "+lastL,"removeLast: "+lastB);
                }
            } else if (operationNumber == 3){
                // getlast
                if (L.size()>0){
                    int lastL=L.getLast();
                    int lastB=B.getLast();
                    assertEquals("getLast: "+lastL,"getLast: "+lastB);
                }
            }
        }
    }
}
