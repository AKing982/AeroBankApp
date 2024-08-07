import {Button} from "@mui/material";
import {Stack} from "@mui/system";

export default function BasicButton({text, submit})
{


    return(
        <Stack spacing={2} direction="row">
            <Button variant="contained" onClick={submit}>{text}</Button>
        </Stack>
    )
}