import java.util.Scanner;

public class twice_even {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // вводим трехзначное число
        System.out.print("Enter a three-digit number: ");
        int number = in.nextInt();

        // проверка на дурака
        if (number > 999 || number < 100) {
            System.out.print("The number is not three-digit or negative!");
            return;
        }

        // делим на разряды
        int hundreds = number / 100; // сотни
        int tens = (number / 10) % 10; // десятки
        int ones = number % 10; // единицы

        // сумма цифр
        int sum = hundreds + tens + ones;

        // произведение цифр
        int product = hundreds * tens * ones;

        // итог
        if (sum % 2 == 0 && product % 2 == 0) {
            System.out.println("Yes, the number is twice even.");
        } else {
            System.out.println("No, the number is not twice even.");
        }
    }
}
