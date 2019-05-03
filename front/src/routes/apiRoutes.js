export default {
    post: {
        createClient: 'http://localhost:9000/clients/create',
        createInvoice: function(clientId) {
            return 'http://localhost:9000/invoice/create/' + clientId
        }
    },
    get: {
        getClients: 'http://localhost:9000/clients/findall',
        getOneClient(clientId) {
            return 'http://localhost:9000/clients/find/' + clientId
        },
        getInvoicesByClient(clientId){
            return 'http://localhost:9000/invoice/getCompleteByClient/' + clientId
        },
        getAllInvoices: 'http://localhost:9000/invoice/findAll',
        getAllInvoicesWithClient: 'http://localhost:9000/invoice/findAllWithClient'
    },
    delete:{
        deleteClient(clientId) {
            return 'http://localhost:9000/clients/delete/' + clientId
        }
    }
}
