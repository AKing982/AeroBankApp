import logo from './logo.svg';
import './App.css';
import {useState} from 'react';

export default function App()
{
    return (
        <Toolbar
            onPlayMovie={() => alert('Playing')}
            onUploadImage={() => alert('Uploading')}
            />
    );
}

function Toolbar({onPlayMovie, onUploadImage})
{
    return (
        <div>
            <Button onClick={onPlayMovie}>
                Play Movie
            </Button>
        </div>
    )
}


export default App;
