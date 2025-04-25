import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Random;
import java.io.*;
import java.nio.file.*;

public class GamePanel extends JPanel {

    private GameListener listener;
    private int mineCount,rows,cols;
    private double timePassed;
    private double score;
    public GamePanel(Box[][] board, int mineCount, GameListener listener) {
        this.timePassed = 0.0;
        this.score = 0.0;
        this.mineCount = mineCount;
        this.listener = listener;
        this.rows = board.length;
        this.cols = board[0].length;
        setLayout(new GridLayout(rows, cols));
        start(board);
    }

    private void start(Box[][] board) {
        long startTime = System.currentTimeMillis();
        int cols = board[0].length;

        for (Box[] boxes : board) {
            for (int y = 0; y < cols; y++) {
                Box box = boxes[y];
                box.setActionCommand(calculateTempMines(board, box));
                box.setText("");
                add(box);

                final Box currentBox = box;

                currentBox.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {

                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (currentBox.isOpen) return;

                            currentBox.isFlagged = !currentBox.isFlagged;
                            currentBox.setText(currentBox.isFlagged ? "🚩" : "");
                            return;
                        }

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (currentBox.isOpen) {
                                int flagCount = getFlagCount(currentBox, board);
                                if (currentBox.getText().equals(String.valueOf(flagCount))) {
                                    openUnflaggedCells(board, currentBox);
                                }
                            }else {
                                if (currentBox.isFlagged) return;

                                currentBox.isOpen = true;
                                currentBox.setEnabled(false);

                                if (currentBox.isMined) {
                                    currentBox.setText("💣");
                                    endGame(board, 0);
                                } else {
                                    String value = currentBox.getActionCommand();
                                    currentBox.setText(value);

                                    if ("0".equals(value)) {
                                        cleanNeighborBoxes(board, currentBox);
                                    }
                                }
                            }
                            timePassed = System.currentTimeMillis() - startTime;
                            checkIsItDone(board);
                        }
                    }
                });

            }
        }
    }

    private void checkIsItDone(Box[][] board) {
        int openCellCount = 0;
        for (Box[] boxes : board) {
            for (Box box : boxes) {
                if (box.isOpen){
                    openCellCount++;
                }
            }
        }
        if (openCellCount == board.length * board[0].length - mineCount) {
            System.out.println(timePassed/1000);
            updateScore();
            endGame(board, 1);
        }
    }

    private void updateScore() {
        score = (mineCount * 1000.0) / (timePassed * (1 + 0.05 * (rows + cols)));
        score *= 1000;

        try {
            Double oldScore = 0.0;
            String fileName = "scores.txt";

            if (Files.exists(Paths.get(fileName))) {
                String content = new String(Files.readAllBytes(Paths.get(fileName))).trim();
                if (!content.isEmpty()) {
                    oldScore = Double.parseDouble(content);
                }
            }

            if (score > oldScore) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    writer.write(String.valueOf(score));
                }
                System.out.println("Yeni skor yazıldı: " + score);
            } else {
                System.out.println("Mevcut skor daha yüksek veya eşit: " + oldScore);
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Hata oluştu: " + e.getMessage());
        }
    }

    private void endGame(Box[][] board, int message) {
        int option;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedScore = df.format(score);
        if (message == 1) {
            option = JOptionPane.showOptionDialog(this,
                    "You Won, Congratulations!\n " +
                            "Your Score: " + formattedScore,
                    "You Won!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Restart", "Return to Menu"},
                    "Restart");
        } else {
            for (Box[] boxes : board) {
                for (Box box : boxes) {
                    box.setEnabled(false);
                    if (box.isMined && !box.isFlagged) {
                        box.setText("💣");
                    } else if (!box.isMined && box.isFlagged) {
                        box.setText("❌");
                    } else {
                        box.setText(box.getActionCommand());
                    }
                }
            }

            option = JOptionPane.showOptionDialog(this,
                    "Game Over! You hit a mine!",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Restart", "Return to Menu"},
                    "Restart");
        }

        if (option == 0) {
            listener.onRestart(rows, cols, mineCount);
        } else if (option == 1) {
            listener.onReturnToMenu();
        }
    }

    private void restartGame(Box[][] board) {
        // Reset the board
        for (Box[] boxes : board) {
            for (Box box : boxes) {
                box.isOpen = false;
                box.isFlagged = false;
                box.isMined = false;
                box.setText("");
                box.setEnabled(true); // Enable all boxes again
            }
        }
        initializeBoard(board);
    }

    private void initializeBoard(Box[][] board) {
        int rows = board.length;
        int cols = board[0].length;
        int mineNumber = mineCount;

        while (mineNumber != 0) {
            Random rand = new Random();
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);
            if (!board[row][col].isMined) {
                board[row][col].isMined = true;
                mineNumber--;
            }
        }

        for (Box[] boxes : board) {
            for (Box box : boxes) {
                box.setActionCommand(calculateTempMines(board, box));
            }
        }
    }

    private void openUnflaggedCells(Box[][] board, Box currentBox) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = currentBox.x + dx;
                int newY = currentBox.y + dy;

                if (newX >= 0 && newX < board.length && newY >= 0 && newY < board[0].length) {
                    if (!board[newX][newY].isFlagged && !board[newX][newY].isOpen) {
                        if(board[newX][newY].isMined){
                            endGame(board, 0);
                        }
                        board[newX][newY].isOpen = true;
                        board[newX][newY].setText(board[newX][newY].getActionCommand());
                        board[newX][newY].setEnabled(false);
                        cleanNeighborBoxes(board, board[newX][newY]);
                    }
                }
            }
        }
    }

    private int getFlagCount(Box currentBox, Box[][] board) {
        int flagCount = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = currentBox.x + dx;
                int newY = currentBox.y + dy;

                if (newX >= 0 && newX < board.length && newY >= 0 && newY < board[0].length) {
                    if (board[newX][newY].isFlagged) {
                        flagCount++;
                    }
                }
            }
        }
        return flagCount;
    }

    private void cleanNeighborBoxes(Box[][] board, Box box) {
        if (!"0".equals(box.getActionCommand())) {
            return;
        }
        int rows = board.length;
        int cols = board[0].length;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = box.x + dx;
                int newY = box.y + dy;

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                    Box neighbor = board[newX][newY];

                    if (!neighbor.isOpen && !neighbor.isFlagged && !neighbor.isMined) {
                        neighbor.isOpen = true;
                        neighbor.setEnabled(false);
                        neighbor.setText(neighbor.getActionCommand());

                        if ("0".equals(neighbor.getActionCommand())) {
                            cleanNeighborBoxes(board, neighbor);
                        }
                    }
                }
            }
        }
    }

    private String calculateTempMines(Box[][] board, Box currBox) {
        if (currBox.isMined) return "💣";

        int count = 0;
        int rows = board.length;
        int cols = board[0].length;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = currBox.x + dx;
                int newY = currBox.y + dy;

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                    if (board[newX][newY].isMined) {
                        count++;
                    }
                }
            }
        }
        return String.valueOf(count);
    }
}
