import java.util.Scanner;

public class maxmin {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // начальные данные
        int bestRoad = -1;
        int bestHeight = -1;

        // количество дорог
        System.out.print("Input number of roads: ");
        int n = in.nextInt(); // количество дорог
        

        // обрабатываем каждую дорогу
        for (int i = 1; i <= n; i++) {
            System.out.print("Input number of tunels: ");
            int m = in.nextInt();  // вводим количество туннелей на текущей дороге
            int minHeight = Integer.MAX_VALUE;

            // вводим высоты туннелей и находим минимальную
            for (int j = 0; j < m; j++) {
                System.out.print("Input height: "); // вводим высоту тунеля
                int height = in.nextInt();
                minHeight = Math.min(minHeight, height);
            }

            // Если минимальная высота текущей дороги больше, чем наилучшая
            if (minHeight > bestHeight) {
                bestHeight = minHeight;
                bestRoad = i;
            }
        }

        // Выводим номер дороги и максимальную минимальную высоту
        System.out.printf("Best road = %d, best height = %d ", bestRoad, bestHeight);
    }
}
