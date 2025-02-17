import java.util.HashSet;
import java.util.Scanner;

public class sum_pair {
    public static int[] findPairWithSum(int[] arr, int target) {
        HashSet<Integer> seenNumbers = new HashSet<>();

        for (int num : arr) {
            int complement = target - num; // вторая часть пары
            if (seenNumbers.contains(complement)) {
                return new int[]{complement, num};
            }
            seenNumbers.add(num);
        }

        return null; // нет пары
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод 
        System.out.print("Enter number of elements: ");
        int n = scanner.nextInt();
        int[] arr = new int[n];

        System.out.println("Enter elements:");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        System.out.print("Enter sum (target): ");
        int target = scanner.nextInt();

        // Поиск пары
        int[] result = findPairWithSum(arr, target);

        // Вывод 
        if (result != null) {
            System.out.println("The pair: " + result[0] + " + " + result[1] + " = " + target);
        } else {
            System.out.println("There is no any pair.");
        }
    }
}
