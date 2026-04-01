# 🎮 Quiz Game

A Java Swing desktop application for creating and playing quiz games with MySQL database integration.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

---

## 📖 About

**Quiz Game** is a lightweight desktop quiz application that lets users both **play quizzes** and **create their own questions**. It stores all questions, categories, and answers in a MySQL database, so content is persistent and reusable across sessions.

### ❓ Problem It Solves

Traditional quiz tools are either too complex, require an internet connection, or don't let users build their own question banks easily. Quiz Game solves this by providing:

- A **simple, offline desktop application** — no browser or internet needed.
- An **easy question-creation workflow** — anyone can add questions without touching SQL.
- **Automatic score tracking** with instant right/wrong feedback, removing the need for manual marking.
- **Randomised question and answer order** so the same quiz feels fresh every time.

### 👥 Who Is It For?

| Audience | Use Case |
|----------|----------|
| **Students** | Self-study and revision across any subject |
| **Teachers / Trainers** | Build question banks for classroom quizzes or assessments |
| **Quiz Enthusiasts** | Create and play trivia games with friends |
| **Java Learners** | Study a practical example of Java Swing + JDBC + MySQL integration |

### 🛠️ Technologies Used

| Layer | Technology |
|-------|------------|
| Language | Java 17+ |
| GUI | Java Swing |
| Database | MySQL 8.0 |
| DB Connectivity | JDBC (MySQL Connector/J 9.5.0) |
| Config | Custom `.env` file loader |

---

### Title Screen
- Select a category from the dropdown menu
- Enter the number of questions you want to answer
- Start the quiz or create your own questions

### Quiz Screen
- Answer multiple-choice questions
- Get instant feedback (green for correct, red for incorrect)
- Track your score in real-time
- Navigate through questions with the "Next" button

### Create Question Screen
- Add your own questions to the database
- Specify the category
- Add 4 answer options and mark the correct one

## ✨ Features

- 🎯 **Play Quiz**: Answer questions from various categories
- ➕ **Create Questions**: Add your own questions to the database
- 📊 **Score Tracking**: Real-time score updates during the quiz
- 🔀 **Randomized Questions**: Questions and answers are shuffled for variety
- 💾 **MySQL Database**: Persistent storage for all quiz data
- 🎨 **Clean UI**: User-friendly Java Swing interface

## 🛠️ Technologies Used

- **Java 17+** - Core programming language
- **Java Swing** - GUI framework
- **MySQL 8.0** - Database
- **JDBC** - Database connectivity

## 📋 Prerequisites

Before running this application, make sure you have:

- [Java JDK 17+](https://www.oracle.com/java/technologies/javase-downloads.html) installed
- [MySQL 8.0+](https://dev.mysql.com/downloads/) installed and running
- MySQL Connector/J (included in `lib/` folder)

## 🚀 Installation

### 1. Clone the repository

```bash
git clone https://github.com/ImethDewmina128At90/QuizGame.git
cd QuizGame
```

### 2. Configure the database

Copy the example environment file and update with your MySQL credentials:

```bash
cp .env.example .env
```

Edit `.env` file with your database settings:

```env
DB_URL=jdbc:mysql://127.0.0.1:3306/quiz_gui_db
DB_USERNAME=root
DB_PASSWORD=your_password_here
```

### 3. Compile the application

```bash
javac -cp "lib/mysql-connector-j-9.5.0.jar" -d out src/constants/*.java src/db/*.java src/screens/*.java src/App.java
```

### 4. Run the application

```bash
java -cp "out;lib/mysql-connector-j-9.5.0.jar" App
```

> **Note for Linux/Mac users**: Use `:` instead of `;` in the classpath:
> ```bash
> java -cp "out:lib/mysql-connector-j-9.5.0.jar" App
> ```

## 📁 Project Structure

```
QuizGame/
├── src/
│   ├── App.java                 # Main entry point
│   ├── constants/
│   │   └── CommonConstants.java # Color constants
│   ├── db/
│   │   ├── JDBC.java           # Database operations
│   │   ├── EnvLoader.java      # Environment variable loader
│   │   ├── category.java       # Category data model
│   │   ├── question.java       # Question data model
│   │   └── answer.java         # Answer data model
│   └── screens/
│       ├── TitleScreenGui.java         # Main menu screen
│       ├── QuickScreenGui.java         # Quiz gameplay screen
│       └── CreateQuestionScreenGui.java # Question creation screen
├── lib/
│   └── mysql-connector-j-9.5.0.jar     # MySQL JDBC driver
├── .env.example                 # Example environment config
├── .gitignore                   # Git ignore rules
└── README.md                    # This file
```

## 🗄️ Database Schema

The application automatically creates the following tables:

```sql
-- Categories table
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL UNIQUE
);

-- Questions table
CREATE TABLE question (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    question_text TEXT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- Answers table
CREATE TABLE answer (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    answer_text VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (question_id) REFERENCES question(question_id)
);
```

## 🎮 How to Play

1. **Launch the application** - The Title Screen will appear
2. **Select a category** - Choose from available categories in the dropdown
3. **Set number of questions** - Enter how many questions you want
4. **Click "Start Quiz"** - Begin answering questions
5. **Select your answer** - Click on an answer button
6. **View feedback** - Green = correct, Red = incorrect
7. **Click "Next"** - Move to the next question
8. **View final score** - See your results at the end

## ➕ Adding Questions

1. Click **"Create Question"** on the Title Screen
2. Enter your question text
3. Enter or select a category
4. Fill in all 4 answer options
5. Select the correct answer using the radio button
6. Click **"Submit Question"**

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 👤 Author

**Imeth Dewmina**
- GitHub: [@ImethDewmina128At90](https://github.com/ImethDewmina128At90)

## 🙏 Acknowledgments

- Java Swing documentation
- MySQL documentation
- All contributors who helped improve this project

---

⭐ Star this repository if you found it helpful!

