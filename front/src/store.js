import Vue from 'vue'
import Vuex from 'vuex'
import apiRoutes from './routes/apiRoutes.js'

Vue.use(Vuex);

export const store = new Vuex.Store({
    strict: true,
    state: {
        apiRoutes: apiRoutes,
        user: {}
    },
    getters: {
        apiRoutes: states => { return states.apiRoutes },
        user: state => {
            let _user = state.user
            if (Object.keys(state.user).length !== 0) {
                return _user
            }
        },
        isUserConnected: state => {
            return (!(typeof state.user.userID === "undefined"));
        },
        hasCheckedUser: state => { return state.hasCheckedUser },
    },
    mutations: {
        updateUser: (state, newUser) => {
            state.user = newUser;
        }
    },
    actions: {
        updateUser: (ctx, callback) => {
            Vue.http.get(ctx.getters.apiRoutes.get.checkConnected).then(response => {
                if (Object.keys(response.body).length === 0)
                    ctx.commit('updateUser', {});
                else
                    ctx.commit('updateUser', response.body);
                callback(ctx.getters.user);
            }, response => {
                console.log(response);
            });
        },
        removeUser: ctx => {
            ctx.commit('updateUser', {});
        },
        toggleCheckedUser: ctx => {
            ctx.commit('toggleCheckedUser');
        },
    }
});
