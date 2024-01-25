import { backendApi } from './backendApi'

const transferClient = backendApi('/authenticated')

export const transferApi = {
    getTransfers() {
        return transferClient.get('/transfers', {

        });
    },

    sendTransfer(destinationAccountNumber, value) {
        return transferClient.get('/sendTransfer', {
            params: {
                destAccountNum: `${destinationAccountNumber}`,
                value: `${value}`
            },

        })
    },

}