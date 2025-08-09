import javax.swing.*;
import java.awt.*;

public class Main {
    private static JFrame frame;
    private static GameGrid gameGrid;
    private static LetterBlocks letterBlocks;
    private static JLabel statusLabel;
    private static Player[] players;
    private static int gridSize;
    private static JButton player1Button;
    private static JButton player2Button;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> setupGame());
    }

    private static void setupGame() {
        String input = JOptionPane.showInputDialog("Enter grid size (e.g., 4 for 4x4):");
        gridSize = Integer.parseInt(input);

        String player1Name = JOptionPane.showInputDialog("Enter Player 1 name:");
        String player2Name = JOptionPane.showInputDialog("Enter Player 2 name:");
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);
        players = new Player[]{player1, player2};

        frame = new JFrame("WordCrafters Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        statusLabel = new JLabel("Status: ");
        frame.add(statusLabel, BorderLayout.NORTH);

        char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        letterBlocks = new LetterBlocks(letters);
        frame.add(letterBlocks, BorderLayout.SOUTH);

        gameGrid = new GameGrid(gridSize, letterBlocks, players, statusLabel);
        frame.add(gameGrid, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(5, 1));
        player1Button = new JButton(players[0].getName() + ": " + players[0].getScore());
        player2Button = new JButton(players[1].getName() + ": " + players[1].getScore());

        controlPanel.add(player1Button);
        controlPanel.add(player2Button);

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGame());

        controlPanel.add(resetButton);
        controlPanel.add(newGameButton);

        frame.add(controlPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private static void resetGame() {
        frame.remove(gameGrid);
        players[0] = new Player(players[0].getName());
        players[1] = new Player(players[1].getName());

        updatePlayerButtons();

        gameGrid = new GameGrid(gridSize, letterBlocks, players, statusLabel);
        frame.add(gameGrid, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private static void newGame() {
        frame.dispose();
        setupGame();
    }

    public static void updatePlayerButtons() {
        SwingUtilities.invokeLater(() -> {
            player1Button.setText(players[0].getName() + ": " + players[0].getScore());
            player2Button.setText(players[1].getName() + ": " + players[1].getScore());
        });
    }
}
