import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/progmatic";
        String username = "root";
        String password = "root";

        try {

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                 Statement statement = connection.createStatement()) {

//                String insertQuery = "INSERT INTO pizza (pazon, pnev, par) VALUES (9, 'Hawaii', '800')";
//                int insertedRows = statement.executeUpdate(insertQuery);
//                System.out.println(insertedRows + " row(s) inserted.");
//
//                String updateQuery = "UPDATE pizza SET par = 900 WHERE pnev = 'Hawaii'";
//                int updatedRows = statement.executeUpdate(updateQuery);
//                System.out.println(updatedRows + " row(s) updated.");

                String preparedSql = "SELECT * FROM futar WHERE fazon = ? OR fnev = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(preparedSql)) {
                    preparedStatement.setInt(1, 3);
                    preparedStatement.setString(2, "Gyalogkakukk");

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            int azonosito = resultSet.getInt("fazon");
                            String nev = resultSet.getString("fnev");
                            String tel = resultSet.getString("ftel");

                            System.out.println("Azonosító: " + azonosito + ", Név: " + nev + ", Telefon: " + tel);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
