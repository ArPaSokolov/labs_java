import java.util.Scanner;

public class treasure {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // начальные данные
        int currentX = 0, currentY = 0;
        int steps = 0;
        int minSteps = 0;
        
        // вводим координаты клада
        System.out.print("Input X: ");
        int targetX = in.nextInt();
        System.out.print("Input Y: ");
        int targetY = in.nextInt();

        // очистка буфера после чтения целых чисел
        in.nextLine();

        // читаем карту
        while (true) {
            System.out.print("Input direction: ");
            String direction = in.nextLine();  // направление

            if (direction.equals("stop")) {
                System.out.println("Minimal number of steps: " + minSteps); // конец пути
                return;
            }
            
            System.out.print("Input distance: ");
            int distance = Integer.parseInt(in.nextLine()); // число шагов

            // движение по координатам в зависимости от направления
            switch (direction) {
                case "north": currentY += distance; break;
                case "south": currentY -= distance; break;
                case "east": currentX += distance; break;
                case "west": currentX -= distance; break;
            }

            steps++; // шаги

            // если достигли клада, выводим результат и завершаем программу
            if (currentX == targetX && currentY == targetY) {
                minSteps = steps;
            }
        }
    }
}
