import { useEffect } from "react";
import { authenticationApi } from "../api/AuthenticationApi";
import { useNavigate } from "react-router-dom";

const Logout = () => {

    const navigate = useNavigate();

    useEffect(() => {
        authenticationApi.logout().catch((error) => {
            console.log('Error while fetching');
        })
        navigate("/");
    }, [navigate]);

    return ( <div>

    </div> );
}
 
export default Logout;