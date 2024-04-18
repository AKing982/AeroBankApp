import {Container} from "@mui/system";
import {Button, FormControl, MenuItem, TextField, Typography} from "@mui/material";
import {useState} from "react";

export default function SecurityQuestionForm({activeStep, handleStepChange, securityForm, handleSecurityFormChange}){
    const [selectedQuestion, setSelectedQuestion] = useState('');
    const [answer, setAnswer] = useState('');

    const securityQuestions = [
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

    const handleQuestionChange = (event) => {
        setSelectedQuestion(event.target.value);
    };

    const handleNextButtonClick = (e) => {
        e.preventDefault();
        handleStepChange(activeStep + 1);
    };

    const handleChange = () => {

    }

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
                        value={selectedQuestion}
                        onChange={handleQuestionChange}
                        helperText="Please select your security question"
                    >
                        {securityQuestions.map((question, index) => (
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
