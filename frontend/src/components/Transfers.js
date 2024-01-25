import './styles/ContentList.css'
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { transferApi } from "../api/TransferApi";
import './styles/Contents.css'

const Transfers = () => {
  const [transfers, setTransfers] = useState([])

  useEffect(() => {
    transferApi.getTransfers()
      .then((res) => {
        setTransfers(res.data)
      }).catch((error) => {
        console.log('Error while fetching');
    })
  }, [])

  return (
    <div className="contents">
      <div className="add-link">
        <Link to={"/createTransfer"}>
          <button> Send transfer</button>
        </Link>
      </div>
      <div className="list">
        {transfers.map(transfer => (
          <div className="preview">
            <div className="preview-content">
              <h2>{transfer.value}</h2>
              <p>From: {transfer.toAccountNumber}</p>
              <p>Date: {transfer.date}</p>
            </div>
          </div>
        ))}
      </div>
      </div>
        );

}

        export default Transfers;