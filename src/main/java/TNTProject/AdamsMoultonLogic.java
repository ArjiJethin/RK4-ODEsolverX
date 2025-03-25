package TNTProject;

import java.util.Scanner;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class AdamsMoultonLogic {

    static double f(double x, double y, Expression expression) {
        return expression.setVariable("x", x).setVariable("y", y).evaluate();
    }

    static double rungeKuttaStep(double x, double y, double h, Expression expression) {
        double k1 = h * f(x, y, expression);
        double k2 = h * f(x + h / 2, y + k1 / 2, expression);
        double k3 = h * f(x + h / 2, y + k2 / 2, expression);
        double k4 = h * f(x + h, y + k3, expression);
        return y + (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }

    static double adamsMoulton(double x0, double y0, double xEnd, double h, Expression expression) {
        int n = (int) ((xEnd - x0) / h);
        double[] x = new double[n + 1];
        double[] y = new double[n + 1];
        double[] f = new double[n + 1];

        x[0] = x0;
        y[0] = y0;
        f[0] = f(x[0], y[0], expression);

        for (int i = 1; i < 4; i++) {
            y[i] = rungeKuttaStep(x[i - 1], y[i - 1], h, expression);
            x[i] = x[i - 1] + h;
            f[i] = f(x[i], y[i], expression);
        }

        for (int i = 3; i < n; i++) {
            double yPredict = y[i] + h / 24 * (55 * f[i] - 59 * f[i - 1] + 37 * f[i - 2] - 9 * f[i - 3]);
            double fPredict = f(x[i + 1], yPredict, expression);
            y[i + 1] = y[i] + h / 24 * (9 * fPredict + 19 * f[i] - 5 * f[i - 1] + f[i - 2]);

            x[i + 1] = x[i] + h;
            f[i + 1] = f(x[i + 1], y[i + 1], expression);
        }

        return y[n];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ODE (e.g., '1 + y^2' or 'x + y'): ");
        String function = scanner.nextLine();

        System.out.print("Enter initial x value (x0): ");
        double x0 = scanner.nextDouble();

        System.out.print("Enter initial y value (y0): ");
        double y0 = scanner.nextDouble();

        System.out.print("Enter endpoint x value (xEnd): ");
        double xEnd = scanner.nextDouble();

        System.out.print("Enter step size (h): ");
        double h = scanner.nextDouble();

        try {
            Expression expression = new ExpressionBuilder(function)
                    .variables("x", "y")
                    .build();

            double result = adamsMoulton(x0, y0, xEnd, h, expression);
            System.out.printf("The value of y at x = %.2f is %.5f%n", xEnd, result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid expression - " + e.getMessage());
        }

        scanner.close();
    }
}
