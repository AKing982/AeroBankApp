import {Autocomplete, TextField} from "@mui/material";

function UsePaymentTemplate({templates, onSelectTemplate}){
    return (
        <Autocomplete
            options={templates}
            getOptionLabel={(template) => template.templateName}
            fullWidth
            renderInput={(params) => <TextField {...params} label="Select a payment template" />}
            onChange={(event, newValue) => {
                onSelectTemplate(newValue);
            }}
        />
    );

}

export default UsePaymentTemplate;