import { backendApi } from './backendApi'

const authenticationClient = backendApi('/unauthenticated')

export const authenticationApi = {
  login(username, password) {
    console.log('Login')
    return authenticationClient.post('/login', {
      params: {
        username: `${username}`,
        password: `${password}`
      },
    })
  },

  requestPartialPassLogin(username) {
    console.log('Request partial login')
    return authenticationClient.post(`requestPartialPassLogin/${username}`)
  },

  partialPassLogin(username, password) {
    console.log('partialPassLogin')
    return authenticationClient.post(`/partialPassLogin`, {
      params: {
        username: `${username}`,
        password: `${password}`
      },

    })
  },

  register(username, password, fullName, surname, identificationNumber) {
    console.log('Register')
    return authenticationClient.post('/register', {
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