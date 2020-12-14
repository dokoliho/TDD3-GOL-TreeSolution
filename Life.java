import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Life implements ILife {

  // Eigene Klasse, die eine Zellposition repräsentiert.
  // Zellpositionen, die anschließend in der Menge der lebenden
  // Zellen gespeichert sind, geben den Zustand des Spiels wider.
  private class Cell implements Comparable<Cell> {

    private int x, y;

    public Cell(int x, int y) {
      this.x = x;
      this.y = y;
    }

    // Um eine effiziente Suche in größeren Zellenhaufen zu ermöglichen,
    // sollte eine Ordnung/Sortierung für Zellen existieren
    // Hier ist es einfach die Position auf dem Spielfeld, die die Ordnung bestimmt

    @Override
    public int compareTo(Cell o) {
      if (x < o.x) return -1;
      if (x > o.x) return 1;
      if (y < o.y) return -1;
      if (y > o.y) return 1;
      return 0;
    }

    // Die Gleichheit bei der Sortierung muss auch in der equals-Methode nachgezogen
    // werden. Standard ist hier, dass Objekte nur gleich sind, wenn sie identisch sind.

    @Override
    public boolean equals(Object obj) {
      if ( obj == null ) return false;
      if ( obj == this ) return true;
      if ( !(obj instanceof Cell) ) return false;
      Cell that = (Cell) obj;
      return  this.x == that.x
              && this.y == that.y;
    }

    // Gleiche Objekte müssen den gleichen Hashcode liefern

    @Override
    public int hashCode()
    {
      return (x*100) + y;
    }

  }


  public static void main(String[] args) {
    Life l = new Life(new String[] {  "     ",
                                      "     ",
                                      " *** ",
                                      "     ",
                                      "     " });
    l = (Life) l.nextGeneration();
  }


  // Die lebenden Zellen werden hier nicht in einer Matrix, sondern in einem Baum gespeichert.
  private TreeSet<Cell> livingCells = new TreeSet<>();

  public Life() {
    nukeAll();
  }

  public Life(String[] setup) {
    this();
    for (int y = 0; y < setup.length; y++)
      for (int x = 0; x < setup[y].length(); x++)
        if (setup[y].charAt(x) != ' ')
          setAlive(x, y);
  }

  // Alle tot -> leerer Baum
  @Override
  public void nukeAll() {
    livingCells.clear();
  }

  // Lebende Zelle -> Aufnahme in den Baum. Wegen der überschriebenen Gleichheit
  // ist sichergestellt, dass eine Zellposition nicht doppelt auftaucht
  @Override
  public void setAlive(int x, int y) {
    Cell c = new Cell(x, y);
    livingCells.add(c);
  }

  // Sterbende Zelle -> Entfernen aus dem Baum
  @Override
  public void setDead(int x, int y) {
    Cell c = new Cell(x, y);
    livingCells.remove(c);
  }

  // Prüfen ob lebend -> In Baum enthalten?
  @Override
  public boolean isAlive(int x, int y) {
    Cell c = new Cell(x, y);
    return livingCells.contains(c);
  }

  // Für eine neue Generation wird ein neuer Baum generiert
  // Lebende Zellen werden nur eingetragen, wenn die entsprechenden Bedingungen
  // erfüllt sind
  @Override
  public ILife nextGeneration() {
    Life result = new Life();
    for (Cell c : livingCells) {
      List<Cell> community = this.community(c);
      for (Cell d : community) {
        int neighborCount = countLivingNeighbors(d);
        if (neighborCount == 3)
          result.setAlive(d.x, d.y);
        if (isAlive(d.x, d.y) && neighborCount == 2)
          result.setAlive(d.x, d.y);
      }
    }
    return result;
  }

  // Community -> Alle Nachbarn und die Zelle selbst
  private List<Cell> community(Cell c) {
    ArrayList<Cell> result = new ArrayList<>();
    for (int x = c.x-1; x <= c.x+1; x++)
      for (int y = c.y-1; y <= c.y+1; y++)
        result.add(new Cell(x, y));
    return result;
  }

  // Nachbarn -> Community ohne die Zelle selbst
  private List<Cell> neighbors(Cell c) {
    List<Cell> result = community(c);
    result.remove(c);
    return result;
  }

  // Anzahl der lebenden Nachbarn
  private int countLivingNeighbors(Cell c) {
    int result = 0;
    for (Cell n : neighbors(c))
      if (isAlive(n.x, n.y))
        result++;
    return result;
  }

}