<template>
    <v-content class="pa-0">
        <v-data-table
                :headers="headers"
                :items="invoices"
                class="elevation-1 text-xs-center"
                hide-actions
        >

            <template v-slot:items="props">
                <td class="text-center">{{ props.item.number }}</td>
                <td class="text-center">{{ props.item.client }}</td>
                <td class="text-center">{{ formatDateTime(props.item.date) }}</td>
                <td class="text-center">{{ props.item.totalTTC }} €</td>
                <v-btn slot="activator" icon class="mx-0" :href="apiRoutes.get.exportInvoiceToPDF(props.item.publicId)">
                    <v-icon color="secondary">delete</v-icon>
                </v-btn>
            </template>
        </v-data-table>
    </v-content>

</template>

<script>
    import { bus } from '../../main';
    import { mapGetters } from 'vuex';

    export default {
        name: "InvoiceList",
        created: function() {
            this.getAllInvoices();
            bus.$on('getAllInvoices', this.getAllInvoices);
        },
        beforeDestroy: function() {
            bus.$off('getAllInvoices');
        },
        computed: {
            ...mapGetters([ 'apiRoutes', 'user'])
        },
        data() {
            return {
                headers: [
                    { text: 'Numéro de facture', value: 'number', align: 'center'},
                    { text: 'Client', value: 'client', align: 'center' },
                    { text: 'date', value: 'date', align:'center' },
                    { text: 'Montant total TTC', value: 'totalTTC', align: 'center'}
                         ],
                invoices: [],
                services: [],
            }
        },
        methods: {
            getAllInvoices: function() {
                this.$http.get(this.apiRoutes.get.getAllInvoicesWithClient(this.user.userID)).then(
                    response => {
                        this.invoices = response.body;
                        console.log(this.invoices)
                    },
                    response => {
                        console.log(response)
                    }
                )
            },
            formatDateTime(date) {
                const moment = this.$moment(date);
                return moment.format('LL')
            }

        }
    }
</script>

<style scoped>

</style>
