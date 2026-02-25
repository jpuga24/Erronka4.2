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
        int stock;
        String kategoria;
        String irudia;

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
                    stock = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Kategoria: ");
                    kategoria = sc.nextLine();

                    System.out.println("Produktuaren irudiak: ");
                    irudia = sc.nextLine();

                    try (Connection conn = DBKonexioa.getConnection()) {
                        String sql = "INSERT INTO produktuak(izena, deskribapena, prezioa, stock, kategoria, irudia) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, izena);
                        ps.setString(2, deskribapena);
                        ps.setDouble(3, prezioa);
                        ps.setInt(4, stock);
                        ps.setString(5, kategoria);
                        ps.setString(6, irudia);

                        int lerroak = ps.executeUpdate();
                        System.out.println("Produktua ondo gehitu da. Gehitutako lerroak: " + lerroak);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.println("Sartu CSV fitxategiaren bidea:");
                    String ruta = sc.nextLine();

                    try (Connection conn = DBKonexioa.getConnection();
                        BufferedReader br = new BufferedReader(new FileReader(ruta))) {

                        String linea;
                        br.readLine();

                        String sql = "INSERT INTO produktuak(izena, deskribapena, prezioa, stock, kategoria, irudia) VALUES (?, ?, ?, ?, ?, ?)";
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
                        String sql = "UPDATE produktuak SET prezioa=?, stock=?, irudia=? where izena=?";
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
                        String sql = "DELETE FROM produktuak WHERE izena=?";
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
                    List<Produktua> produktuak = new ArrayList<>();
                    try (Connection conn = DBKonexioa.getConnection()) {
                        String sql = "SELECT * FROM produktuak";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            Produktua p = new Produktua(
                                rs.getInt("Id_produktua"),
                                rs.getString("izena"),
                                rs.getString("deskribapena"),
                                rs.getDouble("prezioa"),
                                rs.getInt("stock"),
                                rs.getString("kategoria"),
                                rs.getString("irudia")
                            );
                            produktuak.add(p);
                        }
                        esportatuJSON("produktuak.json", produktuak);
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
                            String sql = "SELECT * from produktuak";
                            Statement st = conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            System.out.println("--- Produktu Guztiak ---");
                            while(rs.next()){
                                System.out.println("ID: " + rs.getInt(1) + " | Izena: " + rs.getString(2));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Sartu kategoriaren izena: ");
                        kategoria = sc.nextLine();
                        try (Connection conn = DBKonexioa.getConnection()) {
                            String sql = "SELECT * from produktuak WHERE kategoria=?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, kategoria);
                            ResultSet rs = ps.executeQuery();
                            System.out.println("--- " + kategoria + " kategoriako produktuak ---");
                            while(rs.next()){
                                System.out.println("Izena: " + rs.getString("izena"));
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
                            String sql = "SELECT * FROM produktuak WHERE izena LIKE ?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, "%" + izena + "%");
                            ResultSet rs = ps.executeQuery();
                            while(rs.next()){
                                System.out.println("Aurkitua: " + rs.getString("izena") + " - " + rs.getDouble("prezioa") + "€");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Sartu produktuaren deskribapena: ");
                        deskribapena = sc.nextLine();
                        try (Connection conn = DBKonexioa.getConnection()) {
                            String sql = "SELECT * FROM produktuak WHERE deskribapena LIKE ?";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, "%" + deskribapena + "%");
                            ResultSet rs = ps.executeQuery();
                            while(rs.next()){
                                System.out.println("Aurkitua: " + rs.getString("izena") + " [" + rs.getString("deskribapena") + "]");
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