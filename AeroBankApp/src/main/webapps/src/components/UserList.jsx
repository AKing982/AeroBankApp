import {List, ListItem, ListItemText} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import {Container} from "@mui/system";
import {FixedSizeList} from "react-window";

const usersList = [
    'AKing94',
    'BSmith23',

];


function renderRow(props)
{
    const {index, style} = props;

    return (
        <ListItem button style={style} key={index}>
            <ListItemText primary={usersList[index]} />
        </ListItem>
    );
}

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
        <FixedSizeList
            height={400}
            width={360}
            itemSize={46}
            itemCount={usersList.length}
            overscanCount={5}
        >
            {renderRow}
        </FixedSizeList>
    );
}