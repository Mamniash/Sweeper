package MySweeper;

class Matrix {
    private MyBox [] [] matrix;

    Matrix(MyBox defaultBox) {
        matrix = new MyBox [Ranges.getSize().x] [Ranges.getSize().y];
        for(Coord coord : Ranges.getAllCoords()) {
            matrix [coord.x] [coord.y] = defaultBox;
        }
    }

    MyBox get (Coord coord) {
        if (Ranges.inRanges(coord))
            return matrix [coord. x] [coord.y];
        else
            return null;
    }

    void set (Coord coord, MyBox box) {
        if (Ranges.inRanges(coord))
            matrix [coord.x] [coord.y] = box;
    }
}
