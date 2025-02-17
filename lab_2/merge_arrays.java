import java.util.Arrays;
import java.util.Scanner;

public class merge_arrays {

    public static void main(String[] args) {
        // Ввод данных
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of elements: ");
        int n1 = scanner.nextInt();
        int[] arr1 = new int[n1];
        System.out.println("Enter sorted array: ");
        for (int i = 0; i < n1; i++) {
            arr1[i] = scanner.nextInt();
        }

        System.out.print("Enter number of elements: ");
        int n2 = scanner.nextInt();
        int[] arr2 = new int[n2];
        System.out.println("Enter sorted array: ");
        for (int i = 0; i < n2; i++) {
            arr2[i] = scanner.nextInt();
        }

        int[] mergedArray = new int[n1 + n2]; // длина нового массива
        int i = 0, j = 0, k = 0;

        // Слияние двух отсортированных массивов
        while (i < n1 && j < n2) {
            if (arr1[i] < arr2[j]) {
                mergedArray[k++] = arr1[i++];
            } else {
                mergedArray[k++] = arr2[j++];
            }
        }

        while (i < n1) {
            mergedArray[k++] = arr1[i++];
        }

        while (j < n2) {
            mergedArray[k++] = arr2[j++];
        }

        // Выводим результат
        System.out.println("Merged and sorted array: " + Arrays.toString(mergedArray));
    }
}
