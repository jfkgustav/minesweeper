import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GUI {

    private JFrame window;
    private Board board;

    private final int WINDOW_HEIGHT = 1360;
    private final int WINDOW_WIDTH = 1360;
    private final JButton[] tiles;
    private final int rows;
    private final int columns;
    private final int boardSize;
    private Set<Integer> pressed;

    private final Map<String, Color> colorMap;

    private int numberOfClicks;



    public GUI(Board board){
        pressed = new HashSet<>();
        this.board = board;
        tiles = new JButton[board.getNumberOfTiles()];
        window = new JFrame("MINESWEEPER");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        boardSize = board.getNumberOfTiles();
        rows = board.getNumberOfRows();
        columns = boardSize / rows;
        colorMap = new HashMap<String, Color>();
        fillColorMap();
        numberOfClicks = 0;

        // Använd GridLayout för enkel layout
        window.setLayout(new GridLayout(rows, columns));


        for(int i = 0; i < boardSize; i++) {
            tiles[i] = new JButton("");
            JButton b = tiles[i];
            b.setFont(new Font("Arial", Font.PLAIN, 40));
            b.setBackground(new Color(255,255,255));
            window.add(tiles[i]);
            final int index = i; // Behövs för att använda `i` i lambda-uttrycket
            b.addActionListener(e -> {
                board.setVisible(index);
                String state = board.getStateAt(index);
                if(state.equals("")) checkNeighbours(index);
                b.setText(state);
                b.setBackground(Color.lightGray);
                b.setForeground(colorMap.get(state));
                if(state.equals("X") && numberOfClicks != 0){
                    JOptionPane.showConfirmDialog(window, "GAME OVER");
                    System.exit(0);
                }
                numberOfClicks++;
                pressed.add(index);
                if(board.isDone()){
                    JOptionPane.showConfirmDialog(window, "GAME WON");
                    for(JButton j : tiles){
                        j.setText(state);
                        b.setBackground(Color.lightGray);
                        b.setForeground(colorMap.get(state));
                    }
                    System.exit(0);
                }
            });

            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (!pressed.contains(index) && e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        b.setFont(new Font("Arial", Font.PLAIN, 32));
                        if(!b.getText().equals("sus")){
                            b.setText("sus");
                            b.setBackground(Color.red);
                        } else {
                            b.setText("");
                            b.setBackground(Color.WHITE);
                        }
                    }
                }
            });
        }


    }


    public void startGame(){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }


    private void checkNeighbours(int index){

        if(pressed.contains(index)){
            return;
        }

        pressed.add(index);

        int left = index % columns == 0 ? 0 : -1;
        int right = index % columns == columns - 1 ? 0 : 1;

        for (int row = index - columns; row <= index + columns; row += columns) {
            for (int col = left; col <= right; col++) {
                int tilePosition = row + col;
                if (board.isOnMap(tilePosition) && index != tilePosition) {
                    JButton b = tiles[tilePosition];
                    String state = board.getStateAt(tilePosition);
                    if (state.equals("")) {
                        checkNeighbours(tilePosition);
                        b.setText(state);
                        board.setVisible(tilePosition);
                        b.setBackground(Color.lightGray);
                        ButtonModel model = b.getModel();
                        model.setPressed(true);
                    } else {
                        board.setVisible(tilePosition);
                        if(!board.getStateAt(tilePosition).equals("X")) {
                            b.setBackground(Color.lightGray);
                            b.setForeground(colorMap.get(state));
                            b.setText(state);
                        }
                    }
                }
            }
        }
    }

    private void fillColorMap(){
        colorMap.put("1", Color.BLUE);
        colorMap.put("2", Color.GREEN);
        colorMap.put("3", Color.RED);
        colorMap.put("4", Color.YELLOW);
        colorMap.put("5", Color.ORANGE);
        colorMap.put("6", Color.PINK);
        colorMap.put("7", Color.MAGENTA);
        colorMap.put("8", Color.DARK_GRAY);
        colorMap.put("",  Color.LIGHT_GRAY);
        colorMap.put("X", Color.BLACK);
    }

}
