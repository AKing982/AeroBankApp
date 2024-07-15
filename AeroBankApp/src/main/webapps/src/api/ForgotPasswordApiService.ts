

export interface IUserData {
    email: string;
}


const validateUserName = async(username: string) : Promise<boolean | undefined> => {
    try
    {
        const axios = require('axios');
        const validResponse = await axios.get(`http://localhost:8080/AeroBankApp/api/users/find/${username}`)
        if(validResponse.status === 200 || validResponse.status === 201) {
            return validResponse.data.exists;
        }
    }catch(error) {
        console.error('There was an error fetching username validation: ', error);
    }
};

const generateValidationCode = async() : Promise<boolean | undefined> => {
    try {
        const axios = require('axios');
        const response = await axios.get(`http://localhost:8080/AeroBankApp/api/validationCode/generate`)
        if(response.status === 200 || response.status === 201) {
            return response.data;
        }
    }catch(error){
        console.error('There was an error fetching the generated validation code: ', error);
    }
};

const fetchUsersEmail = async(username: string) : Promise<string | undefined> => {
    try{
        const axios = getAxios();
        const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/email/${username}`)
        if(response.status === 200 || response.status === 201){
            return response.data.email;
        }
    }catch(error){
        console.error("There was an error fetching the user's email: ", error);
    }
};

const buildPasswordResetRequest = (newPassword: string, username: string) => {
    return {
        password: newPassword,
        user: username
    };
};


const sendPasswordResetToServer = async(newPassword: string, user: string) : Promise<boolean> => {

    const request : {password: string, user: string} = buildPasswordResetRequest(newPassword, user);
    try{
        const axios = getAxios();
        const response = await axios.put(`http://localhost:8080/AeroBankApp/api/users/update-password`, request);
        if(response.status === 200 || response.status === 201){
            return true;
        }else{
            console.log('Failed to send password reset request to the server, status: ', response.status);
            return false;
        }
    }catch(error){
        console.error('There was an error sending the password reset: ', error);
        return false;
    }
};

const getAxios = () => {
    return require('axios');
}

const sendValidationCodeToEmail = async(code: string, email: string) : Promise<boolean> => {
    try{
        const axios = getAxios();
        const response = await axios.post(`http://localhost:8080/AeroBankApp/api/validationCode/send-verification-email`, {
            email: email, // Email address to which the verification code is sent
            code: code   // Verification code to include in the email
        });

        if (response.status === 200) {
            console.log(`Verification code sent successfully to ${email}` );
            return true; // Indicates successful sending
        } else {
            console.log(`Failed to send verification code, status: ${response.status}`);
            return false; // Indicates failure
        }
    }catch(error){
        console.error(`Error occurred while sending the verification code: ${error}`);
        return false; // Handle errors and indicate failure
    }
};




