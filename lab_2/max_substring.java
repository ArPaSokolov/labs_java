import java.util.HashSet;
import java.util.Scanner;

public class max_substring {

    public static void main(String[] args) {
        // Ввод строки
        Scanner scanner = new Scanner(System.in); 
        System.out.print("Enter string: ");
        String input = scanner.nextLine(); // пример строки - abcabcbbadcb

        // Алгоритм скользящего окна
        HashSet<Character> set = new HashSet<>();
        int left = 0;
        int maxLength = 0;
        int start = 0;

        for (int right = 0; right < input.length(); right++) {
            while (set.contains(input.charAt(right))) {
                set.remove(input.charAt(left));
                left++;
            }

            set.add(input.charAt(right));

            if (right - left + 1 > maxLength) {
                maxLength = right - left + 1;
                start = left;
            }
        }

        // Результат
        String result = input.substring(start, start + maxLength);
        System.out.println("The longest string: " + result);
    }
}
