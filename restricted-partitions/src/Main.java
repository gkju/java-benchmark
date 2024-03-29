import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println(MathUtils.restrictedPartitions(new ArrayList<>(Arrays.asList(1, 2, 3)), new ArrayList<>(Arrays.asList(3, 3, 3))));
    }
}