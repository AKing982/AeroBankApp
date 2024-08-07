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

export default function UserList({onUserSelect})
{
    const [users, setUsers] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    const renderSkeletons = () => (
        Array.from(new Array(10)).map((_, index) => (
            <ListItem key={index} style={{ padding: '10px' }}>
                <Skeleton variant="text" width={360} height={30} />
            </ListItem>
        ))
    );

    const saveSelectedUser = (username) => {
        sessionStorage.setItem('selectedListUser', username);
    };

    const handleUserClick = (username) => {
        console.log('Selected User:', username);
        onUserSelect && onUserSelect(username);
        saveSelectedUser(username);
    };

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('http://localhost:8080/AeroBankApp/api/users/user-names-list');
                console.log("Users:", response.data);
                setUsers(response.data || []);
            } catch (error) {
                console.error('There has been a problem with your fetch operation:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchUsers();
    }, []);

    const renderRow = ({ index, style }) => (
        <ListItem
            button
            style={style}
            key={index}
            onClick={() => handleUserClick(users[index].username)}
        >
            {users[index].username}
        </ListItem>
    );

    if (isLoading) {
        return (
            <div style={{
                border: '1px solid #ccc',
                width: 'fit-content',
                borderRadius: '8px',
                overflow: 'hidden',
                boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
                backgroundColor: '#f9f9f9',
                margin: '20px',
            }}>
                <List>
                    {renderSkeletons()}
                </List>
            </div>
        );
    }

    return (
        <div style={{
            border: '1px solid #ccc',
            width: 'fit-content',
            borderRadius: '8px',
            overflow: 'hidden',
            boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
            backgroundColor: '#f2f2f2',
            margin: '20px',
        }}>
            <FixedSizeList
                height={400}
                width={360}
                itemSize={46}
                itemCount={users.length}
                overscanCount={5}
            >
                {renderRow}
            </FixedSizeList>
        </div>
    );
}