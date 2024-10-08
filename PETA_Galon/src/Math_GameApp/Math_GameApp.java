package Math_GameApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Math_GameApp extends JFrame {
    private JPanel panel;
    private JLabel questionLabel, scoreLabel, timerLabel, answerPromptLabel;
    private JButton additionButton, subtractionButton, multiplicationButton, divisionButton, submitButton;
    private JTextField answerField;
    private int correctAnswer, score, lives, questionCount;
    private Timer timer;
    private int timeLeft;
    private String currentOperation;

    public Math_GameApp() {
        setTitle("Math Game - 10-2 Galon");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create main panel with white background
        panel = new JPanel();
        panel.setBackground(Color.WHITE); // White background
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Allow resizing in both directions

        // Row 0: Question prompt
        questionLabel = new JLabel("Choose an operation to start:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.1; // Small vertical expansion
        panel.add(questionLabel, gbc);

        // Row 1: Score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Small vertical expansion
        panel.add(scoreLabel, gbc);

        // Row 1: Timer label
        timerLabel = new JLabel("Time Left: 30");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Small vertical expansion
        panel.add(timerLabel, gbc);

        // Create a panel for operation buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 columns with gaps

        additionButton = new JButton("Addition");
        additionButton.addActionListener(e -> startGame("+"));
        buttonPanel.add(additionButton);

        subtractionButton = new JButton("Subtraction");
        subtractionButton.addActionListener(e -> startGame("-"));
        buttonPanel.add(subtractionButton);

        multiplicationButton = new JButton("Multiplication");
        multiplicationButton.addActionListener(e -> startGame("*"));
        buttonPanel.add(multiplicationButton);

        divisionButton = new JButton("Division");
        divisionButton.addActionListener(e -> startGame("/"));
        buttonPanel.add(divisionButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weighty = 0.2; // Medium vertical expansion
        panel.add(buttonPanel, gbc);

        // Row 3: Question prompt
        answerPromptLabel = new JLabel("Answer the following question:");
        answerPromptLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.weighty = 0.1; // Small vertical expansion
        panel.add(answerPromptLabel, gbc);

        // Row 4: Answer text field
        answerField = new JTextField();
        answerField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.2; // Medium vertical expansion
        gbc.insets = new Insets(10, 10, 5, 10); // Add insets for spacing
        panel.add(answerField, gbc);

        // Row 5: Submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> checkAnswer());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.weighty = 0.1; // Small vertical expansion
        panel.add(submitButton, gbc);

        // Row 6: "Made by 10 - 2 Galon" label
        JLabel madeByLabel = new JLabel("Made by 10 - 2 Galon");
        madeByLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        madeByLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        gbc.gridy = 6; // Move to the next row
        panel.add(madeByLabel, gbc);

        add(panel, BorderLayout.CENTER);
        setVisible(true);

        lives = 3; // Number of lives
        score = 0;
        questionCount = 0;
    }

    private void startGame(String operation) {
        resetGame();
        currentOperation = operation;
        generateQuestion();
        startTimer();
    }

    private void resetGame() {
        score = 0;
        questionCount = 0;
        lives = 3;
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time Left: 30");
        answerField.setText("");
        if (timer != null) {
            timer.stop();
        }
    }

    private void startTimer() {
        timeLeft = 30;
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);
            if (timeLeft <= 0) {
                timer.stop();
                endGame();
            }
        });
        timer.start();
    }

    private void generateQuestion() {
        if (questionCount >= 5) {
            endGame();
            return;
        }

        Random rand = new Random();
        int num1 = rand.nextInt(10) + 1; // 1 to 10
        int num2 = rand.nextInt(10) + 1; // 1 to 10

        // Ensure division is valid
        if (currentOperation.equals("/")) {
            num1 = num1 * num2; // make num1 divisible by num2
        }

        correctAnswer = calculateAnswer(num1, num2);
        questionLabel.setText("Question " + (questionCount + 1) + ": " + num1 + " " + currentOperation + " " + num2 + " = ?");
        answerField.setText("");
        questionCount++;
    }

    private int calculateAnswer(int num1, int num2) {
        switch (currentOperation) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                return 0;
        }
    }

    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(answerField.getText());
            if (userAnswer == correctAnswer) {
                score++;
                scoreLabel.setText("Score: " + score);
                generateQuestion();
            } else {
                lives--;
                if (lives > 0) {
                    JOptionPane.showMessageDialog(this, "Wrong answer! The correct answer was: " + correctAnswer + ". Lives left: " + lives);
                    generateQuestion();
                } else {
                    timer.stop();
                    questionLabel.setText(""); // Clear the question label
                    JOptionPane.showMessageDialog(this, "Game Over! You scored " + score + ".\n10-2 Galon");
                    resetGame();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void endGame() {
        timer.stop();
        questionLabel.setText(""); // Clear the question label
        JOptionPane.showMessageDialog(this, "You answered " + score + " questions correctly.\n10-2 Galon");
        resetGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Math_GameApp::new);
    }
}
