
import ClientTabs from './components/Client/ClientTabs.vue'
import InvoiceTabs from './components/Invoice/InvoiceTabs.vue';
import Login from './components/Login.vue'

export default [
    { path: "*", redirect: '/invoice'},
    {
        path: '/login',
        component: Login,
        name: 'login'
    },
    // {
    //     path: '/signup',
    //     component: SignUp,
    //     name: 'signup'
    // },
    {
        path: '/',
        name: 'ClientTabs',
        component: ClientTabs
    },
    {
        path: '/invoice',
        name: 'InvoiceTabs',
        component: InvoiceTabs
    }
];

