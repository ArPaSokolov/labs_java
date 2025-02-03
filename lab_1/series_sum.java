import java.util.Scanner;

public class series_sum {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Input a number of numbers: ");
        int n = in.nextInt(); // вводим количество чисел
        int sum = 0;

        // вводим n чисел и считаем сумму ряда
        for (int i = 0; i < n; i++) { 
            System.out.printf("%d number: ", i + 1);
            int num = in.nextInt();
            if (i % 2 == 0) {
                sum += num; // четные индексы => +
            } else {
                sum -= num; // нечетные индексы => -
            }
        }

        System.out.printf("Sum: %d ", sum); // вывод результата
    }
}
