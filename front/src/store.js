import Vue from 'vue'
import Vuex from 'vuex'
import apiRoutes from './routes/apiRoutes.js'

Vue.use(Vuex);

export const store = new Vuex.Store({
    state: {
        apiRoutes: apiRoutes,
        // getAllClient: getAllClient,
    },
    getters: {
        apiRoutes: states => { return states.apiRoutes },
    },
});
