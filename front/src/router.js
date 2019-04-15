
import ClientTabs from './components/Client/ClientTabs.vue'
import InvoiceTabs from './components/Invoice/InvoiceTabs.vue';

export default [
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

