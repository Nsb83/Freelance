<template>
    <div class="m-5">
        <v-card>
            <v-card-text>
                <v-form>
                    <v-form fill-width lazy-validation autocomplete="off">
                        <v-text-field
                                v-model="form.firstName"
                                label="Prénom"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.lastName"
                                label="Nom"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.email"
                                label="Adresse Email"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.phoneNumber"
                                label="Numéro de téléphone"
                                mask="## ## ## ## ##"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.SIRENNumber"
                                label="Numéro SIREN"
                        ></v-text-field>
                        <v-text-field
                                :append-icon="passwordInvisible ? 'visibility' : 'visibility_off'"
                                autocomplete="new-password"
                                v-on:click:append="() => (passwordInvisible = !passwordInvisible)"
                                :type="passwordInvisible ? 'password' : 'text'"
                                v-model="form.password"
                                label="Mot de passe"
                        ></v-text-field>
<!--                        <v-text-field-->
<!--                                :validate-on-blur="true"-->
<!--                                :append-icon="passwordAgainInvisible ? 'visibility' : 'visibility_off'"-->
<!--                                v-on:click:append="() => (passwordAgainInvisible = !passwordAgainInvisible)"-->
<!--                                :type="passwordAgainInvisible ? 'password' : 'text'"-->
<!--                                v-model="form.passwordConfirm"-->
<!--                                :rules="[-->
<!--                        (value) => { return (passwordAgainRule(value, form.password)) }-->
<!--                        ]"-->
<!--                                label="Saisissez à nouveau votre mot de passe"-->
<!--                        ></v-text-field>-->

                    </v-form>
                </v-form>
            </v-card-text>
            <v-divider></v-divider>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        flat
                        @click.native="backToMainPage()"
                        color="primary"
                >
                    Annuler
                </v-btn>
                <v-btn
                        flat
                        @click="signUp"
                        color="primary"
                >
                    Valider
                </v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex';

    export default {
        computed: {
            ...mapGetters(['apiRoutes'])
        },
        data() {
            return {
                valid: true,
                passwordAgainInvisible: true,
                passwordInvisible: true,
                form: {
                    userId: '',
                    firstName: '',
                    lastName: '',
                    email: '',
                    phoneNumber: '',
                    SIRENNUmber: '',
                    password: '',
                    // passwordConfirm: ''
                }
            }
        },
        methods: {
            backToMainPage() {
                this.$router.push({ path: '/login' })
            },
            signUp() {
                this.$http.post(this.apiRoutes.post.signUp, this.form).then(
                    response => {
                        this.$router.push({ path: '/login' });
                    },
                    response => {
                        console.log(response)
                }
                )
            }
        }
    }
</script>

<style scoped>

</style>
