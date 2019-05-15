import Vue from 'vue';
import { store } from '../store';


export default {
    methods: {
        backToMainPage(next) {
            return (next({
                path: '/client'
            }));
        },
        checkAuth: function (to, from, next) {
            if (to.name !== 'login') {
                let jwt = Vue.ls.get("jwt");
                if (jwt === null || jwt === undefined || jwt === "") {
                    store.dispatch('removeUser');
                    next({name: 'login', query: {from: to.path}});
                } else {
                    if (to.matched.some(record => record.meta.noAuth)) {
                        next();
                    } else if (!store.getters.isUserConnected) {
                        next({name: 'login', query: {from: from.path}});
                    } else {
                        let user = store.getters.user;
                        next();
                    }
                }
            } else {
                if (store.getters.isUserConnected) {
                    if (to.name === 'login') {
                        next({ name: 'client' });
                    }
                    else
                        next({ name: from.name });
                } else {
                    next();
                }
            }

        }
    }

}
