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
import TransactionView from "./components/TransactionView";
import DashBoard from "./components/DashBoard";
import SettingsView from "./components/SettingsView";
import TransferView from "./components/TransferView";
import TransactionSummary from "./components/TransactionSummary";
import BillPayPage from "./components/BillPayPage";
import AccountSummaries from "./components/AccountSummaries";
import DepositView from "./components/DepositView";
import WithdrawView from "./components/WithdrawView";
import ProfilePage from "./components/ProfilePage";
import TransactionBuilder from "./components/TransactionBuilder";
import SupportChat from "./components/SupportChat";
import SummaryAccounts from "./components/SummaryAccounts";
import AccountHistoryPage from "./components/AccountHistoryPage";
import BillPaymentPage from "./components/BillPaymentPage";


function App() {
  return (
      <div className="App">
          <BrowserRouter basename="/AeroBankApp">
              <SupportChat />
              <Routes>
                  <Route path="/" element={<LoginFormOLD/>}/>
                  <Route path="/registration" element={<RegistrationForm />}/>
                  <Route path="/home" element={<Home />}/>
                  <Route path="/forgot-password" element={<ForgotPasswordForm />}/>
                  <Route path="/mfa/authenticate" element={<MFAuthenticate />}/>
                  <Route path="/accounts" element={<TransactionView />}/>
                  <Route path="/dashboard" element={<DashBoard />}/>
                  <Route path="/settings" element={<SettingsView />}/>
                  <Route path="/deposits" element={<DepositView />}/>
                  <Route path="/withdraws" element={<WithdrawView />}/>
                  <Route path="/transfers" element={<TransferView/>}/>
                  <Route path="/transactionAnalytics" element={<TransactionSummary />}/>
                  <Route path="/billPay" element={<BillPaymentPage />}/>
                  <Route path="/reports" element={<AccountSummaries/>}/>
                  <Route path="/profile" element={<ProfilePage />}/>
                  <Route path="/transactions" element={<TransactionBuilder />}/>
                  <Route path="/summary" element={<SummaryAccounts />}/>
                  <Route path="/accounts/:id/:year/:month" element={<AccountHistoryPage />}/>
              </Routes>
          </BrowserRouter>
      </div>
  );
}

export default App;
