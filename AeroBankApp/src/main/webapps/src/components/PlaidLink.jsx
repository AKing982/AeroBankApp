import {useCallback, useEffect, useState} from "react";
import axios from "axios";
import {usePlaidLink} from 'react-plaid-link';
import {Button} from "@mui/material";

function PlaidLink({userID, linkToken})
{
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [isLinked, setIsLinked] = useState(false);


    // useEffect(() => {
    //     const fetchLinkToken = async () => {
    //         setIsLoading(true);
    //         try
    //         {
    //             const response = await fetch(`http://localhost:8080/AeroBankApp/api/plaid/create_link_token`, {
    //                 method: 'POST',
    //                 headers: {
    //                     'Content-Type': 'application/json',
    //                 },
    //                 body: JSON.stringify({userId: userID}),
    //             });
    //
    //             const data = response.json();
    //             console.log('Link Token: ', data.link_token);
    //             setLinkToken(response.data.link_token);
    //
    //         }catch(err)
    //         {
    //             console.error("There was an error fetching the create link token: ", err);
    //         }
    //         setIsLoading(false);
    //     };
    //     fetchLinkToken();
    // }, []);

    const onSuccess = useCallback(async (publicToken, metadata) => {
        setIsLoading(true);
        try
        {
            await axios.post(`http:/localhost:8080/AeroBankApp/api/plaid/exchange_public_token`, {publicToken: publicToken});
            setIsLinked(true);
        }catch(err)
        {
            setError('Failed to exchange public token');
            console.error("There was an error exchanging the public token: ", err);
        }
       setIsLoading(false);
        console.log('Account linked successfully');
    }, [userID]);

    const onExit = useCallback((err, metadata) => {
        if(err != null)
        {
            setError(err.display_message || 'Error linking account');
        }
    }, []);

    const config = {
        token: linkToken,
        onSuccess,
    };

    const handleConnectClick = () => {
        if(ready)
        {
            open();
        }
    };

    const {open, ready} = usePlaidLink(config);
    if(isLoading) return <div>Loading...</div>
    if(error) return <div>Error: {error}</div>
    if(isLinked) return <div>Account successfully linked</div>

    return linkToken && (
            <Button
                onClick={() => open()}
                disabled={!ready}
                variant="contained"
                color="secondary">
                Connect to your Bank account
            </Button>
    );
}

export default PlaidLink;