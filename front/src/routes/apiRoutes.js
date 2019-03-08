export default {
    post: {
        createClient: 'http://localhost:9000/clients/create'
    },
    get: {
        getClients: 'http://localhost:9000/clients/findall'
    },
    delete:{
        deleteClient(clientId) {
            return 'http://localhost:9000/clients/delete/' + clientId
    }
    }
}
