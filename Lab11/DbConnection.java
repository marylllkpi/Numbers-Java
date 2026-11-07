import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public DbConnection(String propertiesFile) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(propertiesFile)) {
            properties.load(in);
        }
        url = properties.getProperty("url");
        user = properties.getProperty("user");
        password = properties.getProperty("password");
    }

    public Connection open() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Cannot close the connection: " + e.getMessage());
        }
    }
}
