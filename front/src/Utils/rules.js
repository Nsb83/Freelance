export default {
    data() {
        return {
            genericRequiredRule: (value) => !!value || 'Ce champ est obligatoire',
        }
    }
}
