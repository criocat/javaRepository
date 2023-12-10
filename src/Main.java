public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");

        int a = 4;
        for (int i = 1; i <= 5; i++){
            a *= 2;
            int b = a - 3;
            System.out.println(b);
            System.out.println("i = " + i);
        }
    }
}