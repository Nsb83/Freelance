export default {
    post: {
        createClient: 'http://localhost:9000/clients/create',
        createInvoice: function(clientId) {
            return 'http://localhost:9000//invoice/create/' + clientId
        }
    },
    get: {
        getClients: 'http://localhost:9000/clients/findall',
        getInvoicesByClient(clientId){
            return 'http://localhost:9000/invoice/getCompleteByClient/' + clientId
        },
        getAllInvoices: 'http://localhost:9000/invoice/findAll'
    },
    delete:{
        deleteClient(clientId) {
            return 'http://localhost:9000/clients/delete/' + clientId
        }
    }
}
