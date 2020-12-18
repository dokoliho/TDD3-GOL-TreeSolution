import java.util.ArrayList;

public class Life implements ILife {


  public static void main(String[] args) {
    Life l = new Life(new String[] {  "     ",
                                      "     ",
                                      " *** ",
                                      "     ",
                                      "     " });
    l = (Life) l.nextGeneration();
  }


  // Die lebenden Zellen werden hier nicht in einer Matrix, sondern in einem Baum gespeichert.
  private ArrayList<String> livingCells = new ArrayList<>();
  private int maxWidth = 0;

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
    setChar(x, y, '*');
  }

  private void setChar(int x, int y, char c) {
    while (y>= livingCells.size())
      livingCells.add("");
    String line = livingCells.get(y);
    while (x>=line.length())
      line = line + " ";
    livingCells.set(y, replaceChar(line, c, x));
    if (x >= maxWidth)
      maxWidth = x+1;
  }

  private String replaceChar(String str, char ch, int position) {
    String temp = str.substring(0, position) + ch + str.substring(position+1);
    return temp;
  }


  // Sterbende Zelle -> Entfernen aus dem Baum
  @Override
  public void setDead(int x, int y) {
    setChar(x, y, ' ');
  }

  // Prüfen ob lebend -> In Baum enthalten?
  @Override
  public boolean isAlive(int x, int y) {
    if (y<0 || y >= livingCells.size()) return false;
    String line = livingCells.get(y);
    if (x<0 || x >= line.length()) return false;
    return line.charAt(x) == '*';
  }

  // Für eine neue Generation wird ein neuer Baum generiert
  // Lebende Zellen werden nur eingetragen, wenn die entsprechenden Bedingungen
  // erfüllt sind
  @Override
  public ILife nextGeneration() {
    Life result = new Life();
    for (int y=0; y <= livingCells.size(); y++) {
      for (int x = 0; x <= maxWidth; x++) {
        int neighborCount = countLivingNeighbors(x, y);
        if (neighborCount == 3)
          result.setAlive(x, y);
        if (isAlive(x, y) && neighborCount == 2)
          result.setAlive(x, y);
      }
    }
    return result;
  }

  // Anzahl der lebenden Nachbarn
  private int countLivingNeighbors(int ox, int oy) {
    int result = 0;
    for (int x = ox-1; x <= ox+1; x++)
      for (int y = oy-1; y <= oy+1; y++) {
        if (x == ox && y == oy) continue;
        if (isAlive(x, y))
          result++;
      }
    return result;
  }

}