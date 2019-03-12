<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-data-table
            :headers="headers"
            :items="clients"
            class="elevation-1 text-xs-center"
            hide-actions
    >
        <template v-slot:items="props">
            <td class="text-center">{{ props.item.companyName }}</td>
            <td class="text-center">{{ props.item.referentFirstName }}</td>
            <td class="text-center">{{ props.item.referentLastName }}</td>
            <td class="text-center">{{ props.item.adress }}</td>
            <td class="text-center">{{ props.item.postalCode}}</td>
            <td class="text-center">{{ props.item.city}}</td>
            <td class="text-center">{{ props.item.email }}</td>
            <td class="text-center">{{ props.item.phoneNumber }}</td>
            <td class="text-center">{{ props.item.VATNumber }}</td>
            <td class="text-center">{{ props.item.isActive }}</td>
            <v-btn slot="activator" icon class="mx-0" @click.native="deleteClient(props.item.id)">
                <v-icon color="secondary">delete</v-icon>
            </v-btn>
        </template>
    </v-data-table>
</template>

<script>
    import { store } from 'vuex';
    import { bus } from '../main';

    export default {
        name: "ClientList",
        created: function() {
            this.getAllClients();
        },
        computed: {
            apiRoutes(){
                return this.$store.state.apiRoutes
            }
        },
        data() {
            return {
                headers: [
                    { text: 'Nom de l\'entreprise', value: 'companyName', align:'center'},
                    { text: 'Prénom du contact', value: 'referentFirstName', align:'center' },
                    { text: 'Nom du Contact', value: 'referentLastName', align:'center' },
                    { text: 'Numéro et rue', value: 'adress', align:'center' },
                    { text: 'Code Postal', value: 'postalCode', align:'center' },
                    { text: 'Ville', value: 'city', align:'center' },
                    { text: 'Email', value: 'email', align:'center' },
                    { text: 'Numéro de téléphone', value: 'phoneNumber', align:'center' },
                    { text: 'Numéro de TVA', value: 'VATNumber', align:'center' },
                    { text: 'Est actif', value: 'isActive', align:'center' }
                ],
                clients: [],
            }
        },
        watch:{
            apiRoutes(){
                return this.$store.state.apiRoutes
            }
        },
        methods: {
            getAllClients() {
                this.$http.get(this.apiRoutes.get.getClients).then(
                    response => {
                        console.log(response.body);
                        this.clients = response.body
                    },
                    response => {
                        console.log(response)
                    }
                )
            },
            deleteClient(clientId) {
                this.$http.delete(this.apiRoutes.delete.deleteClient(clientId)).then(
                    response => {
                        console.log(response);
                        this.getAllClients();
                    }
                )
            }

        }
    }
</script>

<style scoped>

</style>
