package search;

public class BinarySearch {

    //pred: a != null && a отсортирован по невозрастанию
    private static int iterateBinarySearch(int x, int[] a) {
        int l = -1, r = a.length;
        // r - подходящий индекс
        // R <= r
        // R >= 0
        // R принадлежин (l, r]
        // обозначим значение r' - l' за p
        // inv: R принадлежит (l', r'] && p' <= (p / 2)округленное вверх
        while (r - l > 1) {
            // R принадлежит (l', r']
            // r' > l'
            // r' > l' + 1
            // r' >= l' + 2
            int m = (l + r) / 2;
            // m принадлежит [l' + 1, r' - 1]
            // m принадлежит [l + 1, r - 1]
            // m принадлежит [0, a.length - 1]
            if (a[m] <= x) {
                // a[m] <= x
                // m - какой-то подходящий индекс
                // R <= m
                // R принадлежит (l', r']
                // R принадлежит (l', m]
                // p' = m - l' =  (l' + r') / 2 - l' <= (l' - r') / 2 округленное вверх = (p / 2) округленное вверх
                r = m;
                // R принадлежит (l', r'] && p' <= (p / 2) округленное вверх
            }
            else {
                // a[m] > x
                // m - неподходящий индекс
                // R > m
                // R принадлежит (l', r']
                // R принадлежит (m, r']
                // p' = r' - m <= r' - (l' + r') / 2 <= (l' - r') / 2 округленное вверх = (p / 2) округленное вверх
                l = m;
                // R принадлежит (l', r'] && p' <= (p / 2) округленное вверх
            }
            // R принадлежит (l', r'] && p' <= (p / 2) округленное вверх
        }
        // когда p' <= 1 цикл прекращается, p' = r' - l' конечен и с каждой итерацией p' = (p / 2) округленное
        // вверх => цикл отработает <= lon(r' - l') округленное вверх раз
        // r' - l' <= 1
        // R принадлежит (l', r']
        // post: r' - l' == 1

        // R принадлежит (r' - 1, r']
        // R = r'
        return r;
    }
    // post: R = минимальный j : x >= a[j]




    // pred: a != null && R принадлежит (l, r] && a отсортирован по невозрастанию
    private static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        // обозначим значение r' - l' за p
        if (r - l == 1) {
            // R принадлежит (r - 1, r]
            // R = r
            // рекурсивно вызвались 0 раз
            return r;
        } else {
            // R принадлежит (l, r]
            // r >= l + 1
            // r != l + 1
            // r >= l + 2
            int m = (l + r) / 2;
            // m принадлежит [l + 1, r - 1]
            // m принадлежит [0, a.length - 1]
            if (a[m] <= x) {
                // a[m] <= x
                // m - какой-то подходящий индекс
                // R <= m
                // R принадлежит (l, r]
                // R принадлежит (l, m]
                // p' = m - l' =  (l' + r') / 2 - l' <= (l' - r') / 2 округленное вверх = (p / 2) округленное вверх
                r = m;
                // R принадлежит (l', r'] && p' <= (p / 2) округленное вверх
            }
            else {
                // a[m] > x
                // m - неподходящий индекс
                // R > m
                // R принадлежит (l, r]
                // R принадлежит (m, r]
                // p' = r' - m <= r' - (l' + r') / 2 <= (l' - r') / 2 округленное вверх = (p / 2) округленное вверх
                l = m;
                // R принадлежит (l', r'] && p' <= (p / 2) округленное вверх
            }
            // R принадлежит (l', r'] && p' <= (p / 2) округленное вверх
            // a != null && a отсортирован по невозрастанию
            // предусловия функции выполнены
            return recursiveBinarySearch(x, a, l, r);
            // рекурсивно вызвались 1 раз от меньшего полуинтервала
            // вернет R
        }
        // рекурсивно вызвали функцию от меньшего интервала не более 1 раза
    }
    // post: R = минимальный j : x >= a[j]; функция рекурсивно вызвалась не больше одного раза от полуинтервала размера <= (p / 2) округленное вверх
    // так как функция рекурсивно вызвалась не больше одного раза от полуинтервала размера <= (p / 2) округленное вверх то количество рекурсивных запусков будет <= log(r - l) округленное вверх





    // считаем что последний элемент массива a это минус бесконечность, его мы не передаем в main
    // j назовем подходящим индексом если x >= a[j]

    // pred: на вход подается набор целых чисел длины >= 1 (число x, элементы массива a) && элементы массива a
    // отсортированны по невозрастанию
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        // подалось хотябы 1 целове число поэтому ошибки не будет
        int[] a = new int[args.length - 1];
        // args.length - 1 >= 0 т.е. ошибки не будет
        for (int i = 0; i < args.length - 1; ++i) {
            // 0 <= i <= a.length - 1
            // 1 <= i + 1 <= args.length - 1
            // выхода за границы не произойдет и мы считаем все значения из входных данных кроме первого в массив a
            a[i] = Integer.parseInt(args[i + 1]);
        }
        // R > -1
        // a.length - подходящий индекс
        // R <= a.length
        // a != null && R принадлежит (-1, a.length] && a отсортирован по невозрастанию
        System.out.println(recursiveBinarySearch(x, a, -1, a.length));
        // выведет R
    }
    // post: выведет такой минимальный j что x >= a[j]
}
