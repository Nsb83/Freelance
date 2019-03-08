<template>
    <v-flex class="lg-10">
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
                    v-model="form.email"
                    :rules="emailRules"
                    label="E-mail"
            ></v-text-field>

            <v-text-field
                    v-model="form.phoneNumber"
                    label="Numéro de téléphone"
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
                        fixed
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
            <v-btn color="success" flat class="white--text" @click.native="snackbar.showSnackbar = false">Fermer</v-btn>
        </v-snackbar>
    </v-flex>
</template>

<script>
    import {store} from 'vuex';

    export default {
        data: () => ({
            valid: true,
            form: {
                companyName: '',
                referentFirstName: '',
                referentLastName: '',
                phoneNumber: '',
                email: '',
                isActive: false
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
            apiRoutes() {
                return this.$store.state.apiRoutes
            }
        },
        methods: {
            validate() {
                if (this.$refs.form.validate()) {
                    this.$http.post(this.apiRoutes.post.createClient, this.form).then(
                        result => {
                            this.snackbar.showSnackbar = true
                        }
                    )
                }

            }
        }
    }
</script>

<style>

</style>
