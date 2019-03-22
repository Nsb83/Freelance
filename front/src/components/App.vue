<template>
    <v-app id="inspire">
        <v-navigation-drawer
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



<!--<template>-->
    <!--<v-app class="m-1">-->
        <!--<v-navigation-drawer-->
            <!--absolute-->
            <!--v-model="drawer"-->
            <!--clipped-->
            <!--fixed-->
            <!--app-->
        <!--&gt;-->
            <!--<SideMenu></SideMenu>-->
        <!--</v-navigation-drawer>-->
        <!--<v-content>-->
            <!--<v-container>-->
                <!--<v-flex>-->
                    <!--<router-view></router-view>-->
                <!--</v-flex>-->

            <!--</v-container>-->

        <!--</v-content>-->

    <!--</v-app>-->
<!--</template>-->
<script>
    import { mapGetters } from 'vuex';
    import ClientTabs from "./Client/ClientTabs.vue";
    import SideMenu from "./SideMenu.vue";

    export default {
        name: 'App',
        components: {ClientTabs, SideMenu},
        // components: {SideMenu, ClientTabs},
        computed: {
            ...mapGetters([
                'apiRoutes'
            ])
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
