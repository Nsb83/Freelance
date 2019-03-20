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
        <router-link class="no-wrap-words nav-link white--text secondary" :to="{ name: 'InvoiceList' }">
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


    <!--<div>-->
    <!--<v-container fluid>-->
    <!--<v-layout row wrap>-->
    <!--<v-flex xs12>-->
    <!--<v-card color="secondary" :hover="true" class="white&#45;&#45;text">-->
    <!--<v-card-title primary-title>-->
    <!--<div>-->
    <!--<div class="headline">Hello</div>-->
    <!--</div>-->
    <!--</v-card-title>-->
    <!--<v-card-actions>-->
    <!--<v-btn flat dark>client</v-btn>-->
    <!--</v-card-actions>-->
    <!--</v-card>-->
    <!--</v-flex>-->
    <!--</v-layout>-->
    <!--<v-divider-->
    <!--dark-->
    <!--class="my-3"-->
    <!--&gt;</v-divider>-->
    <!--<v-layout row wrap class="">-->
    <!--<v-flex xs12>-->

    <!--</v-flex>-->
    <!--</v-layout>-->


    <!--</v-container>-->
    <!--</div>-->
</template>

<script>
    export default {
        name: "SideMenu"
    }
</script>

<style scoped>

</style>
