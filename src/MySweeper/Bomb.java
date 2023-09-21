package MySweeper;

class Bomb {
    private Matrix bombMap;
    private final int BOMBS;

    Bomb(int totalBomb) {
        BOMBS =  totalBomb;
    }

    void start() {
        bombMap = new Matrix(MyBox.ZERO);
        for (int j =0; j < BOMBS; j++)
            placeBomb();
    }

    private void placeBomb() {
        while(true) {
            Coord coord = Ranges.getRandomCoord();
            if(MyBox.BOMB == bombMap.get(coord))
                continue;
            bombMap.set(coord, MyBox.BOMB);
            incNumbersAroundBomb(coord);
            break;
        }
    }

    private void incNumbersAroundBomb(Coord coord) {
        for(Coord around : Ranges.getAroundCoord(coord)) {
            if(MyBox.BOMB != bombMap.get(around))
                bombMap.set(around, bombMap.get(around).getNextNumberBox());
        }
    }

    MyBox get(Coord coord) {
        return bombMap.get(coord);
    }

    int getTotalBombs() {
        return BOMBS;
    }
}
