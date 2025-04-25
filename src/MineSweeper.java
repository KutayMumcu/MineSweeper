// MineSweeper.java
import javax.swing.*;
import java.util.Random;

public class MineSweeper extends JFrame implements GameListener{
    private int width, height, rows, cols, mineCount;
    private Box[][] grid;

    public MineSweeper() {
        super("MineSweeper");
        this.width = 800;
        this.height = 800;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        showMenu();
    }

    private void showGamePanel(Box[][] board, int mineCount) {
        GamePanel gamePanel = new GamePanel(board, mineCount, this);
        setContentPane(gamePanel);
        revalidate();
        repaint();
    }

    @Override
    public void onRestart(int rows, int cols, int mineCount) {
        createBoard(rows, cols, mineCount);
        showGamePanel(grid, mineCount);
    }

    @Override
    public void onReturnToMenu() {
        showMenu();
    }

    private void showMenu() {
        MenuPanel menuPanel = new MenuPanel();
        setContentPane(menuPanel);
        revalidate();
        repaint();

        menuPanel.setStartAction(e -> {
            try {
                rows = menuPanel.getRows();
                cols = menuPanel.getCols();
                mineCount = menuPanel.getMineCount();

                if (rows < 3 || rows > 17 || cols < 3 || cols > 17 || mineCount < 1 || mineCount >= rows * cols) {
                    JOptionPane.showMessageDialog(this, "Please enter valid values:\nRows/Cols: 3-17\nMines: 1 to " + (rows * cols - 1));
                    return;
                }

                createBoard(rows, cols, mineCount);
                GamePanel gamePanel = new GamePanel(grid, mineCount, this);
                setContentPane(gamePanel);
                revalidate();
                repaint();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid integers.");
            }
        });

        this.setVisible(true);
    }

    private void createBoard(int rows, int cols, int mineNumber) {
        grid = new Box[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Box(i, j);
            }
        }
        while (mineNumber != 0) {
            Random rand = new Random();
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);
            if (!grid[row][col].isMined) {
                grid[row][col].isMined = true;
                mineNumber--;
            }
        }
    }
}