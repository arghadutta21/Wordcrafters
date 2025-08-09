public class Player {
    private String n;
    private int scr;
public Player(String n) {
        this.n = n;
        this.scr = 0;
    }
    public String getName() {
        return n;
    }
    public int getScore() {
        return scr;
    }
    public void addScore(int p) {
        scr = scr + p;
    }
}