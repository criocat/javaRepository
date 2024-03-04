import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;

public class test1 {



    public static void main(String[] args) {
        BigInteger s = BigInteger.valueOf(Integer.MAX_VALUE);
        s = s.multiply(BigInteger.valueOf(Integer.MIN_VALUE));
        System.out.printf(s.toString());
    }
}