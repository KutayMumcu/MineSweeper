// MenuPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class MenuPanel extends JPanel {

    JTextField rowField, colField, mineField;
    JButton startButton;
    private JLabel scoreLabel;

    public MenuPanel() {
        setBackground(new Color(173, 216, 230)); // Açık mavi arka plan
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Highest Score Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        DecimalFormat df = new DecimalFormat("#.##");
        scoreLabel = new JLabel("Highest Score: " + df.format(getHighestScore()));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(scoreLabel, gbc);

        gbc.gridwidth = 1;

        // Row input
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Rows:"), gbc);
        rowField = new JTextField(10);
        gbc.gridy = 2;
        add(rowField, gbc);

        // Column input
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(new JLabel("Columns:"), gbc);
        colField = new JTextField(10);
        gbc.gridy = 2;
        add(colField, gbc);

        // Mines input
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(new JLabel("Number of Mines:"), gbc);
        mineField = new JTextField(20);
        gbc.gridy = 4;
        add(mineField, gbc);

        // Start button
        startButton = new JButton("Start");
        gbc.gridy = 5;
        add(startButton, gbc);
    }

    private double getHighestScore() {
        double score = 0.0;

        try {
            String fileName = "scores.txt";

            // Dosya varsa oku
            if (Files.exists(Paths.get(fileName))) {
                String content = new String(Files.readAllBytes(Paths.get(fileName))).trim();
                if (!content.isEmpty()) {
                    score = Double.parseDouble(content);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Hata oluştu: " + e.getMessage());
        }
        return score;
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
