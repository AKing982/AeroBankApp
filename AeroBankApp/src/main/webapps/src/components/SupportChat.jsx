import {Button, IconButton, List, ListItem, ListItemText, Paper, TextField} from "@mui/material";
import {Box} from "@mui/system";
import ChatIcon from '@mui/icons-material/Chat'; // Import chat icon
import {useState} from "react";

function SupportChat(){
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const [isOpen, setIsOpen] = useState(false);

    const handleSend = () => {
        if(input.trim()){
            setMessages([...messages, input]);
            setInput("");
        }
    }

    return (
        <div style={{ position: 'fixed', bottom: 20, left: 20, zIndex: 1000 }}>
            <Paper elevation={4} style={{ display: 'flex', color:'#e555', justifyContent: 'center', alignItems: 'center', borderRadius: '50%', width: 56, height: 56 }}>
                <IconButton onClick={() => setIsOpen(!isOpen)} color="primary" size="large">
                    <ChatIcon />
                </IconButton>
            </Paper>

            {isOpen && (
                <Paper sx={{
                    width: 300,
                    bgcolor: 'rgba(255, 255, 255, 0.85)', // Adjust transparency here
                    p: 2,
                    mt: -4,
                    position: 'absolute',
                    bottom: 10,
                    left: 70
                }}>
                    <List style={{ maxHeight: 240, overflow: 'auto' }}>
                        {messages.map((msg, index) => (
                            <ListItem key={index}>
                                <ListItemText primary={msg} />
                            </ListItem>
                        ))}
                    </List>
                    <TextField
                        variant="outlined"
                        fullWidth
                        placeholder="Type a message..."
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' ? handleSend() : null}
                        margin="dense"
                    />
                    <Button variant="contained" color="primary" onClick={handleSend} fullWidth>
                        Send
                    </Button>
                </Paper>
            )}
        </div>
    );
}

export default SupportChat;