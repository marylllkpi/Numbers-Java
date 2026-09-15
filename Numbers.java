import java.util.Scanner;
public class Numbers {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter upper limit N: ");
        int n = scanner.nextInt();

        System.out.println("Perfect numbers from 1 to " + n + ":");

        for (int i = 1; i <= n; i++) {

            int sum = 0;

            for (int j = 1; j < i; j++) {
                if (i % j == 0) {
                    sum = sum + j;
                }
            }

            if (sum == i && i != 0) {
                System.out.println(i);
            }
        }
    }
}
