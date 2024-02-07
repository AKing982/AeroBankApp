import {CircularProgress, List, ListItem, ListItemText} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import {Container} from "@mui/system";
import '../UserList.css';
import {FixedSizeList} from "react-window";
import {Skeleton} from "@mui/lab";

const usersList = [
    'AKing94',
    'BSmith23',

];


function renderRow({index, style, users, onUserClick})
{
    const user = users[index];

    return (
        <ListItem button
                  style={style}
                  key={index}
                  onClick={() => onUserClick(user)}
        >
            <ListItemText primary={user} />
        </ListItem>
    );
}

export default function UserList()
{
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    const renderSkeletons = () => {
        return Array.from(new Array(10)).map((_, index) => (
            <ListItem key={index} style={{ padding: '10px' }}>
                <Skeleton variant="text" width={360} height={30} />
            </ListItem>
        ));
    };

    const saveSelectedUser = (user) => {
        sessionStorage.setItem('Selected List User: ', user);
    }

    const handleUserClick = (user) => {
        console.log('Selected User: ', user);
        setSelectedUser(user);
        saveSelectedUser(user);
    }

    useEffect(() => {
        setIsLoading(true);
        const timeoutId = setTimeout(() => {
            axios.get('http://localhost:8080/AeroBankApp/api/users/user-names-list')
                .then(response => {
                    console.log("Users: ", response.data.users);
                    setUsers(response.data);
                })
                .catch(error => {
                    console.error('There has been a problem with your fetch operation: ', error);
                })
                .finally(() => {
                    setIsLoading(false);
                })
        }, 4000);

        return () => clearTimeout(timeoutId);

    }, []);

    if (isLoading) {
        return (
            <div style={{
                border: '1px solid #ccc', // Changed for a lighter border color
                width: 'fit-content',
                borderRadius: '8px', // Rounded corners
                overflow: 'hidden', // Ensures the border encompasses the list
                boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', // Adds a subtle shadow
                backgroundColor: '#f9f9f9', // Light background color for the container
                margin: '20px', // Adds margin around the component
                 }}>
                <List>
                    {renderSkeletons()}
                </List>
            </div>
        );
    }

    return (
        <div style={{
            border: '1px solid #ccc', // Changed for a lighter border color
            width: 'fit-content',
            borderRadius: '8px', // Rounded corners
            overflow: 'hidden', // Ensures the border encompasses the list
            boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', // Adds a subtle shadow
            backgroundColor: '#00000', // Light background color for the container
            margin: '20px', // Adds margin around the component
        }}>
            <FixedSizeList
                height={400}
                width={360}
                itemSize={46}
                itemCount={users.length}
                overscanCount={5}
                style={{
                    backgroundColor: '#f2f2f2', // Background color for the list
                }}
            >
                {props => renderRow({ ...props, users, onUserClick: handleUserClick })}
            </FixedSizeList>
        </div>
    );
}