import {Typography} from "@mui/material";
import {useSearchParams} from "react-router-dom";

function PayeesContentPage()
{
    const [searchParams] = useSearchParams();
    const userID = searchParams.get('userID');

    return (
        <Typography variant="h4"> Payees Content (User ID: {userID})</Typography>
    );
}

export default PayeesContentPage;