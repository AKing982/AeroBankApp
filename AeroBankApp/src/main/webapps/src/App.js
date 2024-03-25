import logo from './logo.svg';
import './App.css';
import Header from './components/Header'
import LoginFormOLD from "./components/LoginFormOLD";
import RegistrationForm from "./components/RegistrationForm";
import {BrowserRouter, Route, Router, Routes} from "react-router-dom";
import Home from "./components/Home";
import LoginForm from "./components/LoginForm";
import ForgotPasswordForm from "./components/ForgotPasswordForm";
import MFAuthenticate from "./components/MFAuthenticate";


function App() {
  return (
      <div className="App">
          <BrowserRouter basename="/AeroBankApp">
              <Routes>
                  <Route path="/" element={<LoginFormOLD/>}/>
                  <Route path="/registration" element={<RegistrationForm />}/>
                  <Route path="/home" element={<Home />}/>
                  <Route path="/forgot-password" element={<ForgotPasswordForm />}/>
                  <Route path="/mfa/authenticate" element={<MFAuthenticate />}/>
              </Routes>
          </BrowserRouter>
      </div>
  );
}

export default App;
