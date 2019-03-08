<template>
    <v-app>
        <v-container>

            <router-view>
                <ClientTabs></ClientTabs>
            </router-view>
        </v-container>
    </v-app>
</template>
<script>
    import { mapGetters } from 'vuex';
    import ClientTabs from "./ClientTabs.vue";

    export default {
        name: 'App',
        components: {ClientTabs},
        computed: {
            ...mapGetters([
                'apiRoutes'
            ])
        },
        data() {
            return {
                active: null,
            }
        }
    }
</script>

<style>

</style>
