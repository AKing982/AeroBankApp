import {FormControl, MenuItem, TextField, Typography} from "@mui/material";
import {Container} from "@mui/system";
import {useState} from "react";

export default function SecurityQuestion(){
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

    const handleChange = () => {

    }

    const handleAnswerChange = (event) => {
        setAnswer(event.target.value);
    };

    const handleSubmit = (event) => {

    }

    return (
        <Container maxWidth="sm">
            <Typography variant="h4" align="center" gutterBottom>
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
                    value={formData.pin}
                    onChange={handleChange}
                    margin="normal"
                    required
                />
            </form>
        </Container>
    );
}