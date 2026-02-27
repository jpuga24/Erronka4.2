import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class Main {
    public static void esportatuJSON(String fitxIzena, List<Produktua> produktuak) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fitxIzena))) {
            out.println("[");
            for (int i = 0; i < produktuak.size(); i++) {
                Produktua p = produktuak.get(i);
                out.printf(
                    "  {\"id\": %d, \"izena\": \"%s\", \"deskribapena\": \"%s\", \"prezioa\": %.2f, \"stock\": %d, \"kategoria\": \"%s\", \"irudia\": \"%s\"}%s\n",
                    p.getId(), p.getIzena(), p.getDeskribapena(), p.getPrezioa(), p.getStocka(),
                    p.getKategoria(), p.getIrudia(),
                    (i == produktuak.size() - 1) ? "" : ","
                );
            }
            out.println("]");
            System.out.println("JSON sortuta: " + fitxIzena);
        } catch (IOException e) {
            System.out.println("Errorea JSON esportatzerakoan.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String izena;
        String deskribapena;
        double prezioa;
        int stocka;
        String kategoria;
        String irudia;
        String sortzeData;

        Scanner sc = new Scanner(System.in);
        int aukera = -1;

        while (aukera != 0) {
            System.out.println("\n--- Opzioak ---");
            System.out.println("1- Produktuak berriak gehitu");
            System.out.println("2- Produktuak CSV fitxategitik kargatzea");
            System.out.println("3- Dauden produktuak eguneratzea");
            System.out.println("4- Produktuak ezabatzea");
            System.out.println("5- Informazioa esportatzea");
            System.out.println("6- Produktuak zerrendatzea");
            System.out.println("7- Produktuak bilatzea");
            System.out.println("0- Irten");
            System.out.println("Aukeratu:");
            aukera = sc.nextInt();
            sc.nextLine();

            switch (aukera) {
                case 1:
                    System.out.println("Produktua sartu");

                    System.out.println("Izena: ");
                    izena = sc.nextLine();

                    System.out.println("Produktuaren deskribapena: ");
                    deskribapena = sc.nextLine();

                    System.out.println("Prezioa: ");
                    prezioa = sc.nextDouble();

                    System.out.println("Stock erabilgarria: ");
                    stocka = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Kategoria: ");
                    kategoria = sc.nextLine();

                    System.out.println("Irudia: ");
                    irudia = sc.nextLine();

                    System.out.println("Sortze-data (YYYY-MM-DD): ");
                    sortzeData = sc.nextLine();

                    try (Connection conn = DBKonexioa.getConnection()) {
                        String sql = "INSERT INTO Produktuak(Izena, Deskribapena, Prezioa, stocka, Kategoria, Irudia, Sortze_data) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, izena);
                        ps.setString(2, deskribapena);
                        ps.setDouble(3, prezioa);
                        ps.setInt(4, stocka);
                        ps.setString(5, kategoria);
                        ps.setString(6, irudia);
                        ps.setDate(7, java.sql.Date.valueOf(sortzeData));

                        int lerroak = ps.executeUpdate();
                        System.out.println("Produktua ondo gehitu da. Gehitutako lerroak: " + lerroak);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Errorea: Dataren formatua ez da zuzena. Erabili YYYY-MM-DD.");
                    }
                    break;

                case 2:
                    System.out.println("Sartu CSV fitxategiaren izena (adibidez, produktuak.csv):");
                    String fitxategiIzena = sc.nextLine();

                    try (Connection conn = DBKonexioa.getConnection();
                        BufferedReader br = new BufferedReader(new FileReader(fitxategiIzena))) {

                        String linea;
                        br.readLine();

                        String sql = "INSERT INTO Produktuak(Izena, Deskribapena, Prezioa, stocka, Kategoria, Irudia, Sortze_data) VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
                        PreparedStatement ps = conn.prepareStatement(sql);

                        int kont = 0;
                        while ((linea = br.readLine()) != null) {
                            String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                            ps.setString(1, datos[1].replace("\"", ""));
                            ps.setString(2, datos[2].replace("\"", ""));
                            ps.setDouble(3, Double.parseDouble(datos[3]));
                            ps.setInt(4, Integer.parseInt(datos[4]));
                            ps.setString(5, datos[6].replace("\"", ""));
                            ps.setString(6, datos[7].replace("\"", ""));

                            ps.executeUpdate();
                            kont++;
                        }
                        System.out.println("CSV kargatuta. " + kont + " produktu gehitu dira.");

                    } catch (FileNotFoundException e) {
                        System.out.println("Errorea: Ez da aurkitu '" + fitxategiIzena + "' izeneko fitxategirik. Ziurtatu proiektuaren karpeta nagusian dagoela.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    System.out.println("Produktuak eguneratzea");
                    System.out.println("Produktuaren izena (eguneratzeko): ");
                    izena = sc.nextLine();

                    System.out.println("Prezio berria: ");
                    double prezioBerria = sc.nextDouble();

                    System.out.println("Stock berria: ");
                    int stockBerria = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Irudi berria: ");
                    String irudiBerria = sc.nextLine();

                    try (Connection conn = DBKonexioa.getConnection()) {
                        String sql = "UPDATE Produktuak SET Prezioa=?, stocka=?, Irudia=? WHERE Izena=?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setDouble(1, prezioBerria);
                        ps.setInt(2, stockBerria);
                        ps.setString(3, irudiBerria);
                        ps.setString(4, izena);

                        int lerroak = ps.executeUpdate();
                        System.out.println("Eguneratze emaitza: " + lerroak + " lerro aldatuta.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    System.out.println("Produktuak ezabatzea");
                    System.out.println("Produktuaren izena: ");
                    izena = sc.nextLine();

                    try (Connection conn = DBKonexioa.getConnection()) {
                        String sql = "DELETE FROM Produktuak WHERE Izena=?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, izena);

                        int tuplak = ps.executeUpdate();
                        System.out.println("Ezabatutako produktu kopurua: " + tuplak);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 5:
                    System.out.println("Informazioa esportatzea...");
                    List<Produktua> produktuKarpeta = new ArrayList<>();
                    try (Connection conn = DBKonexioa.getConnection()) {
                        String sql = "SELECT * FROM Produktuak";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            Produktua p = new Produktua(
                                rs.getInt("Id_produktua"),
                                rs.getString("Izena"),
                                rs.getString("Deskribapena"),
                                rs.getDouble("Prezioa"),
                                rs.getInt("stocka"),
                                rs.getString("Kategoria"),
                                rs.getString("Irudia")
                            );
                            produktuKarpeta.add(p);
                        }
                        esportatuJSON("produktuak.json", produktuKarpeta);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 6:
                    System.out.println("Produktuak zerrendatzea");
                    System.out.println("1- Produktu guztien zerrenda");
                    System.out.println("2- Kategorien araberako zerrenda");
                    int seiAukera = sc.nextInt();
                    sc.nextLine();

                    if (seiAukera == 1) {
                        try (Connection conn = DBKonexioa.getConnection()) {
                            String sql = "SELECT * FROM Produktuak";
                            Statement st = conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            System.out.println("--- Produktu Guztiak ---");
                            while(rs.next()){
                                System.out.println("ID: " + rs.getInt("Id_produktua") + " | Izena: " + rs.getString("Izena"));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Sartu kategoriaren izena: ");
                        kategoria = sc.nextLine();
                        try (Connection conn = DBKonexioa.getConnection()) {
                            String sql = "SELECT * FROM Produktuak WHERE Kategoria=?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, kategoria);
                            ResultSet rs = ps.executeQuery();
                            System.out.println("--- " + kategoria + " kategoriako produktuak ---");
                            while(rs.next()){
                                System.out.println("Izena: " + rs.getString("Izena"));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case 7:
                    System.out.println("Produktuak bilatzea");
                    System.out.println("1- Produktuaren izena");
                    System.out.println("2- Produktuaren deskribapena");
                    int zazpiAukera = sc.nextInt();
                    sc.nextLine();

                    if (zazpiAukera == 1) {
                        System.out.println("Sartu produktuaren izena: ");
                        izena = sc.nextLine();
                        try (Connection conn = DBKonexioa.getConnection()) {
                            String sql = "SELECT * FROM Produktuak WHERE Izena LIKE ?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, "%" + izena + "%");
                            ResultSet rs = ps.executeQuery();
                            while(rs.next()){
                                System.out.println("Aurkitua: " + rs.getString("Izena") + " - " + rs.getDouble("Prezioa") + "€");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Sartu produktuaren deskribapena: ");
                        deskribapena = sc.nextLine();
                        try (Connection conn = DBKonexioa.getConnection()) {
                            String sql = "SELECT * FROM Produktuak WHERE Deskribapena LIKE ?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, "%" + deskribapena + "%");
                            ResultSet rs = ps.executeQuery();
                            while(rs.next()){
                                System.out.println("Aurkitua: " + rs.getString("Izena") + " [" + rs.getString("Deskribapena") + "]");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case 0:
                    System.out.println("Agur!");
                    break;

                default:
                    System.out.println("Aukeratu opzio baliodun bat.");
                    break;
            }
        }
        sc.close();
    }
}