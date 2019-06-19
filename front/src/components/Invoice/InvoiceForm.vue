<template>
    <v-flex class="px-3 py-4">

        <v-container>
            <v-layout>
                <v-flex sm6>
                    <v-autocomplete
                            label="client"
                            v-model="clientId"
                            :items="clients"
                            @change="handleClientChange()"
                    >
                    </v-autocomplete>
                    <v-text-field
                            v-model="form.period"
                            label="Période couverte par la facture : "
                    ></v-text-field>
                </v-flex>
                <v-spacer></v-spacer>
                <v-flex md5>
                    <v-card flat>
                        <v-card-text v-if="client !== null" class="py-0">
                            {{ this.client.referentFirstName }}
                            {{ this.client.referentLastName }}
                        </v-card-text>
                        <v-card-text v-if="client !== null" class="py-0">
                            {{ this.client.address }}
                        </v-card-text>
                        <v-card-text v-if="client !== null" class="py-0">
                            {{ this.client.postalCode }}
                            {{ this.client.city }}
                        </v-card-text>
                        <v-card-text v-if="client !== null" class="py-0">
                            {{ this.client.email }}
                        </v-card-text>
                        <v-card-text v-if="client !== null" class="py-0">
                            {{ this.client.phoneNumber }}
                        </v-card-text>
                        <v-card-text v-if="client !== null" class="py-0">
                            {{ this.client.VATNumber }}
                        </v-card-text>

                    </v-card>
                </v-flex>
            </v-layout>
        </v-container>

        <v-container grid-list-xs>
            <v-flex sm6>

            </v-flex>

        </v-container>

        <div v-for="(service, index) in form.services">
            <v-form
                    ref="form"
                    v-model="valid"
                    lazy-validation
            >
                <v-container grid-list-xs>
                    <v-layout row wrap>
                        <v-flex row wrap>
                            <v-text-field
                                    v-model="service.serviceName"
                                    label="Prestation"
                            ></v-text-field>

                            <v-text-field
                                    v-model="service.quantity"
                                    label="Quantité"
                            ></v-text-field>
                            <v-text-field
                                    v-model="service.unitPrice"
                                    label="Prix Unitaire"
                            ></v-text-field>

                            <v-text-field
                                    v-model="service.VATRate"
                                    label="Taux de TVA"
                            ></v-text-field>
                            <v-btn slot="activator" icon class="mx-0" @click.native="deleteServiceRow(index)">
                                <v-icon color="secondary">delete</v-icon>
                            </v-btn>
                        </v-flex>
                    </v-layout>
                    <v-layout>
                        <v-flex row wrap>
                            <div>
                                Total Hors Taxe : {{ service.quantity * service.unitPrice }}
                            </div>
                            <v-spacer></v-spacer>
                            <div>
                                TVA : {{ (service.quantity * service.unitPrice) * service.VATRate/100 }}
                            </div>
                            <v-spacer></v-spacer>
                            <div>
                                Total TTC : {{ (service.quantity * service.unitPrice) + (service.quantity * service.unitPrice) * service.VATRate/100 }}
                            </div>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-form>
        </div>


        <v-layout align-center justify-center>
            <v-btn
                    color="success"
                    flat
                    @click="addNewService"
                    round
                    outline
            >
                Ajouter un nouveau service
            </v-btn>
        </v-layout>

        <v-flex row wrap>
            <v-btn
                    color="secondary"
                    @click="reset"
                    round
                    outline
            >
                Effacer
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    @click="validate"
                    round
                    outline
            >
                Enregistrer
            </v-btn>
        </v-flex>

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
    import { mapGetters } from 'vuex';

    export default {
        name: "InvoiceForm",

        created() {
            this.getClients();
        },
        data: () => ({
            valid: true,
            clients: [],
            client: null,
            clientId:'',
            form: {
                period: null,
                services:[
                    {
                        serviceName: '',
                        quantity: '',
                        unitPrice: '',
                        VATRate: '',
                    },
                ]
            },
            snackbar: {
                showSnackbar: false,
                timeOut: 5000,
                color: 'success'
            },
        }),
        computed: {
            ...mapGetters(['apiRoutes', 'user'])
        },
        methods: {
            validate() {
                let userID = this.user.userID;
                this.$http.post(this.apiRoutes.post.createInvoice(this.clientId, userID), this.form).then(
                    response => {
                        this.snackbar.showSnackbar = true;
                        bus.$emit('getAllInvoices');
                        this.reset();
                    }
                )
            },
            getClients() {
                this.$http.get(this.apiRoutes.get.getClients(this.user.userID)).then(
                    response => {
                        this.clients = _.map(response.body, function (client) {
                            return ({ value: client.id, text: client.companyName });
                        });
                    },
                    response => {
                        console.log(response)
                    }
                )
            },
            getOneClient(id) {
                this.$http.get(this.apiRoutes.get.getOneClient(id)).then(
                    response => {
                        this.client = response.body;
                    },
                    response => {
                        console.log(response)
                    }
                )
            },
            handleClientChange() {
                this.getOneClient(this.clientId)
            },
            addNewService() {
                this.form.services.push({
                    serviceName: '',
                    quantity: '',
                    unitPrice: '',
                    VATRate: '',
                });
            },
            deleteServiceRow(index) {
                this.form.services.splice(index, 1)
            },
            reset() {
                this.clientId = '';
                this.client = null;
                this.form.services = [
                    {
                        serviceName: '',
                        quantity: '',
                        unitPrice: '',
                        VATRate: '',
                    }
                ]
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
