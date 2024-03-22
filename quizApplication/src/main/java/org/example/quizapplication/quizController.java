package org.example.quizapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.swing.*;

public class quizController implements Initializable {

    public static class QuizQuestion {
        private String question;
        private String options;
        private String answer;

        @Override
        public String toString() {
            return "QuizQuestion{" +
                    "question='" + question + '\'' +
                    ", answer='" + answer + '\'' +
                    ", options='" + options + '\'' +
                    '}';
        }

        public QuizQuestion(String question, String options, String answer) {
            this.question = question;
            this.options = options;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }
    }
    private List<QuizQuestion> loadQuestionsFromCSV(String fileName) {
        List<QuizQuestion> list = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                String question = parts[0];
                String options = parts[1];
                String correctAnswer = parts[2];
                list.add(new QuizQuestion(question, options, correctAnswer));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //Text Area
    @FXML
    public TextArea getScore;
    public TextArea quizQuestions;
    public TextArea quizSummary;
    public TextArea answerText;
    public TextArea errorText;

    //Buttons
    public Button processModels;
    public Button agilityProcess;
    public Button submitAnswer;
    public Button nextQuestion;

    private int nextButtonCount = 2;
    private int scoreValue = 0;
    private String buttonID;
    private String final_answer;

    @FXML
    private void processModelsButton(javafx.event.ActionEvent ae) throws IOException {
        //Just Declared but not used
        //Just Created during design
    }

    @FXML
    private void agilityProcessButton(javafx.event.ActionEvent ae) throws IOException {
        //Just Declared but not used
        //Just Created during design
    }
    @FXML
    private void nextQuestionButton(javafx.event.ActionEvent ae) throws IOException{
        String final_questions = "";
        String [] final_options = {};
        switch (buttonID){
            case "processModels": {
                String [] getQuestionsProcess = fetchQuestionsProcessModel(nextButtonCount);
                final_questions = getQuestionsProcess[0];
                final_options = getQuestionsProcess[1].split(",");
                final_answer = getQuestionsProcess[2];
            }
            break;
            case "agilityProcess": {
                String [] getQuestionsAgility = fetchQuestionsAgility(nextButtonCount);
                final_questions = getQuestionsAgility[0];
                final_options = getQuestionsAgility[1].split(",");
                final_answer = getQuestionsAgility[2];
            }
            break;
            default: errorText.setText("Select any of the above button to get quiz");
        }
        quizQuestions.setText("Question: \n" +
                    final_questions + "\n" +
                    final_options[0] + "\n" +
                    final_options[1] + "\n" +
                    final_options[2] + "\n" +
                    final_options[3] + "\n"
            );
        if(nextButtonCount >= 10){
            nextButtonCount = 1;
            nextQuestion.setDisable(true);
            quizQuestions.clear();
        }
        nextButtonCount++;
    }
    private String[] fetchQuestionsProcessModel(int nextQuestion){
        File file = new File("process model.csv");
        List<QuizQuestion> quizQuestions = loadQuestionsFromCSV(file.getAbsolutePath());
        QuizQuestion strForQuestion = quizQuestions.get(nextQuestion);
        return new String[]{strForQuestion.question, strForQuestion.options, strForQuestion.answer};
    }

    private String[] fetchQuestionsAgility(int nextQuestion){
        File file = new File("agility.csv");
        List<QuizQuestion> quizQuestions = loadQuestionsFromCSV(file.getAbsolutePath());
        QuizQuestion strForQuestion = quizQuestions.get(nextQuestion);
        return new String[]{strForQuestion.question, strForQuestion.options, strForQuestion.answer};
    }

    private void printFirstQuestion(String [] printStatement){
        String final_questions = printStatement[0];
        String [] final_options = printStatement[1].split(",");
        final_answer = printStatement[2];
        quizQuestions.setText("Question: \n" +
                final_questions + "\n" +
                final_options[0] + "\n" +
                final_options[1] + "\n" +
                final_options[2] + "\n" +
                final_options[3] + "\n"
        );
    }
    private void questionType(javafx.event.ActionEvent event) throws IOException{
        Button clickedButton = (Button) event.getSource();
        buttonID = clickedButton.getId();
        if (Objects.equals(buttonID, "processModels")){
            agilityProcess.setDisable(true);
            String [] getQuestionsProcess = fetchQuestionsProcessModel(1);
            printFirstQuestion(getQuestionsProcess);
        }
        else {
            processModels.setDisable(true);
            String [] getQuestionsProcess = fetchQuestionsAgility(1);
            printFirstQuestion(getQuestionsProcess);
        }
        clickedButton.setDisable(true);
    }
    @FXML
    private void submitAnswerButton(javafx.event.ActionEvent ae) throws IOException {
        if(Character.toString(final_answer.charAt(0)).equals(answerText.getText().toLowerCase())){
            scoreValue ++;
            getScore.setText(String.valueOf(scoreValue));
            errorText.setText("Correct Answer");
        }
        else {
            errorText.setText("Incorrect Answer");
        }
        if(nextButtonCount >= 10){
            quizSummary.setText("Your Score for the quiz is: " + scoreValue + "/10");
            nextButtonCount = 1;
            submitAnswer.setDisable(true);
            quizQuestions.clear();
            getScore.clear();
            restartAlert();
        }
        if(Objects.equals(answerText.getText(), "")){
            errorText.setText("Please select an option from a/b/c/d");
        }
        answerText.clear();
    }
    public static void restartAlert() {
        JFrame frame = new JFrame("Restart Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton restartButton = new JButton("Close");
        restartButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame, "Quiz Completed" , "Close window", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame, "Thank you for being here!");
                System.exit(0);
            }
        });
        JPanel panel = new JPanel();
        panel.add(restartButton);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getScore.setPromptText("0");
        quizQuestions.setPromptText("Click on the above options, either Process Models or Agility and Process Models");
        quizSummary.setPromptText("No summary available right now");
        processModels.setOnAction(ae -> {
            try {
                questionType(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        agilityProcess.setOnAction(ae -> {
            try {
                questionType(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        submitAnswer.setOnAction(ae -> {
            try {
                submitAnswerButton(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        nextQuestion.setOnAction(ae -> {
            try {
                nextQuestionButton(ae);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}