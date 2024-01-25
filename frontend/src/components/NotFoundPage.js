import { Link } from "react-router-dom";
import './styles/NotFound.css'
const NotFoundPage = () => {
    return ( 
        <div className="not-found-page">
            <h2>Sorry</h2>
            <p>Page cannot be found</p>
            <Link to="/">Go back to homepage...</Link>
        </div>
    );
}
 
export default NotFoundPage;