package TNTProject;

import java.util.Scanner;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class EulerLogic {

    // ! Function defining the ODE: dy/dx = f(x, y)
    static double f(double x, double y, Expression expression) {
        return expression.setVariable("x", x).setVariable("y", y).evaluate();
    }

    // ! Euler's Method implementation
    static double eulerMethod(double x0, double y0, double xEnd, double h, Expression expression) {
        int n = (int) ((xEnd - x0) / h);
        double x = x0;
        double y = y0;

        // ! Loop through each step, updating y using Euler's formula
        for (int i = 0; i < n; i++) {
            y = y + h * f(x, y, expression);
            x = x + h;
        }

        // ? Return the final value of y at x = xEnd
        return y;
    }

    // ! Main method: Entry point for the program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // * Get user input for the ODE, initial values, and step size
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

        // ! Parse the ODE using Exp4j
        try {
            Expression expression = new ExpressionBuilder(function)
                    .variables("x", "y")
                    .build();

            // ! Solve the ODE using Euler's Method
            double result = eulerMethod(x0, y0, xEnd, h, expression);

            // ! Output the result
            System.out.printf("The value of y at x = %.2f is %.5f%n", xEnd, result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid expression - " + e.getMessage());
        }

        scanner.close();
    }
}