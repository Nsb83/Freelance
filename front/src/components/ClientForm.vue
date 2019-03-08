<template>
    <v-container class="lg-10">
        <v-form
                ref="form"
                v-model="valid"
                lazy-validation
        >
            <v-text-field
                    v-model="form.companyName"
                    label="Nom de l'entreprise"
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

            <v-btn
                    class="primary"
                    @click="validate"
            >
                Validate
            </v-btn>
        </v-form>
    </v-container>
</template>

<script>
    import { store } from 'vuex';

    export default {
        data: () => ({
            valid: true,
            form:{
                companyName: '',
                referentFirstName: '',
                referentLastName: '',
                phoneNumber: '',
                email: '',
                isActive: false
            },
            emailRules: [
                v => !!v || 'E-mail is required',
                v => /.+@.+/.test(v) || 'E-mail must be valid'
            ],
        }),
        computed: {
            apiRoutes(){
                return this.$store.state.apiRoutes
            }
        },
        methods: {
            validate () {
                if (this.$refs.form.validate()) {
                    this.$http.post(this.apiRoutes.post.createClient, this.form).then(
                        result => {
                            this.snackbar = true
                        }
                    )
                }

            }
        }
    }
</script>

<style>

</style>
