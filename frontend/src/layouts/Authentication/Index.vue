<template>
<v-app>
    <v-main>
        <router-view />
    </v-main>
    <v-footer color="primary lighten-1" padless>
        <Footer />
    </v-footer>
</v-app>
</template>

<script>
import Footer from '@/components/Footer.vue'
export default {
    components: {
        Footer,
    }
}
</script>

<style>

</style>
