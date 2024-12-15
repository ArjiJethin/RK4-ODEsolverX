package TNTProject;

import java.util.Scanner;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class RK4Logic {

    // ! Function defining the ODE: dy/dx = f(x, y)
    // * This method takes the current values of x and y, along with the parsed
    // * expression representing the ODE,
    // * and evaluates the expression at those values of x and y.

    static double f(double x, double y, Expression expression) {
        // * Set the variables 'x' and 'y' in the expression, then evaluate the
        // * expression
        return expression.setVariable("x", x).setVariable("y", y).evaluate();
    }

    // ! Runge-Kutta 4th order method implementation
    // * This method solves the ODE using the RK4 method by iterating through the
    // * steps and computing the intermediate
    // * values (k1, k2, k3, k4) and updating the value of y accordingly.
    static double rungeKutta(double x0, double y0, double xEnd, double h, Expression expression) {
        // *Calculate the number of steps required based on the range (x0 to xEnd) and
        // *step size (h)
        int n = (int) ((xEnd - x0) / h);
        double x = x0;
        double y = y0;

        // ! Loop through each step, calculating k1, k2, k3, k4, and updating y
        for (int i = 0; i < n; i++) {
            // Compute the intermediate values using the ODE
            double k1 = h * f(x, y, expression);
            double k2 = h * f(x + h / 2, y + k1 / 2, expression);
            double k3 = h * f(x + h / 2, y + k2 / 2, expression);
            double k4 = h * f(x + h, y + k3, expression);

            // ! Update y using the weighted average of the intermediate values
            y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6;

            // ! Increment x by the step size
            x = x + h;
        }

        // ? Return the final value of y at x = xEnd
        return y;
    }

    // ! Main method: Entry point for the program
    public static void main(String[] args) {
        // ! Create a Scanner object to get input from the user
        Scanner scanner = new Scanner(System.in);

        // *Get user input for the ODE, initial values, and step size
        System.out.print("Enter the ODE (e.g., '1 + y^2' or 'x + y'): ");
        String function = scanner.nextLine(); // * Read the ODE function from the user input

        System.out.print("Enter initial x value (x0): ");
        double x0 = scanner.nextDouble(); // *Read initial x value from the user

        System.out.print("Enter initial y value (y0): ");
        double y0 = scanner.nextDouble(); // *Read initial y value from the user

        System.out.print("Enter endpoint x value (xEnd): ");
        double xEnd = scanner.nextDouble(); // *Read the endpoint value of x from the user

        System.out.print("Enter step size (h): ");
        double h = scanner.nextDouble(); // *Read the step size from the user

        // ! Parse the ODE using Exp4j
        try {
            // * Use the ExpressionBuilder from exp4j to create an Expression object from
            // *the input function
            Expression expression = new ExpressionBuilder(function)
                    .variables("x", "y") // *Define 'x' and 'y' as the variables used in the function
                    .build(); // *Build the expression

            // !Solve the ODE using Runge-Kutta method
            double result = rungeKutta(x0, y0, xEnd, h, expression);

            // ! Output the result
            System.out.printf("The value of y at x = %.2f is %.5f%n", xEnd, result);
        } catch (IllegalArgumentException e) {
            // ! Handle errors if the input expression is invalid
            System.out.println("Error: Invalid expression - " + e.getMessage());
        }

        // *Close the scanner object to prevent resource leakage
        scanner.close();
    }
}
