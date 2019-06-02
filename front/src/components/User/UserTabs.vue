
<template>
    <v-layout>
        <v-flex>
            <v-tabs
                    color="primary"
                    slider-color="secondary"
            >
                <v-tab
                        :key="0"
                        class="white--text"
                >
                    Banque
                </v-tab>
                <v-tab-item
                        :key="0">
                    <Bank></Bank>
                </v-tab-item>


<!--                <v-tab-->
<!--                        :key="1"-->
<!--                        class="white&#45;&#45;text"-->
<!--                >-->
<!--                    Utilisateur-->
<!--                </v-tab>-->
<!--                <v-tab-item-->
<!--                        :key="1">-->
<!--                    <SignUp></SignUp>-->

<!--                </v-tab-item>-->
            </v-tabs>
        </v-flex>
    </v-layout>
</template>

<script>
    import { mapGetters } from 'vuex';
    import Bank from './Bank.vue';
    import SignUp from './SignUp.vue';

    export default {
        name: 'UserTabes',
        components: { Bank, SignUp },
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
