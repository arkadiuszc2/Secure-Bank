import { useState } from 'react';
import './styles/Home.css'

const Home = () => {
    const title = 'Welcome to SecureBank';
    const description = 'Your money is safe with us!';

    const [reaction, setReaction] = useState('Are you satisfied with our services?')

    const handleClick = (e) => {
        setReaction('We are happy to hear that!');
    }

    return ( 
        <div className="home">
            <h1>{ title }</h1>
            <p>{ description }</p>
            <br />
            <p>{ reaction }</p>
            <button onClick={(e) => handleClick(e)}>Yes</button>
        </div>
     );
}
 
export default Home;