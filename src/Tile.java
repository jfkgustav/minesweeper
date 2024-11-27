public class Tile {

    // State constants
    /* 0 - 8 = 0 - 8; */
    private final int BOMB = 9;
    private final int FLAGGED = 10;
    private final int QUESTION = 11;

    protected boolean pressed;
    protected int state;

    public Tile() {
        this.state = 0;
        pressed = false;
    }

    public void pressTile(){
        pressed = true;
    }

    public boolean isBomb(){
        return state == BOMB;
    }

    public void setBomb(){
        if(state != BOMB){
            state = BOMB;
        } else {
            throw new RuntimeException("The tile is already BOMB");
        }
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }

}
