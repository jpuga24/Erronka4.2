import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class MainTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Produktua objektua ondo sortzen dela egiaztatu")
    void testProduktuaCreation() {
        Produktua p = new Produktua(1, "Test", "Desc", 10.5, 100, "Cat", "img.png");
        
        assertAll("Produktuaren atributuak egiaztatu",
            () -> assertEquals(1, p.getId()),
            () -> assertEquals("Test", p.getIzena()),
            () -> assertEquals(10.5, p.getPrezioa()),
            () -> assertEquals(100, p.getStocka())
        );
    }

    @Test
    @DisplayName("JSON esportazioak fitxategia sortzen duela egiaztatu")
    void testEsportatuJSONCreatesFile() throws IOException {
        List<Produktua> produktuak = new ArrayList<>();
        produktuak.add(new Produktua(1, "Monitor", "24 inch", 150.0, 5, "Elektronika", "url"));

        Path fitxPath = tempDir.resolve("test_produktuak.json");
        String fitxIzena = fitxPath.toString();

        Main.esportatuJSON(fitxIzena, produktuak);

        File fitxategia = new File(fitxIzena);
        assertTrue(fitxategia.exists(), "JSON fitxategia sortu beharko litzateke");
        
        String edukia = Files.readString(fitxPath);
        assertTrue(edukia.contains("\"izena\": \"Monitor\""), "JSONak produktuaren datuak izan behar ditu");
        assertTrue(edukia.startsWith("[") && edukia.trim().endsWith("]"), "JSON formatua (array) zuzena izan behar da");
    }

    @Test
    @DisplayName("Konexioa ez dela nulua egiaztatu (DB martxan badago)")
    void testDBConnection() {
        try {
            assertNotNull(DBKonexioa.getConnection(), "Konexioak ez luke nuloa izan behar");
        } catch (Exception e) {
            fail("Ezin izan da DB-ra konektatu: " + e.getMessage());
        }
    }
}