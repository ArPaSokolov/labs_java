import java.util.Scanner;

public class sum_matrix {
    public static int sumMatrix(int[][] matrix) {
        int sum = 0;
        for (int[] row : matrix) {
            for (int num : row) {
                sum += num;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод
        System.out.print("Enter number of rows (n): ");
        int n = scanner.nextInt();
        System.out.print("Enter number of columns  (m): ");
        int m = scanner.nextInt();

        int[][] matrix = new int[n][m];

        System.out.println("Enter matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        // Вычисление суммы элементов
        int sum = sumMatrix(matrix);

        // Вывод
        System.out.println("Sum of elements in matrix: " + sum);
    }
}
