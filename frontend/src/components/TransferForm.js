import { useNavigate} from 'react-router-dom';
import { transferApi } from '../api/TransferApi';
import './styles/Form.css'
import { useState} from 'react';
const TransferForm = () => {

    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();

    const [transfer, setTransfer] = useState({
        destinationAccountNumber: '',
        value: '',
    })

    const handleSubmit = async (e) => {
        e.preventDefault();
        transferApi.sendTransfer(transfer.destinationAccountNumber, transfer.value).catch((error) => {
            console.log('Error while fetching');
        });
        setIsPending(true);
        navigate('/transfers');

    }

    const handleChange = (event) => {
        const target = event.target
        const value = target.value
        const name = target.name

        if (name === 'value' && (!/^\d*\.?\d*$/.test(value) || value.length > 13)) {
            return; 
        } else if (name === 'destinationAccountNumber' && (!/^\d*$/.test(value) || value.length > 22)) {
            return; 
        }



        setTransfer({ ...transfer, [name]: value })
    }

    const pageTitle = <h2>Create transfer</h2>
    const buttonTitle = <p>Send</p>

    return (
        <div className="Form">
            {pageTitle}
            <form onSubmit={handleSubmit}>
                <label>Destination account number </label>
                <input
                    type="text"
                    required
                    value={transfer.destinationAccountNumber || ''}
                    name='destinationAccountNumber'
                    onChange={handleChange}
                />
                <label>Value: </label>
                <input
                    required
                    value={transfer.value || ''}
                    name='value'
                    onChange={handleChange}
                />
                {!isPending && <button>{buttonTitle}</button>}
                {isPending && <button disabled>{buttonTitle}</button>}
            </form>
        </div>
    );
}

export default TransferForm;