
<template>
    <v-app>
        <v-container>
    <v-flex>
        <router-link to="/ClientList">Liste de clients</router-link>
        <router-link to="/ClientForm">Nouveau client</router-link>
    </v-flex>


    <router-view></router-view>
        </v-container>
    </v-app>
</template>

<script>
    import { mapGetters } from 'vuex';

    export default {
        name: 'Example',
        computed: {
            ...mapGetters([
                'apiRoutes'
            ])
        },
    }
</script>

<style>

</style>
