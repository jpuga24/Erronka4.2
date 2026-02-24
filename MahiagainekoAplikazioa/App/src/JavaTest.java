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

    // 2. TEST DE CONEXIÓN: ¿Funciona la base de datos?
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

    // 3. TEST DE JSON: ¿Se crea el archivo físicamente?
    @Test
    public void testCrearJson() {
        // Preparar datos mínimos
        List<Produktua> lista = new ArrayList<>();
        lista.add(new Produktua(1, "A", "B", 1.0, 1, "C", "D"));
        
        String nombreArchivo = "test_basico.json";
        
        // Ejecutar
        Main.esportatuJSON(nombreArchivo, lista);
        
        // Verificar
        File f = new File(nombreArchivo);
        Assert.assertTrue(f.exists());
        
        // Limpiar (borrar el archivo después de la prueba)
        f.delete();
    }
}
