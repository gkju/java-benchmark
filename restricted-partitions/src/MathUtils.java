import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MathUtils {
    public static void fft(ArrayList<Complex> vec, boolean invert) {
        int n = vec.size();
        if(n == 1) {
            return;
        }

        ArrayList<Complex> even = new ArrayList<>(n / 2), odd = new ArrayList<>(n / 2);
        for(int i = 0; 2 * i < n; i++) {
            even.add(vec.get(2 * i));
            odd.add(vec.get(2 * i + 1));
        }
        fft(even, invert);
        fft(odd, invert);

        double arg = 2 * Math.PI / n * (invert ? -1 : 1);
        Complex nthRoot = new Complex(Math.cos(arg), Math.sin(arg)), currentRoot = new Complex(1, 0);
        for(int i = 0; 2 * i < n; i++) {
            Complex coeff = currentRoot.multiply(odd.get(i));
            vec.set(i, even.get(i).add(coeff));
            vec.set(i + n / 2, even.get(i).subtract(coeff));
            if(invert) {
                vec.set(i, vec.get(i).divide(2));
                vec.set(i + n / 2, vec.get(i + n / 2).divide(2));
            }
            currentRoot = currentRoot.multiply(nthRoot);
        }
    }

    private static void reVecToCompVec(ArrayList<Complex> out, ArrayList<Integer> in) {
        out.ensureCapacity(in.size());
        out.clear();

        for (Integer i : in) {
            out.add(new Complex(i));
        }
    }

    private static void padVec(ArrayList<Complex> vec, int n) {
        while(vec.size() < n) {
            vec.add(new Complex(0, 0));
        }
    }

    public static final ArrayList<Integer> fastPolyMul(ArrayList<Integer> a, ArrayList<Integer> b) {
        int resultSize = a.size() + b.size() - 1;
        ArrayList<Complex> apad = new ArrayList<>(a.size()), bpad = new ArrayList<>(b.size());
        reVecToCompVec(apad, a);
        reVecToCompVec(bpad, b);

        int n = 1;
        while(n < a.size() + b.size()) {
            n *= 2;
        }
        padVec(apad, n);
        padVec(bpad, n);

        fft(apad, false);
        fft(bpad, false);
        for(int i = 0; i < n; i++) {
            apad.set(i, apad.get(i).multiply(bpad.get(i)));
        }
        fft(apad, true);

        ArrayList<Integer> result = new ArrayList<>(resultSize);
        for(int i = 0; i < resultSize; i++) {
            result.add((int) Math.round(apad.get(i).getReal()));
        }

        return result;
    }

    public static final void memberwiseAdd(ArrayList<Integer> target, ArrayList<Integer> input) {
        if(target.size() < input.size()) {
            target.ensureCapacity(input.size());
            while(target.size() < input.size()) {
                target.add(0);
            }
        }

        for(int i = 0; i < input.size(); i++) {
            target.set(i, target.get(i) + input.get(i));
        }
    }

    private static ArrayList<Integer> generatePartitionPoly(int num, int limit) {
        ArrayList<Integer> res = new ArrayList<>();
        for(int i = 0; i <= num * limit; ++i) {
            res.add(i % num == 0 ? 1 : 0);
        }

        return res;
    }

    public static ArrayList<Integer> restrictedPartitions(ArrayList<Integer> numbers, ArrayList<Integer> maxCount) {
        if(maxCount.size() < numbers.size()) {
            throw new IllegalArgumentException("Wektor maxCount powinien zawierac ile razy mozna uzyc kazda z liczb");
        }
        // zero mozna uzyskac biorac kazda liczbe zero razy
        ArrayList<Integer> result = new ArrayList<>(List.of(1));

        for(int i = 0; i < numbers.size(); ++i) {
            result = fastPolyMul(result, generatePartitionPoly(numbers.get(i), maxCount.get(i)));
        }

        return result;
    }
}
