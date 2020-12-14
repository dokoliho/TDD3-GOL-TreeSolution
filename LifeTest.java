import org.junit.Test;
import static org.junit.Assert.*;

public class LifeTest {
    
    @Test
    public void createNewCell() {
        
        // Arrange: drei lebende Zellen
        Life l = new Life();
        l.setAlive(0, 0);
        l.setAlive(0, 1);
        l.setAlive(0, 2);

        // Act: Berechnung der Folgegeneration
        ILife nextGen = l.nextGeneration();

        // Assert: Rasterpunkt mit drei Nachbarn sollte jetzt leben
        assertTrue(nextGen.isAlive(1, 1));
    }

    @Test
    public void destroyLonelyCell() {
        // Arrange: eine lebende Zelle
        Life l = new Life();
        l.setAlive(2, 2);

        // Act: Berechnung der Folgegeneration
        ILife nextGen = l.nextGeneration();

        // Assert: Rasterpunkt mit drei Nachbarn sollte jetzt leben
        assertFalse(nextGen.isAlive(2, 2));
    }

    @Test
    public void keepAliveCell() {

        // Arrange: zwei oder drei lebende Zellen
        Life l = new Life();
        l.setAlive(2, 2);
        l.setAlive(2, 3);
        l.setAlive(2, 1);

        // Act: Berechnung der Folgegeneration
        ILife nextGen = l.nextGeneration();

        // Assert: Rasterpunkt mit drei Nachbarn sollte jetzt leben
        assertTrue(nextGen.isAlive(2, 2));
    }

    @Test
    public void destroyCrowdedCell() {
        // Arrange: mehr als drei lebende Zellen
        Life l = new Life();
        l.setAlive(2, 2);
        l.setAlive(2, 3);
        l.setAlive(2, 1);
        l.setAlive(1, 2);
        l.setAlive(3, 2);

        // Act: Berechnung der Folgegeneration
        ILife nextGen = l.nextGeneration();

        // Assert: Rasterpunkt mit drei Nachbarn sollte jetzt leben
        assertFalse(nextGen.isAlive(2, 2));
    }

}
