import BasicTextField from "./BasicTextField";
import {CircularProgress} from "@mui/material";
import {Skeleton} from "@mui/lab";

export default function EmailUserField({value, onChange, isLoading})
{
    if (isLoading) {
        return <Skeleton variant="rectangular" height={55} width="71%" />;
    }

    return (
        <BasicTextField
            label="User Name"
            value={value}
            onChange={onChange}
            height="55"
        />
    );
}