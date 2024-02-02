import {List, ListItem, ListItemText} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import {Container} from "@mui/system";
import '../UserList.css';
import {FixedSizeList} from "react-window";

const usersList = [
    'AKing94',
    'BSmith23',

];


function renderRow(props)
{
    const {index, style, users, onUserClick} = props;

    return (
        <ListItem button
                  style={style}
                  key={index}
                  onClick={() => onUserClick(users[index])}
        >
            <ListItemText primary={users[index]} />
        </ListItem>
    );
}

export default function UserList()
{
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);

    const saveSelectedUser = (user) => {
        sessionStorage.setItem('Selected List User: ', user);
    }

    const handleUserClick = (user) => {
        console.log('Selected User: ', user);
        setSelectedUser(user);
        saveSelectedUser(user);
    }

    useEffect(() => {
        axios.get('http://localhost:8080/AeroBankApp/api/users/user-names-list')
            .then(response => {
                console.log("Users: ", response.data.users);
                setUsers(response.data);
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation: ', error);
            })
    }, []);


    return (
        <div style={{ border: '1px solid black', width: 'fit-content' }}>
            <FixedSizeList
                height={400}
                width={360}
                itemSize={46}
                itemCount={users.length}
                overscanCount={5}
            >
                {props => renderRow({ ...props, users, onUserClick: handleUserClick, setSelectedUser })}
            </FixedSizeList>
        </div>
    );
}