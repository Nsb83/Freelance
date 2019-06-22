<template>
    <div>
        <v-card color="primary" class="white--text p-1 m-1">
            <v-card-title primary-title>
                <div>
                    <div class="headline">{{ user.fullName }}</div>
                    <div><v-icon color="white">email</v-icon>{{ user.email }}</div>
                    <div><v-icon color="white">phone</v-icon>{{ user.phoneNumber }}</div>
                    <div><v-icon color="white">work_outline</v-icon>{{ user.SIRETNumber }}</div>
                </div>
            </v-card-title>

        </v-card>


    <v-list class=" white--text p-1">
        <router-link class="no-wrap-words nav-link white--text secondary" :to="{ name: 'ClientTabs' }">
            <v-list-tile class=" mt-2">
                <v-list-tile-action>
                    <v-icon color="white">people</v-icon>
                </v-list-tile-action>
                <v-list-tile-content>
                    <v-list-tile-title>Clients</v-list-tile-title>
                </v-list-tile-content>
            </v-list-tile>
        </router-link>
        <router-link class="no-wrap-words nav-link white--text secondary" :to="{ name: 'InvoiceTabs' }">
            <v-list-tile class="mt-2">
                <v-list-tile-action>
                    <v-icon color="white">receipt</v-icon>
                </v-list-tile-action>
                <v-list-tile-content>
                    <v-list-tile-title>Factures</v-list-tile-title>
                </v-list-tile-content>
            </v-list-tile>
        </router-link>
            <router-link class="no-wrap-words nav-link white--text secondary" :to="{ name: 'UserTabs' }">
                <v-list-tile class="mt-2">
                    <v-list-tile-action>
                        <v-icon color="white">settings</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Paramètres</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
        </router-link>
    </v-list>
    </div>
</template>

<script>
    import { mapGetters, mapActions } from 'vuex';
    import { store } from '../store';

    export default {
        name: "SideMenu",

        computed: {
            ...mapGetters(['user'])
        },
        methods: {
            ...mapActions(['removeUser', 'updateUser']),
        }
    }
</script>

<style scoped>

</style>
