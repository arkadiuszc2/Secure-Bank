import { useState } from "react";
import { authenticationApi } from "../api/AuthenticationApi";
import "./styles/Details.css"
import { useNavigate } from "react-router-dom";

const PartialLogin = () => {
    const [charNumbers, setCharNumbers] = useState();
    const [isPendingUsername, setIsPendingUsername] = useState(false);
    const [isPendingPassword, setIsPendingPassword] = useState(false);
    const [password, setPassword] = useState();
    const [username, setUsername] = useState();
    const navigate = useNavigate();

    const handleSubmitUsername = async (e) => {
        e.preventDefault();
        authenticationApi.requestPartialPassLogin(username).catch((error) => {
            console.log('Error while fetching');
        })
        .then((res) => {
            setCharNumbers(res.data);
        })
        setIsPendingUsername(true);

    }

    const handleSubmitPassword = async (e) => {
        e.preventDefault();
        authenticationApi.partialPassLogin(username, password).catch((error) => {
            console.log('Error while fetching');
        })
        setIsPendingPassword(true);
        navigate('/home');

    }

    const handleChange = (event) => {
        const target = event.target
        const value = target.value
        const name = target.name

        if (name === 'username' && (/^[\x21-\x7E]*$/.test(value) && value.length < 100)) {
            setUsername(value);
        } else if (name === 'password' && (/^[\x21-\x7E]*$/.test(value) || value.length < 100)) {
            setPassword(value)
        }
    }

    const buttonTitleUsername="Generate character positions";
    const buttonTitlePassword="Sign in";

    return (
        <div className="details">
            <h2>Login</h2>
            <div className="Form">
            <form onSubmit={handleSubmitUsername}>
                    <label>Username: </label>
                    <input
                        type="text"
                        required
                        value={username || ''}
                        name='username'
                        onChange={handleChange}
                    />
                    {!isPendingUsername && <button>{buttonTitleUsername}</button>}
                    {isPendingUsername && <button disabled>{buttonTitleUsername}</button>}
                </form>
                {charNumbers && (
                <article>
                    <div className="details-header">Randomly selected character positions from your password: </div>
                    <p>{charNumbers}</p>
                </article>)}
                <form onSubmit={handleSubmitPassword}>
                    <label>Enter characters from the appropriate positions in the password:</label>
                    <input
                        type="text"
                        required
                        value={password || ''}
                        name='password'
                        onChange={handleChange}
                    />
                    {!isPendingPassword && <button>{buttonTitlePassword}</button>}
                    {isPendingPassword && <button disabled>{buttonTitlePassword}</button>}
                </form>
            </div>
        </div>
    );
}

export default PartialLogin;