import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLiteTest {
    public static void main( String args[] ) {
        // The connection to the database.
        Connection connection = null;
        // An object that allows us to run SQL queries or updates.
        Statement statement = null;
        // An object that holds the result of an SQL query or update.
        ResultSet rs = null;

        // This string contains the SQL statement required to create a table with the following columns: ID, NAME, URL, CONVERTED_URL
        String createStatement = "CREATE TABLE if not exists completed_downloads " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " URL            TEXT     NOT NULL, " +
                " CONVERTED_URL  TEXT      NOT NULL )";

        // An example array of names to give the name of the url
        String[] names = { "John", "Jill", "Jack", "Adam", "Whitney", "Jackson", "Johnathan"};

        // An SQL statement that gets the amount of records that has the specified id.
        String checkStatement = "SELECT COUNT(*) FROM completed_downloads WHERE ID = ";

        try {
            // Required class/jar in order to be able to access the database.
            Class.forName("org.sqlite.JDBC");
            // Initialize a connection to the database.
            connection = DriverManager.getConnection("jdbc:sqlite:downloaded.db");
            // Initialize the statement object.
            statement = connection.createStatement();

            statement.executeUpdate(createStatement);

            for (int i = 0; i < 5; i++) {
                String s = generateAddStatement(i, names[i], names[i], "asdfweonogwe" + i);
                //System.out.println(s);
                //System.out.println(checkStatement + i);
                rs = statement.executeQuery(checkStatement + i);
                int size = 0;

                while(rs.next()) {
                    System.out.println(rs.getInt(1));
                    size = rs.getInt(1);
                }

                if(size == 0) {
                    statement.executeUpdate(s);
                }
            }

            rs.close();
            statement.close();
            connection.close();
        } catch ( Exception e ) { System.err.println( e.getClass().getName() + ": " + e.getMessage() ); System.exit(0); }
    }

    public static String generateAddStatement(int id, String name, String channelName, String randomUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO completed_downloads (ID, NAME, URL, CONVERTED_URL)");
        builder.append( "VALUES (");
        builder.append(id);
        builder.append(", '");
        builder.append(name);
        builder.append("', 'www.youtube.com/");
        builder.append(channelName);
        builder.append("', 'www.googlecontent.com/");
        builder.append(randomUrl);
        builder.append("')");

        return builder.toString();
    }
}