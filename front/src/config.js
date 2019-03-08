export default {
    baseUrl: function () {
        if (process.env.NODE_ENV === 'production')
            return ('http://localhost:9000/');
        else
            return ('http://localhost:9000/');
    }
}
