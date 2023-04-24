//Julia Zezula styczen 2023

import java.sql.*;
import java.io.File;
import java.io.IOException;

class ZdaniaSQL implements GeneratorZdan {
    //instead of LEFT JOIN - JOIN
    private static final String QUERY = "SELECT  I.Plec,I.IMIE, c.Nazwa as Czynnosc, p.Nazwa as Przedmiot from Zdanie z " +
            "JOIN IMIE I on I.ImieID = z.ImieID " +
            "JOIN czynnosc c on c.CzynnoscID = z.CzynnoscID " +
            "JOIN Przedmiot P ON P.PrzedmiotID = Z.PrzedmiotID " +
            "WHERE Z.ZdanieID = ?";

    Connection sqlConnection = null;
    PreparedStatement sqlStatement = null;

    //metoda przekazuje polozenie pliku z baza danych sqlite
    public void plikBazyDanych(String filename) {
            File f = new File(filename);
            if(!f.exists()){
                throw new RuntimeException("Cannot find file: " + filename);
            }

        try {
            String url = "jdbc:sqlite:" + filename;
            sqlConnection = DriverManager.getConnection(url);
            sqlStatement = sqlConnection.prepareStatement(QUERY);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //metoda odtwarza zdanie na podstawie danych zapisanych w bazie danych
    public String zbudujZdanie(int zdanieID) {
        ResultSet sqlRS = null;
        try {
            sqlStatement.setInt(1, zdanieID);
            sqlRS = sqlStatement.executeQuery();
            if (sqlRS.next()) {
                String extra = " ";
                if (sqlRS.getInt(1) == 0) {
                    extra = "a ";
                }
                return sqlRS.getString(2) + " " + sqlRS.getString(3) + extra + sqlRS.getString(4) +".";
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (sqlRS != null) {
                try {
                    sqlRS.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return null;

    }

    /*
    public static void main(String[] args) {

        ZdaniaSQL zds = new ZdaniaSQL();
        System.out.println("Tu main!");

        zds.plikBazyDanych("Zadanie11.sql");
        System.out.println(zds.zbudujZdanie(1));
        System.out.println(zds.zbudujZdanie(2));
        System.out.println(zds.zbudujZdanie(3));

        System.out.println(zds.zbudujZdanie(4));
        System.out.println(zds.zbudujZdanie(8));
        System.out.println(zds.zbudujZdanie(9));

    } */
}