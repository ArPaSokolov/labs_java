import java.util.Scanner;

public class syracuse_sequence {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Input a number: ");
        int n = in.nextInt(); // вводим число
        int steps = 0;

        // шаги и их подсчет
        while (n != 1) { 
            if (n % 2 == 0) {
                n /= 2;
            } else {
                n = 3 * n + 1;
            }
            steps++; 
        }

        System.out.printf("Number of steps: %d ", steps); // вывод результата
    }
}
