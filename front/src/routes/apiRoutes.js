export default {
    post: {
        signUp: '/signUp',
        login: '/signIn',
        createBank: function(userId) {
            return '/bank/create/' + userId
        },
        createClient: '/clients/create',
        createInvoice: function(clientId, userId) {
            return '/invoice/create/' + clientId + '/' + userId
        },
        updateUser: '/user/update',
    },
    get: {
        checkConnected: 'checkConnected',
        getClients(userID) {
            return '/clients/findAll/' + userID
        },
        getOneClient(clientId) {
            return '/clients/find/' + clientId
        },
        getAllInvoicesWithClient(userId) {
            return '/invoice/findAllWithClient/' + userId
        },
        exportInvoiceToPDF: function (publicId) {
            return '/invoice/exportPDF/invoice.pdf?publicId=' + publicId
        },
        getBank: function (userId) {
            return '/bank/find/' + userId
        }

    },
    delete:{
        deleteClient(clientId) {
            return '/clients/delete/' + clientId
        }
    }
}
