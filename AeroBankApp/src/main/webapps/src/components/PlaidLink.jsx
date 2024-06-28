import {useCallback, useEffect, useState} from "react";
import axios from "axios";
import {usePlaidLink} from 'react-plaid-link';
import {Button} from "@mui/material";

function PlaidLink({userID, linkToken, onSuccess})
{
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [isLinked, setIsLinked] = useState(false);

    // const onSuccess = useCallback(async (publicToken, metadata) => {
    //     setIsLoading(true);
    //     try
    //     {
    //         const response = await axios.post(`http:/localhost:8080/AeroBankApp/api/plaid/exchange_public_token`,
    //             {publicToken: publicToken, userId: userID});
    //         console.log('Exchange public token response: ', response.data);
    //         setIsLinked(true);
    //     }catch(err)
    //     {
    //         setError('Failed to exchange public token');
    //         console.error("There was an error exchanging the public token: ", err);
    //     }
    //    setIsLoading(false);
    //     console.log('Account linked successfully');
    // }, [userID]);

    const onExit = useCallback((err, metadata) => {
        if(err != null)
        {
            setError(err.display_message || 'Error linking account');
        }
    }, []);

    const config = {
        token: linkToken,
        onSuccess: async (public_token, metadata) => {
            setIsLoading(true);
            try
            {
                await onSuccess(public_token, metadata);
                setIsLinked(true);

            } catch (err)
            {
                setError('Failed to exchange public token');
                console.error('There was an error exchanging the public token: ', err);
            }
            setIsLoading(false);
        },
        onExit,
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

    return open();

}

export default PlaidLink;