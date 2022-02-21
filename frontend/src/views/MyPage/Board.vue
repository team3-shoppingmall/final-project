<template>
<v-container style="min-height: 910px;">
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">MY BOARD</div>
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="9">
            <MyPageButton class="mt-5" />
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import MyPageButton from '@/components/MyPageButton.vue'
export default {
    components: {
        MyPageButton
    }
}
</script>

<style>

</style>
