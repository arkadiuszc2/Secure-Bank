import { bankUserApi } from "../api/BankUserApi";
import { useNavigate } from "react-router-dom";
import './styles/TaskFrom.css'

const updatePassword = () => {
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const [password, setPassword] = useState();
    const [newPassword, setNewPassword] = useState();
    const [repeatedNewPassword, setRepeatedNewPassword] = useState();


    const handleSubmit = async (e) => {
        e.preventDefault();
        await bankUserApi.updatePassword(password, newPassword, repeatedNewPassword).catch(err => alert(err.message));
        setIsPending(true);
        navigate('/credentials');

    }

    const handleChange = (event) => {
        const target = event.target
        const value = target.value
        const name = target.name
        
        if (name === 'password' && (/^[\x21-\x7E]*$/.test(value) && value.length < 100)) {
            setPassword(value);
        } else if (name === 'newPassword' && (/^[\x21-\x7E]*$/.test(value) && value.length < 100)) {
            setNewPassword(value)
        }  else if (name === 'repeatedNewPassword' && (/^[\x21-\x7E]*$/.test(value) && value.length < 100)) {
            setRepeatedNewPassword(value)
        }
    }

    const buttonTitle = "Change password"

    return (
        <div className="TaskForm">
            <h2>Change your password</h2>
            <form onSubmit={handleSubmit}>
                <label>Current assword: </label>
                <input
                    type="text"
                    required
                    value={password || ''}
                    name='password'
                    onChange={handleChange}
                />
                <label>New password: </label>
                <input
                type="text"
                    required
                    value={newPassword || ''}
                    name='newPassword'
                    onChange={handleChange}
                />
                <label>Repeat new password: </label>
                <input
                    type="text"
                    required
                    value={repeatedNewPassword || ''}
                    name='repeatedNewPassword'
                    onChange={handleChange}
                />
                {!isPending && <button>{buttonTitle}</button>}
                {isPending && <button disabled>{buttonTitle}</button>}
            </form>
        </div>
    );
}
 
export default updatePassword;