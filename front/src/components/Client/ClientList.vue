<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-content class="pa-0">
    <v-data-table
            :headers="headers"
            :items="clients"
            class="elevation-1 text-xs-center"
            hide-actions
    >
        <template v-slot:items="props">
            <td class="text-center">{{ props.item.companyName }}</td>
            <td class="text-center">{{ props.item.referentFirstName }} {{ props.item.referentLastName }}</td>
            <td class="text-center">{{ props.item.email }}</td>
            <td class="text-center">{{ props.item.phoneNumber }}</td>
            <td class="text-center" v-if="props.item.isActive === true">Actif</td>
            <td class="text-center secondary--text" v-else>Inactif</td>

            <v-btn slot="activator" icon class="mx-0" @click.native="deleteClient(props.item.id)">
                <v-icon color="secondary">delete</v-icon>
            </v-btn>
<!--            <v-btn slot="activator" icon class="mx-0" @click.native="getAllInvoiceForClient(props.item.id)">-->
<!--                <v-icon color="primary">receipt</v-icon>-->
<!--            </v-btn>-->
        </template>
    </v-data-table>

<!--    <v-data-table-->
<!--            :headers="headers"-->
<!--            :items="clientInvoices"-->
<!--            class="elevation-1 text-xs-center"-->
<!--            hide-actions-->
<!--    >-->
<!--        <template v-slot:items="props">-->
<!--            {{ props.item }}-->
<!--            <td class="text-center">{{ props.item.clientId }}</td>-->
<!--            <td class="text-center">{{ props.item.date }} </td>-->
<!--            <td class="text-center">{{ props.item.number }}</td>-->
<!--            <td class="text-center">{{ props.item.services }}</td>-->
<!--            -->

<!--        </template>-->
<!--    </v-data-table>-->
    </v-content>
</template>

<script>
    import { store } from 'vuex';
    import { bus } from '../../main';
    import { mapGetters } from 'vuex';

    export default {
        name: "ClientList",
        created: function() {
            this.getAllClients();
            bus.$on('getAllClients', this.getAllClients);
        },
        beforeDestroy: function() {
            bus.$off('getAllClients');
        },
        computed: {
            ...mapGetters([ 'apiRoutes', 'user' ])
        },
        data() {
            return {
                headers: [
                    { text: 'Nom de l\'entreprise', value: 'companyName', align:'center'},
                    { text: 'Contact', value: 'referentName', align:'center' },
                    // { text: 'Numéro et rue', value: 'adress', align:'center' },
                    // { text: 'Code Postal', value: 'postalCode', align:'center' },
                    // { text: 'Ville', value: 'city', align:'center' },
                    { text: 'Email', value: 'email', align:'center' },
                    { text: 'Numéro de téléphone', value: 'phoneNumber', align:'center' },
                    // { text: 'Numéro de TVA', value: 'VATNumber', align:'center' },
                    { text: 'Est actif', value: 'isActive', align:'center' },
                ],
                clients: [],
                clientInvoices: [],
                isActive: null,
            }
        },
        watch:{
            apiRoutes(){
                return this.$store.state.apiRoutes
            }
        },
        methods: {
            getAllClients() {
                this.$http.get(this.apiRoutes.get.getClients(this.user.userID)).then(
                    response => {
                        this.clients = response.body;
                    },
                    response => {
                        console.log(response)
                    }
                )
            },
            getAllInvoiceForClient(clientId) {
              this.$http.get(this.apiRoutes.get.getInvoicesByClient(clientId)).then(
                  response => {
                      this.clientInvoices = response.body;
                  }
              )
            },

            deleteClient(clientId) {
                this.$http.delete(this.apiRoutes.delete.deleteClient(clientId)).then(
                    response => {
                        this.getAllClients();
                    }
                )
            }
        }
    }
</script>

<style scoped>

</style>
