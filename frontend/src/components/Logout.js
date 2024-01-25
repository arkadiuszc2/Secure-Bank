import { useEffect } from "react";
import { authenticationApi } from "../api/AuthenticationApi";
import { useNavigate } from "react-router-dom";

const Logout = () => {

    const navigate = useNavigate();

    useEffect(() => {
        authenticationApi.logout().catch(err => alert(err.message))
        navigate("/");
    }, [navigate]);

    return ( <div>

    </div> );
}
 
export default Logout;