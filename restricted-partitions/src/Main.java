import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> nums = new ArrayList<>();
        ArrayList<Integer> maxCount = new ArrayList<>();
        for(int i = 1; i < 42; i++) {
            nums.add(i);
            maxCount.add(42);
        }
        var startTime = System.nanoTime();
        var res = MathUtils.restrictedPartitions(nums, maxCount);
        var endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e6 + "ms");
        System.out.println(res.subList(0, 30));
    }
}