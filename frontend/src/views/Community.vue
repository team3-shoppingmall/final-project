<template>
<v-container>
    <v-row justify="center">
        <v-col xs="12" sm="12" md="12" lg="9" xl="8">
            <h3>{{UPPER(pagename)}}</h3>
            <v-row justify="center">
                <v-col cols="auto">
                    <v-btn :to="'/community/notice'">notice</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :to="'/community/review'">review</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :to="'/community/faq'">FAQ</v-btn>
                </v-col>
            </v-row>
            <router-view></router-view>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            pagename: '',
        }
    },
    methods: {
        currentURL() {
            let link = document.location.href;
            link = link.substring(32, link.length);
            this.pagename = link;
        },
        UPPER(word) {
            return word.toUpperCase();
        }
    },
    computed: {},
    watch: {
        '$route'() {
            this.currentURL();
        }
    },
    mounted() {
        this.currentURL();
    }
}
</script>

<style>

</style>
