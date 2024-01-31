import {TextField} from "@mui/material";

export default function PortField({value, onChange, isError})
{
    return (
        <div>
            <TextField
                type="number"
                label="Port"
                value={value}
                onChange={onChange}
                error={isError}
                helperText={isError ? "Port must be between 0 and 65535" : ""}
                inputProps={{
                    min: 0,
                    max: 65535
                }}
            />
        </div>
    );
}