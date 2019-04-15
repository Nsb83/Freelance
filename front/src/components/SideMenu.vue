<template>

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
    </v-list>
</template>

<script>
    export default {
        name: "SideMenu"
    }
</script>

<style scoped>

</style>
