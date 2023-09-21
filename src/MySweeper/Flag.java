package MySweeper;

class Flag {
    private Matrix flagMap;
    private static int countOfClose;
    private int nowBombs;

    void start() {
        flagMap = new Matrix(MyBox.CLOSED);
        countOfClose = Ranges.getSize().x * Ranges.getSize().y;
    }

    MyBox get (Coord coord) {
        return flagMap.get(coord);
    }

    void setOpenedtoBox(Coord coord) {
        flagMap.set(coord, MyBox.OPENED);
        countOfClose--;
    }

    void setFlagedToBox(Coord coord) {
        if(nowBombs > 0) {
            nowBombs--;
            flagMap.set(coord, MyBox.FLAGED);
        }
    }

    void toggleFlagedToBox(Coord coord) {
        switch (flagMap.get(coord)) {
            case FLAGED: setCloseToBox(coord); break;
            case CLOSED: setFlagedToBox(coord); break;
        }

    }

    void setCloseToBox(Coord coord) {
        flagMap.set(coord, MyBox.CLOSED);
        nowBombs++;
    }

    int getCountOfClosed() {
        return countOfClose;
    }

    void setBombedTo(Coord coord) {
        flagMap.set(coord, MyBox.BOMBED);
    }

    void setOpenedtoCloseBomb(Coord coord) {
        if (flagMap.get(coord) == MyBox.CLOSED)
            flagMap.set(coord, MyBox.OPENED);
    }

    void setNoBomb(Coord coord) {
        if (flagMap.get(coord) == MyBox.FLAGED)
            flagMap.set(coord, MyBox.NOBOMB);
    }

    void setNowBombs(int bombs) {
        nowBombs = bombs;
    }

    int getNowBombs() {
        return nowBombs;
    }

    int getCountOfCloseBoxArround (Coord coord) {
        int count = 0;
        for (Coord around : Ranges.getAroundCoord(coord))
            if (flagMap.get(around) == MyBox.FLAGED)
                ++count;
            return count;
    }
}
