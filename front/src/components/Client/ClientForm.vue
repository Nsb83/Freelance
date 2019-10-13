<template>
    <v-flex class="px-2">
        <v-form
                ref="form"
                v-model="valid"
                lazy-validation
        >
            <v-text-field
                    v-model="form.companyName"
                    label="Nom de l'entreprise"
                    :rules="nameRules"
            ></v-text-field>

            <v-text-field
                    v-model="form.referentFirstName"
                    label="Prénom du client"
            ></v-text-field>

            <v-text-field
                    v-model="form.referentLastName"
                    label="Nom du contact"
            ></v-text-field>

            <v-text-field
                    v-model="form.address"
                    label="Numéro et rue"
            ></v-text-field>

            <v-text-field
                    v-model="form.postalCode"
                    label="Code Postal"
            ></v-text-field>

            <v-text-field
                    v-model="form.city"
                    label="Ville"
            ></v-text-field>

            <v-text-field
                    v-model="form.email"
                    :rules="emailRules"
                    label="E-mail"
            ></v-text-field>

            <v-text-field
                    v-model="form.phoneNumber"
                    label="Numéro de téléphone"
            ></v-text-field>

            <v-text-field
                    v-model="form.VATNumber"
                    label="Numéro de TVA"
            ></v-text-field>
            <v-checkbox
                    v-model="form.isActive"
                    label="Est actif ?"
            ></v-checkbox>
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
            Entreprise enregistrée !
            <v-btn color="white" flat @click.native="snackbar.showSnackbar = false">Fermer</v-btn>
        </v-snackbar>
    </v-flex>
</template>

<script>
    import { bus } from '../../main';
    import { mapGetters } from 'vuex';

    export default {
        data: () => ({
            valid: true,
            form: {
                companyName: '',
                referentFirstName: '',
                referentLastName: '',
                address:'',
                postalCode:'',
                city:'',
                phoneNumber: '',
                VATNumber:'',
                email: '',
                isActive: false,
                userId: ''
            },
            snackbar: {
                showSnackbar: false,
                timeOut: 5000,
                color: 'success'
            },
            emailRules: [
                v => !!v || 'L\'e-mail est obligatoire',
                v => /.+@.+/.test(v) || 'Cet e-mail n\'est pas valide !'
            ],
            nameRules: [
                v => !!v || 'Le nom de l\'entreprise est obligatoire'
            ]
        }),
        computed: {
            ...mapGetters([ 'apiRoutes', 'user'])
        },
        methods: {
            validate() {
                this.form.userId = this.user.userID;
                if (this.$refs.form.validate()) {
                    this.$http.post(this.apiRoutes.post.createClient, this.form).then(
                        result => {
                            this.snackbar.showSnackbar = true;
                            this.$refs.form.reset();
                            bus.$emit('getAllClients');
                        }
                    )
                }

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
