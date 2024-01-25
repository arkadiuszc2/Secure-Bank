import { backendApi } from './backendApi'

const transferClient = backendApi('/authenticated')

export const transferApi = {
    getTransfers() {
        console.log('Get transfers')
        return transferClient.get('/transfers', {

        });
    },

    sendTransfer(destinationAccountNumber, value) {
        console.log('Send transfer')
        return transferClient.get('/sendTransfer', {
            params: {
                destAccountNum: `${destinationAccountNumber}`,
                value: `${value}`
            },

        })
    },

}