import './styles/Details.css'
import { useState, useEffect } from 'react';
import { bankUserApi } from '../api/BankUserApi';

const AccountInfo = () => {
    const [accountInfo, setAccountInfo] = useState();

    useEffect(() => {
        bankUserApi.getAccountInfo()
            .then((res) => {
                setAccountInfo(res.data);
            }).catch((error) => {
                console.log('Error while fetching');
            }, []);
        })

        return (
            <div>
                <div className="details">
                    {accountInfo && (
                        <article>
                            <div className="details-header">Account number: </div>
                            <p>{accountInfo.number}</p>
                            <div className="details-header">Balance:</div>
                            <p>{accountInfo.balance}</p>
                        </article>)}
                </div>
            </div>
        );
    }

export default AccountInfo;

