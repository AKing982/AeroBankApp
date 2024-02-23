import BasicTextField from "./BasicTextField";
import {CircularProgress} from "@mui/material";

export default function EmailUserField({value, onChange, isLoading})
{
    return (
        <BasicTextField label="User Name"
                        value={isLoading ? '' : value}
                        onChange={onChange}
                        height="55"
                        InputProps={{
                            endAdornment: isLoading && <CircularProgress size={20} />,
                            readOnly: isLoading,
                        }}/>
    );
}