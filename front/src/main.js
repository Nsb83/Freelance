import Vue from 'vue';
import App from './components/App.vue';
import Vuetify from 'vuetify';
import Vuex from 'vuex';
import VueRouter from 'vue-router';
import VueResource from 'vue-resource';
import { store } from './store';
import config from './config';
import router from './router';
import Moment from 'moment';
import lodash from 'lodash';
import * as VueCookies from 'vue-cookies';
import Storage from 'vue-ls';
import auth from './Utils/auth';

const options = {
    namespace: 'vuejs__', // key prefix
    name: 'ls', // name variable Vue.[ls] or this.[$ls],
    storage: 'local', // storage name session, local, memory
};

const route = new VueRouter({
    routes: router,
    linkActiveClass: 'active'
});

Vue.use(VueRouter);
Vue.use(VueCookies);
Vue.use(Storage, options)
Vue.use(lodash);
Vue.use(Vuex);
Vue.use(VueResource);
Vue.use(Moment);
Vue.use(Vuetify,{
    theme: {
        primary: "#0D47A1",
        secondary: "#B71C1C",
        accent: "#00897B",
        error: "#D50000",
        warning: "#E64A19",
        info: "#78909C",
        success: "#2E7D32"
    }
});

Vue.http.options.root = config.baseUrl();

Moment.locale('fr');
Vue.prototype.$moment = Moment;

Vue.http.options.root = config.baseUrl();

export const bus = new Vue();

Vue.http.interceptors.push(function(request, next) {
    let csrfToken = Vue.cookies.get("PLAY_CSRF_TOKEN");
    let jwt = Vue.ls.get("jwt");

    if (jwt !== null && jwt !== undefined && jwt !== "")
        request.headers.set('X-Auth-Token', jwt);
    else
        store.dispatch('removeUser');
    request.headers.set('Csrf-Token', csrfToken);
    next();
});

route.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.noAuth)) {
        next();
    } else {
        if (!store.getters.isUserConnected) {
            store.dispatch('updateUser', function () {
                auth.methods.checkAuth(to, from, next);
            })
        } else {
            if (to.name === 'login') {
                next({ name: 'client' });
            }
            else {
                auth.methods.checkAuth(to, from, next);
            }
        }
    }
});



new Vue({
    store: store,
    router: route,
    el: "#app",
    render: h => h(App),
});


import '../sass/style.scss';
import 'vuetify/dist/vuetify.min.css'
