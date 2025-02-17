import java.util.Scanner;

public class max_subarray_sum {

    public static void main(String[] args) {
        // Ввод
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of elements: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];

        System.out.println("Enter elements:");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        int maxSum = arr[0];
        int currentSum = arr[0];

        // Поиск максимальной суммы
        for (int i = 1; i < n; i++) {
            if (arr[i] > arr[i-1] ) {
                currentSum += arr[i];
            }
            else {
                maxSum = Math.max(maxSum, currentSum);
                currentSum = arr[i];
            }
        }
        maxSum = Math.max(maxSum, currentSum);

        // Вывод
        System.out.println("Max summ of subarray: " + maxSum);
    }
}
