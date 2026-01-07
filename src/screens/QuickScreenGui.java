package screens;
//imports
import javax.swing.*;
import constants.CommonConstants;
import db.answer;
import db.category;
import db.JDBC;
import db.question;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class QuickScreenGui extends JFrame {
    private JButton[] answerButtons;
    private JTextArea questionTextArea;
    private JLabel scoreLabel;
    private JButton nextQuestionButton;
    private category category;
    private int numQuestions;

    private ArrayList<question> questions;
    private question currentQuestion;
    private int currentQuestionNumber;
    private int score;
    private boolean firstChoiceMade;

    public QuickScreenGui(category category, int numQuestions) {
        super("Quiz Game");
        setSize(400, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(CommonConstants.LIGHT_BLUE);
        answerButtons = new JButton[4];

        this.category = category;
        this.currentQuestionNumber = 0;
        this.score = 0;
        this.firstChoiceMade = false;

        //load the questions based on category
        questions = JDBC.getQuestionsByCategory(category);
        if (questions == null || questions.isEmpty()) {
            this.numQuestions = 0;
        } else {
            this.numQuestions = Math.min(numQuestions, questions.size());

            for (question q : questions) {
                ArrayList<answer> answers = JDBC.getAnswers(q);
                q.setAnswers(answers);
            }
            currentQuestion = questions.get(currentQuestionNumber);
        }
        addGuiComponents();
    }

    private void addGuiComponents() {
        // Topic label
        JLabel topicLabel = new JLabel("Topic: " + category.getCategoryName());
        topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topicLabel.setBounds(15, 15, 250, 20);
        topicLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(topicLabel);

        //score label
        scoreLabel = new JLabel("Score: " + score + "/" + numQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setBounds(280, 15, 100, 20);
        scoreLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(scoreLabel);

        //question text area
        questionTextArea = new JTextArea();
        questionTextArea.setFont(new Font("Arial", Font.BOLD, 18));
        questionTextArea.setBounds(15, 50, 370, 150);
        questionTextArea.setForeground(Color.BLACK);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setBackground(CommonConstants.LIGHT_BLUE);
        if (currentQuestion != null) {
            questionTextArea.setText(currentQuestion.getQuestionText());
        }
        add(questionTextArea);

        addAnswerComponents();

        // Return button
        JButton returnButton = new JButton("Return to Title");
        returnButton.setFont(new Font("Arial", Font.PLAIN, 12));
        returnButton.setBounds(250, 500, 135, 25);
        returnButton.setBackground(CommonConstants.LIGHT_GREEN);
        returnButton.setForeground(Color.BLACK);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new TitleScreenGui().setVisible(true);
            }
        });
        add(returnButton);

        nextQuestionButton = new JButton("Next");
        nextQuestionButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextQuestionButton.setBounds(15, 500, 100, 25);
        nextQuestionButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        nextQuestionButton.setForeground(Color.BLACK);
        nextQuestionButton.setVisible(false);
        nextQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestionNumber + 1 < numQuestions) {
                    currentQuestionNumber++;
                    currentQuestion = questions.get(currentQuestionNumber);
                    questionTextArea.setText(currentQuestion.getQuestionText());
                    firstChoiceMade = false;
                    nextQuestionButton.setVisible(false);
                    // Reset answer buttons
                    for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
                        answerButtons[i].setText(currentQuestion.getAnswers().get(i).getAnswerText());
                        answerButtons[i].setBackground(Color.WHITE);
                    }
                } else {
                    // Quiz finished - show results
                    JOptionPane.showMessageDialog(QuickScreenGui.this,
                            "Quiz Complete!\nYour final score: " + score + "/" + numQuestions,
                            "Quiz Finished",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new TitleScreenGui().setVisible(true);
                }
            }
        });
        add(nextQuestionButton);
    }

    private void addAnswerComponents() {
        if (currentQuestion == null || currentQuestion.getAnswers() == null) {
            return;
        }

        int verticalSpace = 60;
        for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
            answer ans = currentQuestion.getAnswers().get(i);
            JButton answerButton = new JButton(ans.getAnswerText());
            answerButton.setFont(new Font("Arial", Font.PLAIN, 14));
            answerButton.setBounds(15, 220 + (i * verticalSpace), 370, 50);
            answerButton.setBackground(Color.WHITE);
            answerButton.setForeground(Color.BLACK);
            answerButton.setHorizontalAlignment(SwingConstants.LEFT);

            final int index = i;
            answerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!firstChoiceMade) {
                        firstChoiceMade = true;
                        handleAnswerSelection(index);
                    }
                }
            });

            answerButtons[i] = answerButton;
            add(answerButton);
        }
    }

    private void handleAnswerSelection(int selectedIndex) {
        answer selectedAnswer = currentQuestion.getAnswers().get(selectedIndex);

        // Highlight correct/incorrect answers
        for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
            answer ans = currentQuestion.getAnswers().get(i);
            if (ans.isCorrect()) {
                answerButtons[i].setBackground(CommonConstants.LIGHT_GREEN);
            } else if (i == selectedIndex) {
                answerButtons[i].setBackground(CommonConstants.LIGHT_RED);
            }
        }

        // Update score if correct
        if (selectedAnswer.isCorrect()) {
            score++;
            scoreLabel.setText("Score: " + score + "/" + numQuestions);
        }

        // Show the Next button
        nextQuestionButton.setVisible(true);
    }
}
