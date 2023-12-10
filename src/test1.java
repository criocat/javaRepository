import java.lang.reflect.Array;
import java.util.ArrayList;

public class test1 {


    interface aaa {

    }

    static class bbb implements aaa {
        int a = 10;
        public bbb() {}
    }

    public static void main(String[] args) {
        aaa obj = new bbb();
        Class cl = obj.getClass();
        System.out.println(cl);
    }
}