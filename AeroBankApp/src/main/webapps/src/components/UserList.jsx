import {List, ListItem, ListItemText} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";

export default function UserList()
{
    const [users, setUsers] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/AeroBankApp/api/users/list')
            .then(response => {
                setUsers(response.data);
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation: ', error);
            })
    }, []);

    return (
        <List>
            {users.map((user, index) => (
                <ListItem key={index} divider>
                    <ListItemText primary={user.username}/>
                </ListItem>
            ))}
        </List>
    )
}