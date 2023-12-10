//import java.io.FileOutputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Scanner;
//public class Reverse_v1 {
//    static class StringArray{
//        int maxLen = 1;
//        int len = 0;
//        String[] arr = new String[1];
//        private void expand() {
//            maxLen *= 2;
//            String[] newArr = new String[maxLen];
//            for (int i = 0; i < arr.length; ++i) {
//                newArr[i] = arr[i];
//            }
//            arr = newArr;
//        }
//        public void push_back(String s) {
//            if (len == maxLen) {
//                expand();
//            }
//            arr[len++] = s;
//        }
//        public String get(int ind) {
//            return arr[ind];
//        }
//    }
//    public static void main(String[] args) {
//        try {
//            FileOutputStream out = new FileOutputStream("output.txt");
//        MyScanner sc = new MyScanner(System.in);
//        int cntStr = 0;
//        StringArray strArray = new StringArray();
//        String nextString = sc.nextLine();
//        while(nextString != null) {
//            strArray.push_back(nextString);
//            ++cntStr;
//            nextString = sc.nextLine();
//        }
//        out.close();
//        int pointerL = -1;
//        int pointerR = -1;
//        for (int line = cntStr - 1; line > -1; --line) {
//            char lastChar = 'a';
//            String curString = strArray.get(line);
//            for (int i = curString.length() - 1; i > -1; --i) {
//                char curChar = curString.charAt(i);
//                if (curChar != ' ') {
//                    if (pointerL == -1) {
//                        pointerL = i + 1;
//                        pointerR = i;
//                    }
//                    --pointerL;
//                }
//                else if (lastChar != ' ') {
//                    if (pointerL != -1) {
//                        System.out.print(curString.substring(pointerL, pointerR + 1));
//                        pointerL = -1;
//                        pointerR = -1;
//                    }
//                    System.out.print(curChar);
//                }
//                lastChar = curChar;
//            }
//            if (pointerL != -1) {
//                System.out.print(curString.substring(pointerL, pointerR + 1));
//                pointerL = -1;
//                pointerR = -1;
//            }
//            System.out.print('\n');
//        }
//        } catch(Exception e) {}
//    }
//}