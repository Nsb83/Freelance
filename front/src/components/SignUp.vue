<template>
    <v-dialog v-model="isActive" max-width="700">
        <v-card>
            <v-card-text>
                <v-form>
                    <v-form fill-width v-model="valid" ref="form" lazy-validation autocomplete="off">
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
                        @click.native="isActive = false"
                        color="primary"
                >
                    Annuler
                </v-btn>
                <v-btn
                        flat
                        :disabled="!valid"
                        @click="onSubmit"
                        color="primary"
                >
                    Valider
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {

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
    }
</script>

<style scoped>

</style>
