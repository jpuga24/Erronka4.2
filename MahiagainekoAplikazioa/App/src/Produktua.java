public class Produktua {
    private int id;
    private String izena;
    private String deskribapena;
    private double prezioa;
    private int stock;
    private String kategoria;
    private String irudia;

    public Produktua(int id, String izena, String deskribapena, double prezioa, int stock, String kategoria, String irudia) {
        this.id = id;
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.prezioa = prezioa;
        this.stock = stock;
        this.kategoria = kategoria;
        this.irudia = irudia;
    }

    public int getId() { return id; }
    public String getIzena() { return izena; }
    public String getDeskribapena() { return deskribapena; }
    public double getPrezioa() { return prezioa; }
    public int getStocka() { return stock; }
    public String getKategoria() { return kategoria; }
    public String getIrudia() { return irudia; }
}
