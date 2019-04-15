<template>
    <v-flex class="px-2">
        <v-form
                ref="form"
                v-model="valid"
                lazy-validation
        >
            <v-autocomplete
                label="client"
                v-model="form.clientId"
                items="clients">
            </v-autocomplete>
            <v-text-field
                    v-model="form.serviceName"
                    label="Prestation"
            ></v-text-field>

            <v-text-field
                    v-model="form.quantity"
                    label="Quantité"
            ></v-text-field>

            <v-text-field
                    v-model="form.unitPrice"
                    label="Prix Unitaire"
            ></v-text-field>

            <v-text-field
                    v-model="form.VATRate"
                    label="Taux de TVA"
            ></v-text-field>
            <v-flex class="offset-5 lg-2">
                <v-btn
                        class="primary"
                        center
                        @click="validate"
                        round
                >
                    Enregistrer
                </v-btn>
            </v-flex>
        </v-form>
        <v-snackbar
                :timeout="snackbar.timeOut"
                :color="snackbar.color"
                :multi-line="true"
                :vertical="false"
                v-model="snackbar.showSnackbar">
            Facture enregistrée !
            <v-btn color="white" flat @click.native="snackbar.showSnackbar = false">Fermer</v-btn>
        </v-snackbar>
    </v-flex>
</template>

<script>
    import { store } from 'vuex';
    import { bus } from '../../main';

    export default {
        name: "InvoiceForm",
        data: () => ({
            valid: true,
            clients: [],
            form: {
                clientId: '',
                service: {
                    serviceName: '',
                    quantity: '',
                    unitPrice: '',
                    VATRate: '',
                }
            },
            snackbar: {
                showSnackbar: false,
                timeOut: 5000,
                color: 'success'
            },
        }),
        computed: {
            apiRoutes() {
                return this.$store.state.apiRoutes
            }
        },
        methods: {
            validate() {
                if (this.$refs.form.validate()) {
                    this.$http.post(this.apiRoutes.post.createInvoice(this.form.clientId), this.form).then(
                        result => {
                            this.snackbar.showSnackbar = true;
                            bus.$emit('getAllClients');

                        }
                    )
                }
            },
            getClients() {
                this.$http.get(this.apiRoutes.get.getClients).then(
                    response => {
                        this.clients = _.map(response.body, function (client) {
                            return ({ value: client.id, text: client.companyName });
                        });
                    },
                    response => {
                        console.log(response)
                    }
                )
            }
        }
    }
</script>

<style lang="scss">
    .v-text-field {
        padding-top: 6px;
        margin-top: 2px;
    }
</style>
