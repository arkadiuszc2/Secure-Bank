import './styles/TaskDetails.css'
import { useState, useEffect } from 'react';
import { bankUserApi } from '../api/BankUserApi';
import { Link } from 'react-router-dom';

const Credentials = () => {
    const [credentials, setCredentials] = useState();

    useEffect(() => {
        bankUserApi.getCredentials()
            .then((res) => {
                setCredentials(res.data);
            }).catch(err => alert(err.message))
    }, []);

    return (<div>
        <div className="tasks-add-link">
            <Link to={"/updatePassword"}>
                <button> Update password</button>
            </Link>
        </div>
        <div className="task-details">
            {credentials && (
                <article>
                    <div className="task-details-header">Name: </div>
                    <p>{credentials[0]}</p>
                    <div className="task-details-header">Surname:</div>
                    <p>{credentials[1]}</p>
                    <div className="task-details-header">ID number:</div>
                    <p>{credentials[2]}</p>
                </article>)}
        </div>
    </div>
    );
}

export default Credentials;

