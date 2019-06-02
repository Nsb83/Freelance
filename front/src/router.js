
import ClientTabs from './components/Client/ClientTabs.vue';
import InvoiceTabs from './components/Invoice/InvoiceTabs.vue';
import Login from './components/Login.vue';
import SignUp from './components/SignUp.vue';

export default [
    { path: "*", redirect: '/login'},
    {
        path: '/login',
        component: Login,
        name: 'login'
    },
    {
        path: '/signup',
        component: SignUp,
        name: 'signup',
        meta: { noAuth: true },
    },
    {
        path: '/client',
        name: 'ClientTabs',
        component: ClientTabs,
        meta: { requireAuth: true },
    },
    {
        path: '/invoice',
        name: 'InvoiceTabs',
        component: InvoiceTabs,
        meta: { requireAuth: true },
    }
];

