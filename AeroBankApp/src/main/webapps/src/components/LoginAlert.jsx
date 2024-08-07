import {Alert} from "@mui/material";

export default function LoginAlert()
{
    return (
        <Alert severity="error">
            Incorrect user Credentials - Please Try again.
        </Alert>
    );
}