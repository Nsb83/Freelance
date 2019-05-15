<template>
    <div class="h-100">
        <v-app
                style="background-color: transparent"
                id="app"
                light>
            <v-snackbar
                    :timeout="6000"
                    :bottom="true"
                    :right="true"
                    :multi-line="true"
                    :vertical="true"
                    v-model="failedLogin"
            >
                E-mail ou mot de passe incorrect
                <v-btn flat color="primary" @click.native="failedLogin = false">Fermer</v-btn>
            </v-snackbar>
            <v-container fill-height>
                <v-layout row wrap align-center>
                    <v-flex md6 offset-md3 class="text-xs-center">
                        <v-card>
                            <v-form @submit.stop.prevent="login()">
                                <v-card-title>
                                    <h4>Freelance Facturer</h4>
                                </v-card-title>
                                <v-card-text >
                                    <v-text-field v-model="form.email" label="Adresse Email">
                                    </v-text-field>
                                    <v-text-field v-model="form.password" label="Mot de Passe" type="password">
                                    </v-text-field>
                                    <v-checkbox color="primary" v-model="form.rememberMe" label="Se souvenir de moi"></v-checkbox>
                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn color="primary" type="submit" class="white--text">Connexion</v-btn>
                                    </v-card-actions>
                                </v-card-text>
                            </v-form>
                            <v-divider></v-divider>
                            <div>Pas encore inscrit ?</div>
                            <v-card-actions>
                                <v-btn color="secondary" @click.native="signUp()" class="white--text">S'inscrire</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-app>
    </div>
</template>

<script>
    import { mapGetters, mapActions } from 'vuex';
    import apiRoutes from "../routes/apiRoutes";
    import Vue from 'vue';

    export default {
        data() {
            return {
                form: {
                    email: '',
                    password: '',
                    rememberMe: false
                },
                failedLogin: false,
            }
        },
        computed: {
            ...mapGetters(['apiRoutes', 'user'])
        },
        methods: {
            // makePath(user) {
            //     return ('/client');
            // },
            login: function() {
                let that = this;
                this.$http.post(this.apiRoutes.post.login, this.form).then(result => {
                    Vue.ls.set("jwt", result.body.token);
                    this.updateUser(function () {
                        // let path = that.$route.query.from;
                        // if (path === undefined || path === '/')
                        //     path = that.makePath(user);
                        that.$router.push({ path: '/client' });
                    });
                }, result => {
                    this.failedLogin = true;
                })
            },
            signUp() {
                this.$router.push({ path: '/signup' })

            },
            ...mapActions(['updateUser'])
        },
    }
</script>

<style lang="scss">
</style>
