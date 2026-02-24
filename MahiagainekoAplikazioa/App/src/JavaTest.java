import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class JavaTest {
    @Test
    public void testProducto() {
        Produktua p = new Produktua(1, "Test", "Desc", 10.0, 5, "Cat", "img.png");
        Assert.assertEquals("Test", p.getIzena());
        Assert.assertEquals(10.0, p.getPrezioa(), 0.01);
    }

    @Test
    public void testConexion() {
        try {
            Connection conn = DBKonexioa.getConnection();
            Assert.assertNotNull(conn);
            conn.close();
        } catch (Exception e) {
            Assert.fail("No hay conexión: " + e.getMessage());
        }
    }

    @Test
    public void testCrearJson() {
        List<Produktua> lista = new ArrayList<>();
        lista.add(new Produktua(1, "A", "B", 1.0, 1, "C", "D"));
        
        String nombreArchivo = "test_basico.json";
        
        // Ejecutar
        Main.esportatuJSON(nombreArchivo, lista);
        
        // Verificar
        File f = new File(nombreArchivo);
        Assert.assertTrue(f.exists());
        
        f.delete();
    }
}
