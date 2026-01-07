package screens;

import constants.CommonConstants;
import db.JDBC;
import db.category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TitleScreenGui extends JFrame {

    private JTextField numQuestionsField;
    private JComboBox categoryMenu;

    public TitleScreenGui() {
        super("Title Screen");
        setSize(400, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(CommonConstants.DARK_BLUE);
        addGuiComponents();
    }

    protected void addGuiComponents() {
        //title label
        JLabel titleLabel = new JLabel("Quiz Game!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0, 20, 390, 43);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(CommonConstants.LIGHT_GREEN);
        add(titleLabel);

        //choose category Label
        JLabel chooseCategoryLabel = new JLabel("Choose a Category:");
        chooseCategoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseCategoryLabel.setBounds(0, 80, 400, 43);
        chooseCategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseCategoryLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(chooseCategoryLabel);

        // CATEGORY DROPDOWN MENU
        ArrayList<String> categoryList = JDBC.getCategories();
        categoryMenu = new JComboBox(categoryList.toArray());
        categoryMenu.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryMenu.setBounds(20, 120, 337, 45);
        categoryMenu.setBackground(CommonConstants.LIGHT_BLUE);
        categoryMenu.setForeground(Color.BLACK);
        add(categoryMenu);

        // number of questions label (left)
        JLabel numQuestionsLabel = new JLabel("Number of Questions:");
        numQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        numQuestionsLabel.setBounds(20, 190, 180, 30);
        numQuestionsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        numQuestionsLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(numQuestionsLabel);

        // number of questions text field (right)
        numQuestionsField = new JTextField("10");
        numQuestionsField.setFont(new Font("Arial", Font.PLAIN, 14));
        numQuestionsField.setBounds(210, 185, 147, 40);
        numQuestionsField.setBackground(CommonConstants.LIGHT_BLUE);
        numQuestionsField.setForeground(Color.BLACK);
        add(numQuestionsField);

        //quiz start button
        JButton startQuizButton = new JButton("Start Quiz");
        startQuizButton.setFont(new Font("Arial", Font.BOLD, 18));
        startQuizButton.setBounds(100, 300, 200, 60);
        startQuizButton.setBackground(CommonConstants.LIGHT_GREEN);
        startQuizButton.setForeground(Color.BLACK);
        startQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected category
                String selectedCategoryName = (String) categoryMenu.getSelectedItem();
                if (selectedCategoryName == null || selectedCategoryName.isEmpty()) {
                    JOptionPane.showMessageDialog(TitleScreenGui.this,
                            "Please select a category.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get number of questions
                int numQuestions;
                try {
                    numQuestions = Integer.parseInt(numQuestionsField.getText().trim());
                    if (numQuestions <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TitleScreenGui.this,
                            "Please enter a valid number of questions.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get category object from database
                category categoryObj = JDBC.getCategoryByName(selectedCategoryName);
                if (categoryObj == null) {
                    JOptionPane.showMessageDialog(TitleScreenGui.this,
                            "Category not found.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Open Quiz Screen
                QuickScreenGui quizScreen = new QuickScreenGui(categoryObj, numQuestions);
                quizScreen.setVisible(true);
                dispose();
            }
        });
        add(startQuizButton);

        //exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setBounds(65, 365, 262, 45);
        exitButton.setBackground(CommonConstants.LIGHT_RED);
        exitButton.setForeground(Color.BLACK);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TitleScreenGui.this.dispose();
                System.exit(0);
            }
        });
        add(exitButton);

        //create question button
        JButton createQuestionButton = new JButton("Create Question");
        createQuestionButton.setFont(new Font("Arial", Font.BOLD, 18));
        createQuestionButton.setBounds(100, 430, 200, 60);
        createQuestionButton.setBackground(CommonConstants.LIGHT_GREEN);
        createQuestionButton.setForeground(Color.BLACK);
        createQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Create Question Screen
                CreateQuestionScreenGui createQuestionScreen = new CreateQuestionScreenGui();
                createQuestionScreen.setVisible(true);
                // Close the Title Screen
                dispose();
            }
        });
        add(createQuestionButton);
    }
}
