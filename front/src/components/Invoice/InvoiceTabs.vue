
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
                    Liste de factures
                </v-tab>
                <v-tab-item
                        :key="0">
                    <InvoiceList></InvoiceList>
                </v-tab-item>


                <v-tab
                        :key="1"
                        class="white--text"
                >
                    Nouvelle facture
                </v-tab>
                <v-tab-item
                        :key="1">
                    <InvoiceForm></InvoiceForm>

                </v-tab-item>
            </v-tabs>
        </v-flex>
    </v-layout>
</template>

<script>
    import { mapGetters } from 'vuex';
    import InvoiceList from './InvoiceList.vue';
    import InvoiceForm from './InvoiceForm.vue';

    export default {
        name: 'InvoiceTabs',
        components: { InvoiceList, InvoiceForm },
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
