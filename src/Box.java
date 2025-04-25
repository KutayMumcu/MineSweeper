import javax.swing.*;

public class Box extends JButton {
    public boolean isOpen = false;
    public boolean isFlagged = false;
    public boolean isMined = false;
    public int x, y;
    private String text;

    public Box(int x, int y) {
        this.x = x;
        this.y = y;
        this.text = "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
