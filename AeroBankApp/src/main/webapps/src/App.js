import logo from './logo.svg';
import './App.css';
import Header from './components/Header'
import LoginFormOLD from "./components/LoginFormOLD";
import RegistrationForm from "./components/RegistrationForm";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Home from "./components/Home";
import LoginForm from "./components/LoginForm";
import ForgotPasswordForm from "./components/ForgotPasswordForm";
import MFAuthenticate from "./components/MFAuthenticate";
import TwoFactorAuthPage from "./components/TwoFactorAuthPage";


function App() {
  return (
      <div className="App">
          <Router basename="/AeroBankApp">
              <Routes>
                  <Route path="/" element={<LoginFormOLD/>}/>
                  <Route path="/registration" element={<RegistrationForm />}/>
                  <Route path="/home" element={<Home />}/>
                  <Route path="/forgot-password" element={<ForgotPasswordForm />}/>
                  <Route path="/mfa/authenticate" element={<TwoFactorAuthPage />}/>
              </Routes>
          </Router>
      </div>
  );
}

export default App;
