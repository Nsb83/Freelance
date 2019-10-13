<template>
    <div class="m-5">
        <v-card>
            <v-card-text >
                    <v-form fill-width autocomplete="off" ref="form">
                        <v-text-field
                                v-model="form.firstName"
                                label="Prénom"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.lastName"
                                label="Nom"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.email"
                                label="Adresse Email"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.phoneNumber"
                                label="Numéro de téléphone"
                                mask="## ## ## ## ##"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.address"
                                label="Adresse"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.postalCode"
                                label="Code Postal"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.city"
                                label="Ville"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                v-model="form.SIRETNumber"
                                label="Numéro SIRET"
                                :rules="[(value) => { return genericRequiredRule(value)}]"
                        ></v-text-field>
                        <v-text-field
                                :append-icon="passwordInvisible ? 'visibility' : 'visibility_off'"
                                autocomplete="new-password"
                                v-on:click:append="() => (passwordInvisible = !passwordInvisible)"
                                :type="passwordInvisible ? 'password' : 'text'"
                                v-model="form.password"
                                label="Mot de passe"
                                :rules="[(value) => { return genericRequiredRule(value)}]"

                        ></v-text-field>
                    </v-form>
                <v-snackbar
                        :timeout="5000"
                        color="success"
                        :multi-line="true"
                        :vertical="false"
                        v-model="showSnackbar">
                    Votre modification a été prise en compte
                </v-snackbar>
            </v-card-text>
            <v-divider></v-divider>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        v-if="!update"
                        flat
                        @click.native="backToMainPage()"
                        color="primary"
                >
                    Annuler
                </v-btn>
                <v-btn
                        v-if="!update"
                        flat
                        @click="signUp"
                        color="primary"
                >
                    Valider
                </v-btn>
                <v-btn
                        v-if="update"
                        flat
                        @click="updateUser"
                        color="primary"
                >
                    Mettre à jour
                </v-btn>
            </v-card-actions>
        </v-card>

    </div>

</template>

<script>
    import { mapGetters } from 'vuex';
    import rules from "../../Utils/rules";

    export default {
        mixins: [rules],
        computed: {
            ...mapGetters(['apiRoutes', 'user'])
        },
        created: function() {
            if(this.update) {
                this.getUser(this.user.userID)
            }
        },
        props:{
          update: {
              type: Boolean,
              default: false
          }
        },
        data() {
            return {
                valid: true,
                passwordAgainInvisible: true,
                passwordInvisible: true,
                form: {
                    userID: '',
                    firstName: '',
                    lastName: '',
                    email: '',
                    phoneNumber: '',
                    address: '',
                    postalCode: '',
                    city: '',
                    SIRETNumber: '',
                    password: '',
                },
                showSnackbar: false
            }
        },
        methods: {
            backToMainPage() {
                this.$router.push({ path: '/login' })
            },
            signUp() {
                if(this.$refs.form.validate())
                this.$http.post(this.apiRoutes.post.signUp, this.form).then(
                    response => {
                        this.$router.push({ path: '/login' });
                    },
                    response => {
                        console.log(response)
                }
                )
            },
            getUser() {
                if(this.update) {
                    this.form.userID = this.user.userID;
                    this.form.firstName= this.user.firstName;
                    this.form.lastName= this.user.lastName;
                    this.form.email= this.user.email;
                    this.form.phoneNumber= this.user.phoneNumber;
                    this.form.address= this.user.address;
                    this.form.postalCode= this.user.postalCode;
                    this.form.city= this.user.city;
                    this.form.SIRETNumber= this.user.SIRETNumber;
                }
            },
            updateUser() {
                if(this.$refs.form.validate())
                this.form.userID = this.user.userID;
                this.$http.post(this.apiRoutes.post.updateUser, this.form).then(
                    response => {
                        this.showSnackbar = true;
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
