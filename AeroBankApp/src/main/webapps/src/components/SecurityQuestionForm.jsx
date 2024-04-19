import {Container} from "@mui/system";
import {Button, Divider, FormControl, MenuItem, TextField, Typography} from "@mui/material";
import {useState} from "react";
import React from "react";


export default function SecurityQuestionForm({activeStep, handleStepChange, securityForm, handleSecurityFormChange}){
    const [selectedQuestions, setSelectedQuestions] = useState(['', '']);
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
    const [answers, setAnswers] = useState(['', '']);

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

    const handleQuestionSelect = (index) => (event) => {
        const newQuestions = [...selectedQuestions];
        newQuestions[index] = event.target.value;
        setSelectedQuestions(newQuestions);

        // Update answers based on previously saved answers if they exist
        const currentQuestion = securityForm.securityQuestions.find(q => q.question === event.target.value);
        const newAnswers = [...answers];
        newAnswers[index] = currentQuestion ? currentQuestion.answer : '';
        setAnswers(newAnswers);
    };
    // const handleNextButtonClick = (e) => {
    //     e.preventDefault();
    //     // Update the security questions in the main form state
    //     const updatedQuestions = selectedQuestions.map((question, index) => ({
    //         question: question,
    //         answer: answers[index]
    //     }));
    //
    //     // Ensure to merge with existing questions that are not being edited
    //     const newSecurityQuestions = securityForm.securityQuestions
    //         .filter(q => !selectedQuestions.includes(q.question))
    //         .concat(updatedQuestions);
    //
    //     handleSecurityFormChange({ ...securityForm, securityQuestions: newSecurityQuestions });
    //
    //     // Proceed to the next step
    //     handleStepChange(activeStep + 1);
    //     console.log('Security Questions: ', updatedQuestions.length);
    // };

    const handleNextButtonClick = (e) => {
        e.preventDefault();

        // Create the array of updated question-answer pairs
        const updatedQuestions = selectedQuestions.map((question, index) => ({
            question: question,
            answer: answers[index]
        }));

        // This ensures that you are not adding empty questions or answers
        const validUpdatedQuestions = updatedQuestions.filter(q => q.question && q.answer);

        // Merge with existing questions that are not currently being edited
        const newSecurityQuestions = securityForm.securityQuestions
            .filter(q => !selectedQuestions.includes(q.question))
            .concat(validUpdatedQuestions);

        // Update the main form state with the new set of security questions
        handleSecurityFormChange({ ...securityForm, securityQuestions: newSecurityQuestions });

        // Move to the next step in the form process
        handleStepChange(activeStep + 1);
    };
    const handleAnswerChange = (index) => (event) => {
        const newAnswers = [...answers];
        newAnswers[index] = event.target.value;
        setAnswers(newAnswers);
    };


    const handleSubmit = (event) => {

    }

    return (
        <Container maxWidth="sm">
            <Typography variant="h6" align="center" gutterBottom>
                Security Questions
            </Typography>
            <form onSubmit={e => e.preventDefault()}>
                {Array.from({ length: 2 }).map((_, index) => (
                    <React.Fragment key={index}>
                        <FormControl fullWidth margin="normal">
                            <TextField
                                select
                                label={`Select Security Question ${index + 1}`}
                                value={selectedQuestions[index]}
                                onChange={handleQuestionSelect(index)}
                                helperText="Please select your security question"
                            >
                                {predefinedQuestions.map((question, idx) => (
                                    <MenuItem key={idx} value={question} disabled={selectedQuestions.includes(question)}>
                                        {question}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </FormControl>
                        <FormControl fullWidth margin="normal">
                            <TextField
                                fullWidth
                                label="Your Answer"
                                value={answers[index]}
                                multiline
                                rows={1}
                                onChange={handleAnswerChange(index)}
                                required
                            />
                            <Divider />
                        </FormControl>
                    </React.Fragment>
                ))}
                <Button
                    type="button"
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
