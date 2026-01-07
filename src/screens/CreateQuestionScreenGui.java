// java
package screens;

import db.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.CommonConstants.BRIGHT_YELLOW;

public class CreateQuestionScreenGui extends JFrame {

    private static final Color BG_COLOR = new Color(48, 139, 255);
    private static final Color TITLE_COLOR = new Color(129, 255, 0);

    private JTextArea questionTextArea;
    private JTextField categoryField;
    private JTextField[] answerTextFields;
    private JRadioButton[] correctButtons;

    public CreateQuestionScreenGui() {
        super("Create a Question");
        setSize(660, 420);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_COLOR);

        addGuiComponents();
    }

    private void addGuiComponents() {
        JLabel titleLabel = new JLabel("Create your own Question");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(30, 20, 300, 30);
        add(titleLabel);

        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionLabel.setForeground(TITLE_COLOR);
        questionLabel.setBounds(30, 60, 100, 20);
        add(questionLabel);

        questionTextArea = new JTextArea();
        questionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        questionTextArea.setForeground(Color.BLACK);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        JScrollPane questionScroll = new JScrollPane(questionTextArea);
        questionScroll.setBounds(30, 85, 300, 120);
        add(questionScroll);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setForeground(TITLE_COLOR);
        categoryLabel.setBounds(30, 215, 100, 20);
        add(categoryLabel);

        categoryField = new JTextField();
        categoryField.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryField.setBounds(30, 240, 300, 28);
        categoryField.setForeground(Color.BLACK);
        add(categoryField);

        addAnswerComponents();

        //submit button
        JButton submitButton = new JButton("Submit Question");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(TITLE_COLOR);
        submitButton.setForeground(Color.BLACK);
        submitButton.setBounds(230, 320, 200, 40);
        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    String questionText = questionTextArea.getText().trim();
                    String categoryText = categoryField.getText().trim();
                    String[] answers = new String[4];
                    int correctIndex = 0;
                    for (int i = 0; i < 4; i++) {
                        answers[i] = answerTextFields[i].getText().trim();
                        if (correctButtons[i].isSelected()) {
                            correctIndex = i;
                        }
                    }
                    if (JDBC.insertQuestionToDB(questionText, categoryText, answers, correctIndex)) {
                        JOptionPane.showMessageDialog(CreateQuestionScreenGui.this,
                                "Question submitted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        resetFields();
                    } else {
                        JOptionPane.showMessageDialog(CreateQuestionScreenGui.this,
                                "Failed to submit question. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(CreateQuestionScreenGui.this,
                            "Please fill in all fields and select the correct answer.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(submitButton);

        //go back button
        JButton goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Arial", Font.BOLD, 16));
        goBackButton.setBackground(new Color(255, 102, 102)); // Light red
        goBackButton.setForeground(Color.BLACK);
        goBackButton.setBounds(30, 320, 150, 40);
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateQuestionScreenGui.this.dispose();
                new TitleScreenGui().setVisible(true);
            }
        });
        add(goBackButton);
    }

    private void addAnswerComponents() {
        String[] answerTitles = {"Answer #1", "Answer #2", "Answer #3", "Answer #4"};
        answerTextFields = new JTextField[4];
        correctButtons = new JRadioButton[4];
        ButtonGroup group = new ButtonGroup();

        int baseY = 80;
        int vGap = 60;
        for (int i = 0; i < 4; i++) {
            JLabel answerLabel = new JLabel(answerTitles[i]);
            answerLabel.setFont(new Font("Arial", Font.BOLD, 14));
            answerLabel.setForeground(TITLE_COLOR);
            answerLabel.setBounds(360, baseY + i * vGap - 20, 100, 20);
            add(answerLabel);

            correctButtons[i] = new JRadioButton();
            correctButtons[i].setBackground(BG_COLOR);
            correctButtons[i].setBounds(360, baseY + i * vGap, 20, 20);
            group.add(correctButtons[i]);
            add(correctButtons[i]);

            answerTextFields[i] = new JTextField();
            answerTextFields[i].setFont(new Font("Arial", Font.PLAIN, 14));
            answerTextFields[i].setForeground(Color.BLACK);
            answerTextFields[i].setBounds(385, baseY + i * vGap, 220, 28);
            add(answerTextFields[i]);
        }

        //give a defaultvalue to the first radio button
        correctButtons[0].setSelected(true);
    }
    private boolean validateInputs() {
        if (questionTextArea == null || questionTextArea.getText().trim().isEmpty()) {
            return false;
        }
        if (categoryField == null || categoryField.getText().trim().isEmpty()) {
            return false;
        }
        if (answerTextFields == null || correctButtons == null) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (answerTextFields[i] == null || answerTextFields[i].getText().trim().isEmpty()) {
                return false;
            }
        }
        boolean hasSelection = false;
        for (int i = 0; i < 4; i++) {
            if (correctButtons[i] != null && correctButtons[i].isSelected()) {
                hasSelection = true;
                break;
            }
        }
        return hasSelection;
    }
    private void resetFields() {
        questionTextArea.setText("");
        categoryField.setText("");
        for (JTextField answerField : answerTextFields) {
            answerField.setText("");
        }
        correctButtons[0].setSelected(true);
    }
}
