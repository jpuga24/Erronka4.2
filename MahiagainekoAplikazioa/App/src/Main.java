import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class Main{

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
            e.printStackTrace();
        }
    }
    public static void main(String[] args){

        String izena;
        String deskribapena;
        double prezioa;
        int stock;
        String kategoria;
        String irudia;

        Scanner sc=new Scanner(System.in);
        int aukera= -1;

        while(aukera != 0){
        System.out.println("Opzioak:");
        System.out.println("1- Produktuak berriak gehitu");
        System.out.println("2- Produktuak CSV fitxategitik kargatzea");
        System.out.println("3- Dauden produktuak eguneratzea");
        System.out.println("4- Produktuak ezabatzea");
        System.out.println("5- Informazioa esportatzea");
        System.out.println("6- Produktuak zerrendatzea");
        System.out.println("7- Produktuak bilatzea");
        System.out.println("Aukeratu:");
        aukera=sc.nextInt();
        sc.nextLine();

        switch (aukera) {
            case 1:
                System.out.println("Produktua sartu");

                System.out.println("Izena: ");
                izena=sc.nextLine();
                sc.next();
                
                System.out.println("Produktuaren deskribapena: ");
                deskribapena=sc.nextLine();
                sc.next();

                System.out.println("Prezioa: ");
                prezioa=sc.nextDouble();

                System.out.println("Stock erabilgarria: ");
                stock=sc.nextInt();

                System.out.println("Kategoria: ");
                kategoria=sc.nextLine();
                sc.next();

                System.out.println("Produktuaren irudiak: ");
                irudia=sc.nextLine();
                sc.next();

                try (Connection conn = DBKonexioa.getConnection()) {

                    String sql = "INSERT INTO produktuak(izena, deskribapena, prezioa, stock, kategoria, irudia) VALUES (?, ?, ?, ?, ?, ?)";

                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, izena);
                    ps.setString(2, deskribapena);
                    ps.setDouble(3, prezioa);
                    ps.setInt(4, stock);
                    ps.setString(5, kategoria);
                    ps.setString(6, irudia);

                    ps.executeUpdate();
                }catch(SQLException e){
                    e.printStackTrace();
                }

                break;

            case 2:
                System.out.println("Sartu CSV fitxategiaren bidea:");
                String ruta = sc.nextLine();

                try(Connection conn = DBKonexioa.getConnection();
                    BufferedReader br = new BufferedReader(new FileReader(ruta))){

                    String linea;
                    br.readLine();

                    String sql = "INSERT INTO produktuak(izena, deskribapena, prezioa, stock, kategoria, irudia) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);

                    while((linea = br.readLine()) != null){
                        String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                        izena = datos[1].replace("\"","");
                        deskribapena = datos[2].replace("\"","");
                        prezioa = Double.parseDouble(datos[3]);
                        stock = Integer.parseInt(datos[4]);
                        kategoria = datos[6].replace("\"","");
                        irudia = datos[7].replace("\"","");

                        ps.setString(1, izena);
                        ps.setString(2, deskribapena);
                        ps.setDouble(3, prezioa);
                        ps.setInt(4, stock);
                        ps.setString(5, kategoria);
                        ps.setString(6, irudia);

                        ps.executeUpdate();
                    }

                    System.out.println("CSV kargatuta.");

                }catch(Exception e){
                    e.printStackTrace();
                }
                break;

            case 3:
                System.out.println("Produktuak eguneratzea");
                System.out.println("Produktuaren izena: ");
                izena=sc.nextLine();
                sc.nextLine();

                System.out.println("Prezio berria: ");
                int prezioBerria=sc.nextInt();
                sc.nextLine();

                System.out.println("Stock berria: ");
                int stockBerria=sc.nextInt();
                sc.nextLine();

                System.out.println("Irudi berria: ");
                String irudiBerria=sc.nextLine();
                sc.nextLine();

                try(Connection conn=DBKonexioa.getConnection()){
                    String sql="UPDATE produktuak SET prezioa=?, stock=?, irudia=? where izena=?";
                    PreparedStatement ps=conn.prepareStatement(sql);

                    ps.setDouble(1, prezioBerria);
                    ps.setInt(2,stockBerria);
                    ps.setString(3,irudiBerria);
                    ps.setString(4,izena);

                    ps.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }

                break;

            case 4:
                System.out.println("Produktuak ezabatzea");
                System.out.println("Produktuaren izena: ");
                izena=sc.nextLine();
                sc.nextLine();

                try(Connection conn=DBKonexioa.getConnection()){
                    String sql="DELETE FROM produktuak WHERE izena=?";
                    PreparedStatement ps=conn.prepareStatement(sql);
                    ps.setString(1,izena);

                    int tuplak=ps.executeUpdate();
                    System.out.println("Ezabatutako produktuak: "+tuplak);
                }catch(SQLException e){
                    e.printStackTrace();
                }

                break;

            case 5:
                System.out.println("Informazioa esportatzea");

                List<Produktua> produktuak = new ArrayList<>();

                try(Connection conn = DBKonexioa.getConnection()) {
                        String sql = "SELECT * FROM produktuak";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while(rs.next()) {
                            Produktua p = new Produktua(
                            rs.getInt("Id_produktua"),
                            rs.getString("izena"),
                            rs.getString("deskribapena"),
                            rs.getDouble("prezioa"),
                            rs.getInt("stocka"),
                            rs.getString("kategoria"),
                            rs.getString("irudia")
                            );
                        produktuak.add(p);
                        }

                    esportatuJSON("produktuak.json", produktuak);

                    } catch(SQLException e) {
                        e.printStackTrace();
                    }
                break;
            
            case 6:
                System.out.println("Produktuak zerrendatzea");
                System.out.println("Bi moduak daude, aukeratu hauetatik bat:");
                System.out.println("1- Produktu guztien zerrenda");
                System.out.println("2- Kategorien araberako zerrenda");
                int seiAukera=sc.nextInt();

                if(seiAukera==1){
                    try(Connection conn=DBKonexioa.getConnection()){
                        String sql="SELECT * from produktuak";
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Sartu kategoriaren izena: ");
                    kategoria=sc.nextLine();
                    sc.nextLine();

                    try(Connection conn=DBKonexioa.getConnection()){
                        String sql="SELECT * from produktuak WHERE kategoria=?";
                        PreparedStatement ps=conn.prepareStatement(sql);
                        ps.setString(1, kategoria);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }
                break;

            case 7:
                System.out.println("Produktuak bilatzea");
                System.out.println("Nola bilatu nahi duzu?");
                System.out.println("1- Produktuaren izena");
                System.out.println("2- Produktuaren deskribapena");
                int zazpiAukera=sc.nextInt();

                if(zazpiAukera==1){
                    System.out.println("Sartu produktuaren izena: ");
                    izena=sc.nextLine();
                    sc.nextLine();

                    try(Connection conn=DBKonexioa.getConnection()){
                        String sql="SELECT * FROM produktuak WHERE izena=?";
                        PreparedStatement ps=conn.prepareStatement(sql);
                        ps.setString(1, izena);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Sartu produktuaren deskribapena: ");
                    deskribapena=sc.nextLine();
                    sc.nextLine();

                    try(Connection conn=DBKonexioa.getConnection()){
                        String sql="SELECT * FROM produktuak WHERE deskribapena=?";
                        PreparedStatement ps=conn.prepareStatement(sql);
                        ps.setString(1, deskribapena);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }

                break;

            default:
                System.out.println("Aukeratu opzio bat");
                break;
        }
    }
    sc.close();
    }
}