package MySweeper;

//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

public class MyMidi {
    static boolean sound = true;

    public void listen(String name) {
        if (sound) {
            try {
                String filename = "/wav/" + name + ".wav";

                //AudioPlayer.player.start
                        //(new AudioStream(getClass().getResourceAsStream
                                //(filename)));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setSound(boolean b) {
        sound = b;
    }
}
