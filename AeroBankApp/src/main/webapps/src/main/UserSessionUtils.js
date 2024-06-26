import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function UserSessionUtils()
{
    const [currentUserLog, setCurrentUserLog] = useState({});
    const [duration, setDuration] = useState(0);
    const [userIsActive, setUserIsActive] = useState(false);
    const navigate = useNavigate();

    async function fetchCurrentUserLogSession(){
        const userID = sessionStorage.getItem('userID');
        console.log('UserID: ', userID);
        return axios.get(`http://localhost:8080/AeroBankApp/api/session/currentSession/${userID}`)
            .then(response => {
                const userLogSession = response.data;
                console.log('Successfully fetched user log id');
                setCurrentUserLog(userLogSession);
                console.log('User Log session: ', response.data);
                return userLogSession;
            })
            .catch(error => {
                console.error('There was an error fetching the current user log id: ', error);
            });
    }

    async function updateUserLogRequest(userID, duration, isActive, loginTime, lastLogout)
    {

        try {
            const session = await fetchCurrentUserLogSession();
            if (!session || !session.id) {
                console.error('Failed to fetch current user log session or session ID not found.');
                return; // Exit function if no session or session ID is found
            }

            const sessionID = session.id;


            console.log('sessionID: ', sessionID);
            console.log('Session: ', session);
            console.log("Type of SessionID: ", typeof sessionID);

            const userLogData = {
                id: sessionID,
                userID: userID,
                lastLogin: loginTime,
                lastLogout: lastLogout,
                sessionDuration: duration,
                isActive: isActive
            }

            console.log('UserLog Sign Out Request: ', userLogData);

            const response = await axios.put(`http://localhost:8080/AeroBankApp/api/session/updateUserLog/${sessionID}`, userLogData);
            console.log('Response Status was ok: ', response.data.ok);
            if (response.status === 200) {
                console.log('User Log Data Successfully posted...', response.data);
            } else {
                console.log(`There was an error updating the User Log: Status ${response.status}`, response.data);
            }
        } catch (error) {
            if(error.response){
                console.error(`Server responded with status ${error.response.status}: `, error.response.data);
            }else if(error.request){
                console.error('No Response received for the update request.', error.request);
            }else{
                console.error('Error setting up the update request: ', error.message);
            }
        }
    }

    const fetchLogout = async () => {
        try{
            return await fetch(`http://localhost:8080/AeroBankApp/api/auth/logout`,  {
                method: 'POST', // Specify the method explicitly
            });
        }catch(error){
            console.error(`Error during logout: `);
            return false;
        }
    }

    const handleLogout = async () => {
        const logoutTime = new Date().getTime();
        const lastLoginTime = sessionStorage.getItem('currentLoginTime');
        const loginTime = sessionStorage.getItem('loginTime');
        const loginISO = sessionStorage.getItem('loginISOTime');
        const duration = logoutTime - loginTime;
        const duration_in_seconds = Math.floor(duration / 1000);
        console.log('Duration Logged In: ', duration);
        setDuration(duration_in_seconds);
        const userID = sessionStorage.getItem('userID');
        const currentTime = new Date();
        setUserIsActive(0);

        updateUserLogRequest(userID, duration_in_seconds, 0, lastLoginTime, currentTime.toLocaleString());
        sessionStorage.removeItem('loginTime');
        sessionStorage.clear();

        const logoutResponse =  await fetchLogout();
        console.log('Logout Response: ', logoutResponse);
        if(logoutResponse.status === 200){
            console.log('User logged out successfully');
            navigate('/');
        }
    }

    return {
        currentUserLog,
        duration,
        userIsActive,
        fetchCurrentUserLogSession,
        updateUserLogRequest,
        fetchLogout,
        handleLogout
    };
}

export default UserSessionUtils;