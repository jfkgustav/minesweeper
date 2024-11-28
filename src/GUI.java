import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GUI {

    private JFrame window;
    private Board board;
    private final int WINDOW_HEIGHT = 1200;
    private final int WINDOW_WIDTH = 1200;
    private GameSettings settings;
    private JButton[] tiles;
    private int rows;
    private int columns;
    private int boardSize;
    private Set<Integer> pressed;
    private final Map<String, Color> colorMap;
    private int numberOfClicks;



    public GUI(){
        window = new JFrame("MINESWEEPER");
        colorMap = new HashMap<String, Color>();
        fillColorMap();
    }


    private void startGame(Board board){
        window.setVisible(false);
        window.getContentPane().removeAll();
        window.revalidate();
        window.repaint();
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        pressed = new HashSet<>();
        numberOfClicks = 0;

        this.board = board;
        boardSize = board.getNumberOfTiles();
        rows = board.getNumberOfRows();
        columns = boardSize / rows;
        tiles = new JButton[board.getNumberOfTiles()];
        window.setLayout(new GridLayout(rows, columns));

        for(int i = 0; i < boardSize; i++) {
            tiles[i] = new JButton("");
            JButton b = tiles[i];
            b.setFont(new Font("Arial", Font.PLAIN, 20));
            if(settings.mines == 99) b.setFont(new Font("Arial", Font.PLAIN, 9));
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
                    JOptionPane.showMessageDialog(window, "GAME OVER");
                    showSettings();
                }
                numberOfClicks++;
                pressed.add(index);
                if(board.isDone()){
                    JOptionPane.showMessageDialog(window, "GAME WON");
                    for(JButton j : tiles){
                        j.setText(state);
                        b.setBackground(Color.lightGray);
                        b.setForeground(colorMap.get(state));
                    }
                    showSettings();
                }
            });

            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (!pressed.contains(index) && e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        b.setFont(new Font("Arial", Font.PLAIN, 20));
                        if(settings.mines == 99) b.setFont(new Font("Arial", Font.PLAIN, 9));
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

    public void showSettings() {
        window.getContentPane().removeAll();
        window.revalidate();
        window.repaint();
        // Skapa komponenter
        JTextArea textArea = new JTextArea("Svårighetsgrad?");
        JButton easy = new JButton("Lätt (9x9, 10 minor)");
        JButton medium = new JButton("Medel (16x16, 40 minor)");
        JButton hard = new JButton("Svårt (16x30, 99 minor)");
        JTextArea soonTM = new JTextArea("Kommer snart! Välj själv! :D");

        window.setLayout(new FlowLayout());
        // Skapa en ActionListener för knapparna
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kollar vilken knapp som trycktes
                if (e.getSource() == easy) {
                    settings = new GameSettings(9, 9, 10);
                    Board board = new Board(settings);
                    startGame(board);
                } else if (e.getSource() == medium) {
                    settings = new GameSettings(16, 16, 40);
                    Board board = new Board(settings);
                    startGame(board);
                } else if (e.getSource() == hard) {
                    settings = new GameSettings(16, 30, 99);
                    Board board = new Board(settings);
                    startGame(board);
                }
            }
        };

        // Lägg till lyssnaren till knapparna
        easy.addActionListener(buttonListener);
        medium.addActionListener(buttonListener);
        hard.addActionListener(buttonListener);

        // Lägg till komponenterna till fönstret
        window.add(textArea);
        window.add(easy);
        window.add(medium);
        window.add(hard);
        window.add(soonTM);


        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        // Returnera en default GameSettings (kan vara anpassad beroende på behov)
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
