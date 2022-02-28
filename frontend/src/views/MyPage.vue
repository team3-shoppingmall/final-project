<template>
<v-container >
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">MY PAGE</div>
            <v-row class="justify-center">
                <v-btn class="mx-5" :to="'/myPage/home'">MY PAGE</v-btn>
                <v-btn class="mx-5" :to="'/myPage/order'">ORDER</v-btn>
                <v-btn class="mx-5" :to="'/myPage/profile'">PROFILE</v-btn>
                <v-btn class="mx-5" :to="'/myPage/wishlist'">WISH LIST</v-btn>
                <v-btn class="mx-5" :to="'/myPage/point'">POINT</v-btn>
                <v-btn class="mx-5" :to="'/myPage/board'">BOARD</v-btn>
            </v-row>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-divider class="mt-5"></v-divider>
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="9">
            <router-view></router-view>
        </v-col>
    </v-row>

</v-container>
</template>

<script>
export default {
    components: {}
}
</script>

<style scoped>
tr:hover {
    background: white !important;
}
</style>
