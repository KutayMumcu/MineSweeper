import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

public class MineSweeper extends JFrame{
    private int width,height, rows, cols, mineCount;
    private Box[][] grid;

    public MineSweeper() {
        super("MineSweeper");
        this.width = 600;
        this.height = 600;
        getInitialInformations();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        addPanel();

        this.setVisible(true);
    }

    private void addPanel(){
        try {
            this.add(new GamePanel(grid, mineCount));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getInitialInformations() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Rows: ");
        this.rows = sc.nextInt();
        System.out.println("Enter Columns: ");
        this.cols = sc.nextInt();
        System.out.println("Enter Mine Number: ");
        this.mineCount = sc.nextInt();
        createBoard(rows, cols, mineCount);
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
