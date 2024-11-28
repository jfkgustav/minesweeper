import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board {

    private final int numberOfTiles;
    private final Tile[] tiles;
    private final int numberOfBombs;
    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private int numberOfVisible;
    private final Set<Integer> visibleTiles;


    public Board(GameSettings options){
        BOARD_WIDTH = options.cols;
        BOARD_HEIGHT = options.rows;
        visibleTiles = new HashSet<>();
        numberOfTiles = BOARD_WIDTH * BOARD_HEIGHT;
        tiles = new Tile[numberOfTiles];
        numberOfBombs = options.mines;
        for(int i = 0; i < numberOfTiles; i++){
               tiles[i] = new Tile();
        }
        numberOfVisible = 0;
        fillBoard();
    }

    private void fillBoard(){

        Random rand = new Random();

        int bombsLeft = numberOfBombs;

        while (bombsLeft > 0){
            int bombLocation = rand.nextInt(numberOfTiles);
            if(!tiles[bombLocation].isBomb()){
                try {
                    tiles[bombLocation].setBomb();
                } catch (RuntimeException e){
                    System.err.println(e.getMessage());
                }
                bombsLeft--;
            }
        }

        placeTheOtherTiles();
    }

    private void placeTheOtherTiles(){
        for(int i = 0; i < numberOfTiles; i++){
            if(!tiles[i].isBomb()) {
                calculateNumberOfAdjacentBombs(i);
            }
        }
    }

    private void calculateNumberOfAdjacentBombs(int index){
        int numberOfAdjacentBombs = 0;
        int left = index % BOARD_WIDTH == 0 ? 0 : -1;
        int right = index % BOARD_WIDTH == BOARD_WIDTH - 1 ? 0 : 1;



        for (int row = index - BOARD_WIDTH; row <= index + BOARD_WIDTH; row += BOARD_WIDTH) {
            for (int col = left; col <= right; col++) {
                int tilePosition = row + col;
                if (isOnMap(tilePosition) && index != tilePosition) {
                    if (tiles[tilePosition].isBomb()) numberOfAdjacentBombs++;
                }
            }
        }
        tiles[index].setState(numberOfAdjacentBombs);
    }

    public boolean isOnMap(int pos){
        return pos >= 0 && pos < numberOfTiles;
    }


    public void printBoard(){
        for(int i = 0; i < numberOfTiles; i++){
            if(i % BOARD_WIDTH == 0){
                System.out.print("|");
            }
            if(tiles[i].isBomb()){
                System.out.print(" X ");
                continue;
            }
            System.out.print(" " +tiles[i].state+ " ");
            if(i % BOARD_WIDTH == BOARD_WIDTH - 1){
                System.out.println("|");
            }
        }
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public String getStateAt(int index){
        if (tiles[index].isBomb()) return "X";
        if (tiles[index].getState() == 0) return "";
        return String.valueOf(tiles[index].getState());
    }

    public int getNumberOfRows(){
       return BOARD_HEIGHT;
    }

    public void setVisible(int index){
        tiles[index].pressed = true;
        visibleTiles.add(index);
        numberOfVisible = visibleTiles.size();
    }

    public int getNumberOfVisible(){
        return numberOfVisible;
    }

    public int getNumberOfSafeTiles(){
        return numberOfTiles - numberOfBombs;
    }

    public boolean isDone(){
        return getNumberOfSafeTiles() - numberOfVisible == 0;
    }

}
