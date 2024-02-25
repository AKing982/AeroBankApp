import {CircularProgress, TextField} from "@mui/material";
import {Skeleton} from "@mui/lab";

export default function PortField({value, onChange, isError, isLoading})
{
    const label = value ? null : "Port";

    if (isLoading) {
        return <Skeleton variant="rectangular" height={56} width="10%" />;
    }

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