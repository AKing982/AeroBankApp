import {Container} from "@mui/system";
import {Button, FormControl, MenuItem, TextField, Typography} from "@mui/material";
import {useState} from "react";

export default function SecurityQuestionForm({activeStep, handleStepChange, securityForm, handleSecurityFormChange}){
    const [selectedQuestion, setSelectedQuestion] = useState('');
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
    const [answer, setAnswer] = useState('');

    const predefinedQuestions = [
        "What was the make and model of your first car?",
        "What was the name of your first pet?",
        "In what city did your parents meet?",
        "What was the name of your elementary school?",
        "What street did you live on in third grade?",
        "What was your childhood nickname?",
        "In what city or town did your mother and father meet?",
        "What was the name of your favorite teacher in high school?",
        "What was your dream job as a child?",
        "What is the first name of the person you went to your first concert with?"
    ];

    const handleQuestionSelect = (event) => {
        const question = event.target.value;
        const index = predefinedQuestions.indexOf(question);
        setCurrentQuestionIndex(index);
        const currentQuestion = securityForm.securityQuestions.find(q => q.question === question);
        setAnswer(currentQuestion ? currentQuestion.answer : '');
    };

    const handleNextButtonClick = (e) => {
        e.preventDefault();
        // Assuming `selectedQuestion` is the current question selected from dropdown and `answer` is the state holding the answer input
        const updatedQuestions = [...securityForm.securityQuestions];

        // Check if the question already exists in the array
        const questionIndex = updatedQuestions.findIndex(q => q.question === selectedQuestion);

        if (questionIndex > -1) {
            // Update existing question
            updatedQuestions[questionIndex] = {
                ...updatedQuestions[questionIndex],
                answer: answer
            };
        } else {
            // Add new question and answer
            updatedQuestions.push({ question: selectedQuestion, answer: answer });
        }

        // Update the securityForm state with the new array of questions
        handleSecurityFormChange({ ...securityForm, securityQuestions: updatedQuestions });

        // Proceed to next step
        handleStepChange(activeStep + 1);
        console.log('Security Questions: ', updatedQuestions.length);
    };


    const handleAnswerChange = (event) => {
        setAnswer(event.target.value);
    };

    const handleSubmit = (event) => {

    }

    return (
        <Container maxWidth="sm">
            <Typography variant="h6" align="center" gutterBottom>
                Security Questions
            </Typography>
            <form onSubmit={handleSubmit}>
                <FormControl fullWidth margin="normal">
                    <TextField
                        select
                        label="Select a Security Question"
                        value={predefinedQuestions[currentQuestionIndex]}
                        onChange={handleQuestionSelect}
                        helperText="Please select your security question"
                    >
                        {predefinedQuestions.map((question, index) => (
                            <MenuItem key={index} value={question}>
                                {question}
                            </MenuItem>
                        ))}
                    </TextField>
                </FormControl>
                <FormControl fullWidth margin="normal">
                    <TextField
                        fullWidth
                        label="Your Answer"
                        value={answer}
                        multiline
                        rows={1}
                        onChange={handleAnswerChange}
                        required
                    />
                </FormControl>
                <TextField
                    fullWidth
                    label="PIN"
                    name="pin"
                    type="number"
                    multiline
                    rows={1}
                    value={securityForm.pin}
                    onChange={handleSecurityFormChange}
                    margin="normal"
                    required
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    size="large"
                    onClick={handleNextButtonClick}
                >
                    Next
                </Button>
            </form>
        </Container>
    );
}
