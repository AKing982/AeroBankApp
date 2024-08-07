import BasicTextField from "./BasicTextField";
import {CircularProgress} from "@mui/material";

export default function MailServerField({value, onChange, isLoading})
{
    return (
        <BasicTextField
            label="Mail Server"
            height="55"
            value={isLoading ? '' : value}
            onChange={onChange}
            InputProps={{
                endAdornment: isLoading && <CircularProgress size={20} />,
                readOnly: isLoading,
            }}
        />
    );
}