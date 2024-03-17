package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void ArrayRandomTest() {
        StudentArrayDeque<Integer> AD1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> correct = new ArrayDequeSolution<>();
        int randomSize = 10000;
        String test = new String();
        for (int i = 0; i < 10000; i++) {
            int size = correct.size();
            if (size != 0) {
                int sel = StdRandom.uniform(0, 4);
                if (sel == 0) {
                    int random = StdRandom.uniform(randomSize);
                    test += "addFirst("+random+")\n";
                    AD1.addFirst(random);
                    correct.addFirst(random);
                }
                else if (sel == 1) {
                    int random = StdRandom.uniform(randomSize);
                    test += "addLast("+random+")\n";
                    AD1.addLast(StdRandom.uniform(random));
                    correct.addLast(StdRandom.uniform(random));
                }
                else if (sel == 2) {
                    Integer number1 = AD1.removeFirst();
                    Integer number2 = correct.removeFirst();
                    test += "removeFirst()\n";
                    assertEquals(test,number1,number2);
                }
                else {
                    Integer number1 = AD1.removeLast();
                    Integer number2 = correct.removeLast();
                    test += "removeLast()\n";
                    assertEquals(test,number1,number2);
                }
            }
            else {
                int sel = StdRandom.uniform(0, 2);
                if (sel == 0) {
                    int random = StdRandom.uniform(randomSize);
                    test += "addFirst("+random+")\n";

                    AD1.addFirst(random);
                    correct.addFirst(random);
                }
                else {
                    int random = StdRandom.uniform(randomSize);
                    test += "addLast("+random+")\n";

                    AD1.addLast(StdRandom.uniform(random));
                    correct.addLast(StdRandom.uniform(random));
                }
            }
        }
    }
}
