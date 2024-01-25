import './styles/TaskDetails.css'
import { useState, useEffect } from 'react';
import { bankUserApi } from '../api/BankUserApi';

const AccountInfo = () => {
    const [accountInfo, setAccountInfo] = useState();

    useEffect(() => {
        bankUserApi.getAccountInfo()
            .then((res) => {
                setAccountInfo(res.data);
            }).catch(err => alert(err.message))
    }, []);

    return (
        <div>
            <div className="task-details">
                {accountInfo && (
                    <article>
                        <div className="task-details-header">Account number: </div>
                        <p>{accountInfo.number}</p>
                        <div className="task-details-header">Balance:</div>
                        <p>{accountInfo.balance}</p>
                    </article>)}
            </div>
        </div>
    );
}

export default AccountInfo;

