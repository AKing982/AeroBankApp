import {TextField} from "@mui/material";

export default function PortField({value, onChange, isError})
{
    const label = value ? null : "Port";
    return (
        <div>
            <TextField
                type="number"
                label={label}
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