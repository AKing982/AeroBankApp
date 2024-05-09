import {Button} from "@mui/material";

function SavePaymentTemplate({currentPaymentDetails, onSaveTemplate}){
    return (
        <Button
            variant="contained"
            color="primary"
            onClick={() => onSaveTemplate(currentPaymentDetails)}
            sx={{ mt: 2 }}
        >
            Save as Template
        </Button>
    );
}

export default SavePaymentTemplate;