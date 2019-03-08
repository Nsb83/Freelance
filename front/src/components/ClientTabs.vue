
<template>
    <v-app>
        <v-layout row wrap>
            <v-flex>
                <v-tabs
                        color="primary"
                        slider-color="secondary"
                >
                    <v-tab
                            :key="0"
                            class="white--text"
                    >
                        Liste de clients
                    </v-tab>
                    <v-tab-item
                            :key="0">
                        <ClientList></ClientList>
                    </v-tab-item>


                    <v-tab
                            :key="1"
                            class="white--text"
                    >
                        Nouveau client
                    </v-tab>
                    <v-tab-item
                            :key="1">
                        <ClientForm></ClientForm>
                    </v-tab-item>
                </v-tabs>
            </v-flex>
        </v-layout>
    </v-app>
</template>

<script>
    import { mapGetters } from 'vuex';
    import ClientForm from "./ClientForm.vue";
    import ClientList from "./ClientList.vue";

    export default {
        name: 'ClientTabs',
        components: {ClientList, ClientForm},
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

<style scoped>

</style>
