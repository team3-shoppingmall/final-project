<template>
<v-container>
    Community Page
    <v-btn :to="'/community/notice'">notice</v-btn>
    <v-btn :to="'/community/review'">review</v-btn>
    <v-btn :to="'/community/faq'">FAQ</v-btn>
    <router-view></router-view>
</v-container>
</template>

<script>
export default {

}
</script>

<style>

</style>
