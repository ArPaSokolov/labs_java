import java.util.Arrays;
import java.util.Scanner;

public class max_element {
    public static int[] findMaxInRows(int[][] matrix) {
        int n = matrix.length;
        int[] maxElements = new int[n];

        for (int i = 0; i < n; i++) {
            int max = matrix[i][0];
            for (int num : matrix[i]) {
                if (num > max) {
                    max = num;
                }
            }
            maxElements[i] = max;
        }
        return maxElements;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод
        System.out.print("Enter number of rows (n): ");
        int n = scanner.nextInt();
        System.out.print("Enter number of columns (m): ");
        int m = scanner.nextInt();

        int[][] matrix = new int[n][m];

        System.out.println("Enter matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        // Поиск максимальных элементов в строках
        int[] maxValues = findMaxInRows(matrix);

        // Вывод
        System.out.println("Max elenents in each row: " + Arrays.toString(maxValues));
    }
}
