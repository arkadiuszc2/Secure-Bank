import { backendApi } from './backendApi'

const authenticationClient = backendApi('/unauthenticated')

export const authenticationApi = {
  login(username, password) {
    console.log('Login')
    return authenticationClient.get('/login', {
      params: {
        username: `${username}`,
        password: `${password}`
      },
      headers: {
        'Content-Type': 'application/json'
      } 
    })
  },

  requestPartialPassLogin(username) {
    console.log('Request partial login')
    return authenticationClient.post(`/requestPartialPassLogin/${username}`)
  },

  partialPassLogin(username, password) {
    console.log('partialPassLogin')
    return authenticationClient.get(`/partialPassLogin`, {
      params: {
        username: `${username}`,
        password: `${password}`
      }, withCredentials: true
    })
  },

  register(username, password, fullName, surname, identificationNumber) {
    console.log('Register')
    return authenticationClient.get('/register', {
      params: {
        username: `${username}`,
        password: `${password}`,
        fullName: `${fullName}`,
        surname: `${surname}`,
        identificationNumber: `${identificationNumber}`
      }
    })
  },

  logout() {
    console.log('logout')
    return authenticationClient.post(`/logout`, {

    })
  },
}