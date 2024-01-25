import { backendApi } from './backendApi'

const bankUserClient = backendApi('/authenticated/bankUser')

export const bankUserApi = {
  updatePassword (password, newPassword, repeatPassword) {
    return bankUserClient.get('/updatePassword', {
      params: {
        password:`${password}`,
        newPassword:`${newPassword}`,
        repPassword:`${repeatPassword}`
    }
    });
  },

  getCredentials () {
    return bankUserClient.get(`/showCredentials`)
  },

  getAccountInfo () {
    return bankUserClient.get('/accountInfo')
    },
}