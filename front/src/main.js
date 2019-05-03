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

Vue.use(VueRouter);
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

Moment.locale('fr');
Vue.prototype.$moment = Moment;

Vue.http.options.root = config.baseUrl();

export const bus = new Vue();

const route = new VueRouter({
    routes: router,
});

new Vue({
    store: store,
    router: route,
    el: "#app",
    render: h => h(App),
});


import '../sass/style.scss';
import 'vuetify/dist/vuetify.min.css'
