<template>
    <v-app id="inspire">
        <v-navigation-drawer
                v-if="isUserConnected"
                v-model="drawer"
                clipped
                fixed
                app
        >
        <SideMenu></SideMenu>
        </v-navigation-drawer>
        <v-toolbar app fixed clipped-left>
            <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
            <v-toolbar-title>Freelance Facturer</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-toolbar-items>
                <v-btn flat @click="disconnect">
                    <v-icon>close</v-icon> Déconnexion
                </v-btn>
            </v-toolbar-items>
        </v-toolbar>
        <v-content >
            <v-container fluid fill-height class="p-1">
                <v-layout>
                    <v-flex>
                           <router-view></router-view>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>
        <v-footer app fixed>
            <span>&copy; 2019</span>
        </v-footer>
    </v-app>
</template>



<script>
    import { mapGetters, mapActions } from 'vuex';
    import InvoiceTabs from "./Invoice/InvoiceTabs.vue";
    import SideMenu from "./SideMenu.vue";
    import Vue from 'vue';

    export default {
        name: 'App',
        components: {InvoiceTabs, SideMenu},
        computed: {
            ...mapGetters([ 'apiRoutes', 'isUserConnected' ])
        },
        methods: {
            ...mapActions([
                'removeUser'
            ]),
            disconnect: function () {
                Vue.ls.remove("jwt");
                this.removeUser();
                this.$router.push({name: 'login'});
            }
        },
        data() {
            return {
                active: null,
                drawer: null
            }
        }
    }
</script>

<style lang="scss">

</style>
