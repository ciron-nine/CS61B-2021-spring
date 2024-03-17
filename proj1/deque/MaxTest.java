package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class MaxTest {
    @Test
    public  void CompareTest(){
        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        Compartor1<Integer> test = new Compartor1<>();
        Compartor2<Integer> test2 = new Compartor2<>();
        MaxArrayDeque<Integer> maxtest = new MaxArrayDeque<>(test);
        for(int i = 0; i < 8; i++){
            maxtest.addFirst(i);
        }
        int max_num = maxtest.max();
        int max_num2 = maxtest.max(test2);
        assertEquals("should the same",max_num, 7);
        assertEquals("should the same",max_num2, 7);
    }
}
