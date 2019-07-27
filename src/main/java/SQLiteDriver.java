import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class SQLiteDriver {
    // The connection to the database.
    private Connection connection = null;
    // An object that allows us to run SQL queries or updates on the database.
    private PreparedStatement preparedStatement = null;
    // An object that holds the result of an SQL query or update.
    private ResultSet resultSet = null;

    // This string contains the SQL statement required to create a table with the following columns: ID, NAME, URL, CONVERTED_URL
    private String createTableStatement = "CREATE TABLE if not exists downloads " +
            "(ID INT PRIMARY KEY     NOT NULL," +
            " NAME           NTEXT    NOT NULL, " +
            " URL            NTEXT     NOT NULL, " +
            " CONVERTED_URL  NTEXT      NOT NULL, " +
            " DOWNLOADED  BIT      NOT NULL )";

    private String createDownloadEntry = "INSERT INTO ";

    public SQLiteDriver() {
        createTable();
    }

    /**
     * Creates a table with the following columns: ID, NAME, URL, CONVERTED_URL, DOWNLOADED.
     */
    public void createTable() {
        try {
            // Initialize a connection to the database.
            connection = DriverManager.getConnection("jdbc:sqlite:downloads.db");

            preparedStatement = connection.prepareStatement(createTableStatement);
            preparedStatement.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Closes all connections with the database and destroys all objects.
     */
    public void closeConnections() {
        try{
            if(resultSet != null) { resultSet.close(); resultSet = null; }
            if(preparedStatement != null) { preparedStatement.close(); preparedStatement = null;}
            if(connection != null) { connection.close(); connection = null; }
        } catch (Exception e) { System.exit(0); }
    }

//    public static void main(String[] args) {
//        SQLiteDriver sDriver = new SQLiteDriver();
//        sDriver.createTable();
//        sDriver.closeConnections();
//    }
}