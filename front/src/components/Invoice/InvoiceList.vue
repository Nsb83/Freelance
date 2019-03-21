<template>
    <v-content>
        <v-data-table
                :headers="headers"
                :items="invoices"
                class="elevation-1 text-xs-center"
                hide-actions
        >

            <template v-slot:items="props">
                <td class="text-center">{{ props.item.number }}</td>
                <td class="text-center">{{ formatDateTime(props.item.date) }}</td>
                <!--<td class="text-center">{{ props.item.service }}</td>-->
            </template>
        </v-data-table>
    </v-content>

</template>

<script>
    import { bus } from '../../main';

    export default {
        name: "InvoiceList",
        created: function() {
            this.getAllInvoices();

            bus.$on('getAllInvoice', this.getAllInvoices);
        },
        beforeDestroy: function() {
            bus.$off('getAllInvoice');
        },
        computed: {
            apiRoutes(){
                return this.$store.state.apiRoutes
            }
        },
        data() {
            return {
                headers: [
                    { text: 'Numéro de facture', value: 'number', align:'center'},
                    { text: 'date', value: 'date', align:'center' },
                    // { text: 'Prestation', value: 'services', align: 'center'}
                         ],
                invoices: [],
                services: [],
            }
        },
        watch:{
            apiRoutes(){
                return this.$store.state.apiRoutes
            }
        },
        methods: {
            getAllInvoices: function() {
                this.$http.get(this.apiRoutes.get.getAllInvoices).then(
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
