<template>
    <div class="m-5">
        <v-card>
            <v-card-text >
                <v-form>
                    <v-form
                            fill-width
                            lazy-validation
                            ref="form">
                        <v-text-field
                                v-model="form.bankName"
                                label="Nom de la Banque"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.BICNumber"
                                label="Identifiant BIC"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.IBANNumber"
                                label="IBAN"
                        ></v-text-field>
                    </v-form>
                </v-form>
            </v-card-text>
            <v-divider></v-divider>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        flat
                        @click="createBank"
                        color="primary"
                >
                    Valider
                </v-btn>
            </v-card-actions>
        </v-card>
        <v-snackbar
                :timeout="snackbar.timeOut"
                :color="snackbar.color"
                :multi-line="true"
                :vertical="false"
                v-model="snackbar.showSnackbar">
            Banque enregistrée !
            <v-btn color="white" flat @click.native="snackbar.showSnackbar = false">Fermer</v-btn>
        </v-snackbar>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex';

    export default {
        created: function() {
            this.getBank(this.user.userID)
        },
        computed: {
            ...mapGetters(['apiRoutes', 'user'])
        },
        data() {
            return {
                valid: true,
                form: {
                    id: '',
                    bankName: '',
                    BICNumber: '',
                    IBANNumber: '',
                },
                snackbar: {
                    showSnackbar: false,
                    timeOut: 5000,
                    color: 'success'
                },
            }
        },
        methods: {
            createBank() {
                const userID = this.user.userID;
                this.$http.post(this.apiRoutes.post.createBank(userID), this.form).then(
                    response => {
                        this.snackbar.showSnackbar = true;
                        this.getBank(userID)
                    },
                    response => {
                        console.log(response)
                    }
                )
            },
            getBank() {
                this.$http.get(this.apiRoutes.get.getBank(this.user.userID)).then(
                    response => {
                        this.form.id = response.body.id;
                        this.form.bankName = response.body.bankName;
                        this.form.BICNumber = response.body.BICNumber;
                        this.form.IBANNumber = response.body.IBANNumber
                    }, response => {
                    console.log(response)
                }
                )
            }

        }
    }
</script>

<style scoped>

</style>
