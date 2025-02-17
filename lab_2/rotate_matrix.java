import java.util.Arrays;
import java.util.Scanner;

public class rotate_matrix {
    public static int[][] rotate90Clockwise(int[][] matrix) {
        int n = matrix.length;  
        int m = matrix[0].length; 
        int[][] rotated = new int[m][n]; // Повернутая матрица

        // Заполняем новую матрицу
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        return rotated;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) {
        // Ввод матрицы
        Scanner scanner = new Scanner(System.in);

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
        System.out.println("Original matrix:");
        printMatrix(matrix);

        int[][] rotatedMatrix = rotate90Clockwise(matrix);
        
        System.out.println("Rotated matrix:");
        printMatrix(rotatedMatrix);
    }
}
