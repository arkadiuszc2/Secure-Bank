import { useNavigate} from 'react-router-dom';
import { authenticationApi} from '../api/AuthenticationApi';
import './styles/Form.css'
import { useState} from 'react';

const Register = () => {
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const [password, setPassword] = useState();
    const [username, setUsername] = useState();
    const [fullName, setFullName] = useState();
    const [surname, setSurname] = useState();
    const [id, setId] = useState();

    const handleSubmit = async (e) => {
        e.preventDefault();
        await authenticationApi.register(username, password, fullName, surname, id).catch(err => alert(err.message));
        setIsPending(true);
        navigate('/login');

    }

    const handleChange = (event) => {
        const target = event.target
        const value = target.value
        const name = target.name
        
        if (name === 'username' && (/^[\x21-\x7E]*$/.test(value) && value.length < 100)) {
            setUsername(value);
        } else if (name === 'password' && (/^[\x21-\x7E]*$/.test(value) && value.length < 100)) {
            setPassword(value)
        } else if (name === 'fullname' && (/^[a-zA-Z]*$/.test(value) && value.length < 50)) {
            setFullName(value)
        } else if (name === 'surname' && (/^[a-zA-Z]*$/.test(value) && value.length < 50)) {
            setSurname(value)
        } else if (name === 'id' && (/^\d*$/.test(value) && value.length < 50)) {
            setId(value)
        }
    }

    const buttonTitle = "Register"

    return (
        <div className="Form">
            <h2>Register</h2>
            <form onSubmit={handleSubmit}>
                <label>Username: </label>
                <input
                    type="text"
                    required
                    value={username || ''}
                    name='username'
                    onChange={handleChange}
                />
                <label>Password: </label>
                <input
                type="text"
                    required
                    value={password || ''}
                    name='password'
                    onChange={handleChange}
                />
                <label>Full name: </label>
                <input
                    type="text"
                    required
                    value={fullName || ''}
                    name='fullname'
                    onChange={handleChange}
                />
                <label>Surname: </label>
                <input
                    type="text"
                    required
                    value={surname || ''}
                    name='surname'
                    onChange={handleChange}
                />
                <label> ID number: </label>
                <input
                    type="text"
                    required
                    value={id || ''}
                    name='id'
                    onChange={handleChange}

                />
                {!isPending && <button>{buttonTitle}</button>}
                {isPending && <button disabled>{buttonTitle}</button>}
            </form>
        </div>
    );
}
 
export default Register;