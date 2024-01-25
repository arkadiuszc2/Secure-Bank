import { backendApi } from './backendApi'

const authenticationClient = backendApi('/unauthenticated')

export const authenticationApi = {
  login(username, password) {
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
    return authenticationClient.post(`/requestPartialPassLogin/${username}`)
  },

  partialPassLogin(username, password) {
    return authenticationClient.get(`/partialPassLogin`, {
      params: {
        username: `${username}`,
        password: `${password}`
      }
    })
  },

  register(username, password, fullName, surname, identificationNumber) {
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
    return authenticationClient.post(`/logout`)
  },
}