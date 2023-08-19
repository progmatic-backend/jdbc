import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // Felvesszük az adatbázisunk adatait
        String jdbcUrl = "jdbc:mysql://localhost:3306/adatbazis_neve";
        String username = "root";
        String password = "jelszo";

        try {
            // Betöltjük a JDBC Drivert
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Létrehozunk egy kapcsolatot az adatbázissal
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Létrehozunk egy Statementet, amin majd SQL parancsokat tudunk futtatni
            Statement statement = connection.createStatement();

            // Végrehajtunk egy lekérdezést
            String sqlQuery = "SELECT * FROM pizza";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // A ResultSet-be kerülnek be ilyenkor a lekrédezés eredményei
            // Ezen végigiterálunk és kiírjuk őket egyesével
            while (resultSet.next()) {
                int azonosito = resultSet.getInt("pazon");
                String nev = resultSet.getString("pnev");
                int ar = resultSet.getInt("par");

                System.out.println("Azonosító: " + azonosito + ", Név: " + nev + ", Ár:" + ar);
            }

            // Lezárjuk az erőforrásokat, mivel rendkívül elegáns programozók vagyunk
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
