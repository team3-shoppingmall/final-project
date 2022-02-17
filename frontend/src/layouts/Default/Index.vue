<template>
<v-app>
    <v-app-bar color="primary" app dark>
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
        <v-row>
            <v-col cols="10" align-self="center">
                <v-row class="mx-10" justify="space-between">
                    <v-col cols="auto" align-self="center">
                        <v-btn v-for="idx in 6" :key="idx">{{idx}}</v-btn>
                    </v-col>
                    <v-col cols="auto" align-self="center">
                        <v-btn v-for="idx in 4" :key="idx">{{idx}}</v-btn>
                        <v-btn :to="'/authentication/signIn'">Sign In</v-btn>
                        <v-btn :to="'/authentication/signUp'">Sign Up</v-btn>
                    </v-col>
                </v-row>
            </v-col>
            <v-col cols="2" align-self="center">
              <!-- 색상 수정 방법 찾아서 하기 -->
                <v-select :items="items" label="Solo field" dense solo hide-details></v-select>
            </v-col>
        </v-row>
        <!-- <router-link :to="'/about'" class="white">sdsdsd</router-link> -->
    </v-app-bar>
    <v-main>
        <router-view />
    </v-main>
    <v-footer>
        <Footer />
    </v-footer>
</v-app>
</template>

<script>
import Footer from '@/components/Footer.vue'
export default {
    data() {
        return {
            items: ['Foo', 'Bar', 'Fizz', 'Buzz'],
        }
    },
    components: {
        Footer,
    }

}
</script>

<style scoped>
</style>
