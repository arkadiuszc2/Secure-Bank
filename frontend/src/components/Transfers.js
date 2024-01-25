import './styles/TaskList.css'
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { transferApi } from "../api/TransferApi";
import './styles/Tasks.css'

const Transfers = () => {
  const [transfers, setTransfers] = useState([])

  useEffect(() => {
    transferApi.getTransfers()
      .then((res) => {
        setTransfers(res.data)
      })
      .catch(err => alert(err.message))
  }, [])

  return (
    <div className="tasks">
      <div className="tasks-add-link">
        <Link to={"/createTransfer"}>
          <button> Send transfer</button>
        </Link>
      </div>
      <div className="task-list">
        {transfers.map(transfer => (
          <div className="task-preview">
            <div className="task-preview-content">
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