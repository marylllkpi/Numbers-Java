import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DbConnection dbConnection;
        Database database;
        try {
            dbConnection = new DbConnection("db.properties");
            database = new Database(dbConnection.open());
            System.out.println("Connected to the database.");
        } catch (IOException e) {
            System.out.println("Cannot read db.properties: " + e.getMessage());
            return;
        } catch (SQLException e) {
            System.out.println("Cannot connect to the database: " + e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== COMPANY DATABASE =====");
            System.out.println("1. Show all employees");
            System.out.println("2. Show all tasks");
            System.out.println("3. Show employees of a department");
            System.out.println("4. Add a task for an employee");
            System.out.println("5. Show tasks of an employee");
            System.out.println("6. Delete an employee");
            System.out.println("0. Exit");
            String choice = ask("Your choice:");
            try {
                switch (choice) {
                    case "1":
                        database.showEmployees();
                        break;
                    case "2":
                        database.showTasks();
                        break;
                    case "3":
                        database.showDepartments();
                        int departmentId = askNumber("Department ID:");
                        if (departmentId != -1) {
                            database.showEmployeesOfDepartment(departmentId);
                        }
                        break;
                    case "4":
                        int employeeId = askNumber("Employee ID:");
                        if (employeeId != -1) {
                            String description = ask("Task description:");
                            database.addTask(employeeId, description);
                        }
                        break;
                    case "5":
                        int id = askNumber("Employee ID:");
                        if (id != -1) {
                            database.showTasksOfEmployee(id);
                        }
                        break;
                    case "6":
                        int deleteId = askNumber("Employee ID to delete:");
                        if (deleteId != -1) {
                            database.deleteEmployee(deleteId);
                        }
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Wrong menu item, try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }

        dbConnection.close();
        System.out.println("Connection closed. Program finished.");
    }

    private static String ask(String prompt) {
        System.out.print(prompt + " ");
        if (!scanner.hasNextLine()) {
            return "0";
        }
        return scanner.nextLine().trim();
    }

    private static int askNumber(String prompt) {
        String text = ask(prompt);
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
            return -1;
        }
    }
}
