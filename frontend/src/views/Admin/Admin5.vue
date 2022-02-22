<template>
<v-container fluid>
    <v-row v-for="count in counts" :key="count">
        <v-col cols="8" align-self="center">
            <v-img src="https://picsum.photos/500/200"></v-img>
        </v-col>
        <v-col cols="2" align-self="center">
            <v-file-input accept="image/png, image/jpeg, image/bmp" hide-details></v-file-input>
        </v-col>
        <v-col cols="1" align-self="center">
            <v-text-field hide-details></v-text-field>
        </v-col>
        <v-col cols="1" align-self="center">
            <v-btn class="primary">btn</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            counts: 7
        }
    }
}
</script>

<style>

</style>
