import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private final Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
    }

    private static String text(String value) {
        if (value == null) {
            return "-";
        }
        return value.trim();
    }

    public void showDepartments() throws SQLException {
        String sql = "SELECT DepartmentId, Name, Phone FROM Departments ORDER BY DepartmentId";
        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()) {
            System.out.println("ID  Name         Phone");
            while (rs.next()) {
                System.out.printf("%-3d %-12s %s%n", rs.getInt("DepartmentId"),
                        text(rs.getString("Name")), text(rs.getString("Phone")));
            }
        }
    }

    // 1. all employees
    public void showEmployees() throws SQLException {
        String sql = "SELECT e.EmployeeId, e.LastName, e.FirstName, e.Position, d.Name AS Department "
                + "FROM Employees e JOIN Departments d ON e.DepartmentId = d.DepartmentId "
                + "ORDER BY e.EmployeeId";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            System.out.println("ID  Last name    First name  Position     Department");
            while (rs.next()) {
                System.out.printf("%-3d %-12s %-11s %-12s %s%n", rs.getInt("EmployeeId"),
                        text(rs.getString("LastName")), text(rs.getString("FirstName")),
                        text(rs.getString("Position")), text(rs.getString("Department")));
            }
        }
    }

    // 2. all tasks
    public void showTasks() throws SQLException {
        String sql = "SELECT t.TaskId, t.Description, e.LastName, e.FirstName "
                + "FROM Tasks t JOIN Employees e ON t.EmployeeId = e.EmployeeId "
                + "ORDER BY t.TaskId";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            System.out.println("ID  Description                     Employee");
            while (rs.next()) {
                System.out.printf("%-3d %-31s %s %s%n", rs.getInt("TaskId"),
                        text(rs.getString("Description")),
                        text(rs.getString("LastName")), text(rs.getString("FirstName")));
            }
        }
    }

    // 3. employees of one department
    public void showEmployeesOfDepartment(int departmentId) throws SQLException {
        String sql = "SELECT EmployeeId, LastName, FirstName, Position "
                + "FROM Employees WHERE DepartmentId = ? ORDER BY EmployeeId";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, departmentId);
            try (ResultSet rs = statement.executeQuery()) {
                boolean found = false;
                System.out.println("Employees of department " + departmentId + ":");
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-3d %-12s %-11s %s%n", rs.getInt("EmployeeId"),
                            text(rs.getString("LastName")), text(rs.getString("FirstName")),
                            text(rs.getString("Position")));
                }
                if (!found) {
                    System.out.println("No employees found.");
                }
            }
        }
    }

    private boolean employeeExists(int employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) AS Total FROM Employees WHERE EmployeeId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, employeeId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("Total") > 0;
            }
        }
    }

    // 4. add a task for an employee
    public void addTask(int employeeId, String description) throws SQLException {
        if (!employeeExists(employeeId)) {
            System.out.println("Employee " + employeeId + " is not found.");
            return;
        }
        String sql = "INSERT INTO Tasks (Description, EmployeeId) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, description);
            statement.setInt(2, employeeId);
            statement.executeUpdate();
            System.out.println("Task added for employee " + employeeId + ".");
        }
    }

    // 5. tasks of one employee
    public void showTasksOfEmployee(int employeeId) throws SQLException {
        String sql = "SELECT TaskId, Description FROM Tasks WHERE EmployeeId = ? ORDER BY TaskId";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, employeeId);
            try (ResultSet rs = statement.executeQuery()) {
                boolean found = false;
                System.out.println("Tasks of employee " + employeeId + ":");
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-3d %s%n", rs.getInt("TaskId"), text(rs.getString("Description")));
                }
                if (!found) {
                    System.out.println("No tasks found.");
                }
            }
        }
    }

    // 6. delete an employee (the tasks are deleted by ON DELETE CASCADE)
    public void deleteEmployee(int employeeId) throws SQLException {
        String sql = "DELETE FROM Employees WHERE EmployeeId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, employeeId);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                System.out.println("Employee " + employeeId + " is not found.");
            } else {
                System.out.println("Employee " + employeeId + " is deleted (with his tasks).");
            }
        }
    }
}
