import logo from './logo.svg';
import './App.css';
import Header from './components/Header'
import LoginFormOLD from "./components/LoginFormOLD";
import RegistrationForm from "./components/RegistrationForm";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Home from "./components/Home";
import LoginForm from "./components/LoginForm";
import ForgotPasswordForm from "./components/ForgotPasswordForm";


function App() {
  return (
      <div className="App">
          <Router basename="/AeroBankApp">
              <Routes>
                  <Route path="/" element={<LoginFormOLD/>}/>
                  <Route path="/register" element={<RegistrationForm />}/>
                  <Route path="/home" element={<Home />}/>
                  <Route path="/forgot-password" element={<ForgotPasswordForm />}/>
              </Routes>
          </Router>
      </div>
  );
}

export default App;
