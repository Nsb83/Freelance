
import ClientTabs from './components/Client/ClientTabs.vue'
import InvoiceList from "./components/Invoice/InvoiceList.vue";

export default [
    {
        path: '/',
        name: 'ClientTabs',
        component: ClientTabs
    },
    {
        path: '/invoiceList',
        name: 'InvoiceList',
        component: InvoiceList

    }


];

