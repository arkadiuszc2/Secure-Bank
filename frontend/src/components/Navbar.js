import './styles/Navbar.css'
import { Link } from 'react-router-dom'

const Navbar = () => {


    return (
        <nav className="navbar">
            <h1>SecureBank</h1>
            <div className="links">
                <Link to="/">Home</Link>
                <Link to="/register">Register</Link>
                <Link to="/partialLogin">Login</Link>
                <Link to='/logout' >Logout</Link>
                <Link to="/account">Account</Link>
                <Link to="/transfers">Transfers</Link>
                <Link to="/credentials">Credentials</Link>
            </div>
        </nav>
    );
}

export default Navbar;