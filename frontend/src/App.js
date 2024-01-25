import Navbar from './components/Navbar';
import AccountInfo from './components/AccountInfo';
import TransferForm from './components/TransferForm';
import { Route, Routes } from 'react-router-dom';
import NotFoundPage from './components/NotFoundPage';
import Transfers from './components/Transfers';
import Credentials from './components/Credentials';
import PartialLogin from './components/PartialLogin';
import Register from './components/Register';
import Logout from './components/Logout';
import Home from './components/Home';
import UpdatePassword from './components/UpdatePassword';

function App() {
  return (
    <div className="App">
      <Navbar />
      <div className="content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/transfers" element={<Transfers />} />
          <Route path="/credentials" element={<Credentials />} />
          <Route path="/createTransfer" element={<TransferForm />} />
          <Route path="/account" element={<AccountInfo />} />
          <Route path="/partialLogin" element={<PartialLogin />} />
          <Route path="/register" element={<Register />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="/updatePassword" element={<UpdatePassword />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
