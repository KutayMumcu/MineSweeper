import javax.swing.*;

public class Box extends JButton {
    public boolean isOpen = false;
    public boolean isFlagged = false;
    public boolean isMined = false;
    public int x, y;

    public Box(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
