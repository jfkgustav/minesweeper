public class Main {

    public static void main(String[] args) {

        Board board = new Board(16, 16, 40);
        //board.printBoard();
        GUI window = new GUI(board);
        window.startGame();

    }

}