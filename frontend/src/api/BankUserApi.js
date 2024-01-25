import { backendApi } from './backendApi'

const bankUserClient = backendApi('/authenticated/bankUser')

export const bankUserApi = {
  updatePassword (password, newPassword, repeatPassword) {
    console.log('Update password')
    return bankUserClient.get('/updatePassword', {
      params: {
        password:`${password}`,
        newPassword:`${newPassword}`,
        repPassword:`${repeatPassword}`
    },
    });
  },

  getCredentials () {
    console.log('Show credentials')
    return bankUserClient.get(`/showCredentials`, {

    })
  },

  getAccountInfo () {
    console.log('Get account info')
    return bankUserClient.get('/accountInfo',{

    })
    },
}