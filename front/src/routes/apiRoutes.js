export default {
    post: {
        signUp: '/signUp',
        login: '/signIn',
        createClient: '/clients/create',
        createInvoice: function(clientId, userId) {
            return '/invoice/create/' + clientId + '/' + userId
        }
    },
    get: {
        checkConnected: 'checkConnected',
        getClients(userID) {
            return '/clients/findAll/' + userID
        },
        getOneClient(clientId) {
            return '/clients/find/' + clientId
        },
        getInvoicesByClient(clientId){
            return '/invoice/getCompleteByClient/' + clientId
        },
        getAllInvoices: '/invoice/findAll',
        getAllInvoicesWithClient(userId) {
            return '/invoice/findAllWithClient/' + userId
        }
    },
    delete:{
        deleteClient(clientId) {
            return '/clients/delete/' + clientId
        }
    }
}
