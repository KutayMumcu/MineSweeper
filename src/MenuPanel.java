// MenuPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JTextField rowField;
    private JTextField colField;
    private JTextField mineField;
    private JButton startButton;

    public MenuPanel() {
        setBackground(new Color(173, 216, 230));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Row
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Rows:"), gbc);
        rowField = new JTextField(10);
        gbc.gridy = 1;
        add(rowField, gbc);

        // Col
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(new JLabel("Columns:"), gbc);
        colField = new JTextField(10);
        gbc.gridy = 1;
        add(colField, gbc);

        // Mines
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(new JLabel("Number of Mines:"), gbc);
        mineField = new JTextField(20);
        gbc.gridy = 3;
        add(mineField, gbc);

        // Start button
        startButton = new JButton("Start");
        gbc.gridy = 4;
        add(startButton, gbc);
    }

    public int getRows() {
        return Integer.parseInt(rowField.getText());
    }

    public int getCols() {
        return Integer.parseInt(colField.getText());
    }

    public int getMineCount() {
        return Integer.parseInt(mineField.getText());
    }

    public void setStartAction(ActionListener listener) {
        startButton.addActionListener(listener);
    }
}
