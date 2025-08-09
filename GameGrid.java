import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class GameGrid extends JPanel {
    private char[][] grid;
    private int gridSize;
    private LetterBlocks letterBlocks;
    private Player[] players;
    private int currentPlayerIndex;
    private JLabel statusLabel;

    private Set<String> dictionary;
    private javax.swing.Timer turnTimer;
    private int turnTime = 15;

    public GameGrid(int size, LetterBlocks ltrBlocks, Player[] players, JLabel statusLabel) {
        this.gridSize = size;
        this.letterBlocks = ltrBlocks;
        this.players = players;
        this.currentPlayerIndex = 0;
        this.statusLabel = statusLabel;
        this.grid = new char[size][size];
        setLayout(new GridLayout(size, size));
        initGrid();
        loadDictionary("dictionary.txt");
        updateStatusLabel();
        startTurnTimer();
    }

    private void initGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = ' ';
                JButton cellButton = new JButton(" ");
                cellButton.setFont(new Font("Arial", Font.PLAIN, 24));
                cellButton.setPreferredSize(new Dimension(60, 60));
                cellButton.setEnabled(true);
                int r = i;
                int c = j;
                cellButton.addActionListener(e -> {
                    char selectedLetter = letterBlocks.getSelectedLtr();
                    if (selectedLetter != ' ' && grid[r][c] == ' ') {
                        placeLetter(r, c, selectedLetter);
                    }
                });
                add(cellButton);
            }
        }
    }

    private void loadDictionary(String fileName) {
        dictionary = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = br.readLine()) != null) {
                dictionary.add(word.toUpperCase());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading dictionary: " + e.getMessage());
        }
    }

    public boolean placeLetter(int r, int c, char letter) {
        if (grid[r][c] == ' ') {
            grid[r][c] = letter;
            ((JButton) getComponent(r * gridSize + c)).setText(String.valueOf(letter));
            String horizontalWord = getWord(r, c, true);
            String verticalWord = getWord(r, c, false);
            boolean validWord = false;
            int wordLength = 0;

            if (dictionary.contains(horizontalWord)) {
                wordLength = horizontalWord.length();
                validWord = true;
            }
            if (dictionary.contains(verticalWord)) {
                wordLength = Math.max(wordLength, verticalWord.length());
                validWord = true;
            }
            if (validWord) {
                players[currentPlayerIndex].addScore(wordLength);
                highlightWord(r, c, wordLength, horizontalWord.length() > verticalWord.length());
                Main.updatePlayerButtons();
            }
            updateStatusLabel();
            switchPlayer();
            checkGameEnd();
            return true;
        }
        return false;
    }

    private String getWord(int r, int c, boolean horizontal) {
        StringBuilder word = new StringBuilder();
        if (horizontal) {
            for (int i = c; i >= 0 && grid[r][i] != ' '; i--) {
                word.insert(0, grid[r][i]);
            }
            for (int i = c + 1; i < gridSize && grid[r][i] != ' '; i++) {
                word.append(grid[r][i]);
            }
        } else {
            for (int i = r; i >= 0 && grid[i][c] != ' '; i--) {
                word.insert(0, grid[i][c]);
            }
            for (int i = r + 1; i < gridSize && grid[i][c] != ' '; i++) {
                word.append(grid[i][c]);
            }
        }
        return word.toString();
    }

    private void highlightWord(int r, int c, int length, boolean horizontal) {
        if (horizontal) {
            for (int i = c; i >= 0 && grid[r][i] != ' '; i--) {
                ((JButton) getComponent(r * gridSize + i)).setBackground(Color.GREEN);
            }
            for (int i = c + 1; i < gridSize && grid[r][i] != ' '; i++) {
                ((JButton) getComponent(r * gridSize + i)).setBackground(Color.GREEN);
            }
        } else {
            for (int i = r; i >= 0 && grid[i][c] != ' '; i--) {
                ((JButton) getComponent(i * gridSize + c)).setBackground(Color.GREEN);
            }
            for (int i = r + 1; i < gridSize && grid[i][c] != ' '; i++) {
                ((JButton) getComponent(i * gridSize + c)).setBackground(Color.GREEN);
            }
        }
    }

    private void updateStatusLabel() {
        StringBuilder status = new StringBuilder();
        for (Player player : players) {
            status.append(player.getName()).append(": ").append(player.getScore()).append("  ");
        }
        status.append("Current Turn: ").append(players[currentPlayerIndex].getName());
        status.append(" - Time left: ").append(turnTime).append("s");
        statusLabel.setText(status.toString());
    }

    private void startTurnTimer() {
        if (turnTimer != null) {
            turnTimer.stop();
        }
        turnTimer = new javax.swing.Timer(1000, e -> {
            turnTime--;
            updateStatusLabel();
            if (turnTime <= 0) {
                switchPlayer();
            }
        });
        turnTime = 15;
        turnTimer.start();
    }

    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        updateStatusLabel();
        startTurnTimer();
    }

    private void checkGameEnd() {
        boolean isGridFull = true;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == ' ') {
                    isGridFull = false;
                    break;
                }
            }
        }
        if (isGridFull) {
            endGame();
        }
    }

    private void endGame() {
        turnTimer.stop();
        Player winner = Arrays.stream(players).max(Comparator.comparingInt(Player::getScore)).orElse(null);
        JOptionPane.showMessageDialog(this, "Congratulations " + winner.getName() + "! You won with a score of " + winner.getScore());
    }
}
