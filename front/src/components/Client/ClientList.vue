<template>
    <v-data-table
            :headers="headers"
            :items="clients"
            class="elevation-1 text-xs-center"
            hide-actions
    >
        <template v-slot:items="props">
            <td class="text-center">{{ props.item.companyName }}</td>
            <td class="text-center">{{ props.item.referentFirstName }} {{ props.item.referentLastName }}</td>
            <td class="text-center">{{ props.item.adress }}</td>
            <td class="text-center">{{ props.item.postalCode}}</td>
            <td class="text-center">{{ props.item.city}}</td>
            <td class="text-center">{{ props.item.email }}</td>
            <td class="text-center">{{ props.item.phoneNumber }}</td>
            <td class="text-center">{{ props.item.VATNumber }}</td>
            <td class="text-center" v-if="props.item.isActive === true">Actif</td>
            <td class="text-center secondary--text" v-else>Inactif</td>

            <v-btn slot="activator" icon class="mx-0" @click.native="deleteClient(props.item.id)">
                <v-icon color="secondary">delete</v-icon>
            </v-btn>
        </template>
    </v-data-table>
</template>

<script>
    import { store } from 'vuex';
    import { bus } from '../../main';

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
            apiRoutes(){
                return this.$store.state.apiRoutes
            }
        },
        data() {
            return {
                headers: [
                    { text: 'Nom de l\'entreprise', value: 'companyName', align:'center'},
                    { text: 'Contact', value: 'referentName', align:'center' },
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
                        this.clients = response.body
                        console.log(this.clients)
                    },
                    response => {
                        console.log(response)
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
