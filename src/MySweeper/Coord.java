package MySweeper;

public class Coord {
    public int y;
    public int x;

    public Coord(int theX, int theY) {
        this.x = theX;
        this.y = theY;
    }

    @Override
    public boolean equals(Object o) {
       if (o instanceof Coord) {
           Coord to = (Coord) o;
           if (to.x == x && to.y ==y)
               return true;
       }
       return super.equals(o);
    }
}
