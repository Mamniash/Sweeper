package MySweeper;

public class Game{
    private final Bomb bomb;
    private final Flag flag;
    private final int BOMBS;
    private GameState gameState;

    public Game(int cols, int rows, int bombs) {
        Ranges.setSize(new Coord (cols, rows));
        bomb = new Bomb(bombs);
        BOMBS = bombs;
        flag = new Flag();
    }

    public void start() {
        bomb.start();
        flag.start();
        gameState = GameState.PLAYED;
        flag.setNowBombs(BOMBS);
        //new MyMidi().listen("welcome");
    }

    public MyBox getBox(Coord coord) {
        if(MyBox.OPENED == flag.get(coord))
            return bomb.get(coord);
        else
            return flag.get(coord);
    }

    public void pressLeftButton(Coord coord) {
        if (gameOver()) return;
        openBox(coord);
        checkWinner();
    }

    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case FLAGED: return;
            case OPENED: setOpenedtoCloseBoxArround(coord); return;
            case CLOSED:
                switch (bomb.get(coord)) {
                    case BOMB: openBombs(coord); return;
                    case ZERO: openBoxAround(coord); return;
                    default  : flag.setOpenedtoBox(coord);
                }
        }
    }

    private void openBombs(Coord bombed) {
        gameState = GameState.BOMBED;
        flag.setBombedTo(bombed);
        //new MyMidi().listen("boom");
        for (Coord coord : Ranges.getAllCoords()) {
            if (bomb.get(coord) == MyBox.BOMB)
                flag.setOpenedtoCloseBomb(coord);
            else
                flag.setNoBomb(coord);
        }
    }

    private void openBoxAround(Coord coord) {
        flag.setOpenedtoBox(coord);
        for(Coord around : Ranges.getAroundCoord(coord))
            openBox(around);
    }

    public void pressRightButton(Coord coord) {
        if (gameOver()) return;
        flag.toggleFlagedToBox(coord);
        checkWinner();
    }

    public boolean gameOver() {
        return gameState != GameState.PLAYED;
    }

    public void checkWinner() {
        if (gameState == GameState.PLAYED)
            if(flag.getCountOfClosed() == bomb.getTotalBombs())
                if(flag.getNowBombs() == 0) {
                    gameState = GameState.WINNER;
                    //new MyMidi().listen("win");
                }

    }

    public int getNowBombs() {
        return flag.getNowBombs();
    }

    void setOpenedtoCloseBoxArround(Coord coord) {
        if(bomb.get(coord) != MyBox.BOMB)
            if(flag.getCountOfCloseBoxArround(coord)
                >= bomb.get(coord).getNumber())
                for(Coord around : Ranges.getAroundCoord(coord))
                    if(flag.get(around) == MyBox.CLOSED)
                        openBox(around);
    }

    public void sleep(int sec) {
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setSound(boolean b) { //MyMidi.setSound(b);
    }
}
