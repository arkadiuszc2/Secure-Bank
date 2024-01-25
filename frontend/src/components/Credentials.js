import './styles/Details.css'
import './styles/Contents.css'
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
        <div className="add-link">
            <Link to={"/updatePassword"}>
                <button>Update password</button>
            </Link>
        </div>
        <div className="details">
            {credentials && (
                <article>
                    <div className="details-header">Name: </div>
                    <p>{credentials[0]}</p>
                    <div className="details-header">Surname:</div>
                    <p>{credentials[1]}</p>
                    <div className="details-header">ID number:</div>
                    <p>{credentials[2]}</p>
                </article>
            )}
        </div>

    </div>

    );
}

export default Credentials;

