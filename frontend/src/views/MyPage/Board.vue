<template>
<v-container style="min-height: 910px;">
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">BOARD</div>
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="9">
            <MyPageButton class="mt-0" />
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="9">
            <MyPageReview class="mt-0" />
        </v-col>
    </v-row>
    <v-divider class="my-5"></v-divider>
    <v-row justify="center">
        <v-col cols="9">
            <MyPageQnA class="mt-0" />
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import MyPageButton from '@/components/MyPageButton.vue'
import MyPageReview from '@/components/MyPageReview.vue'
import MyPageQnA from '@/components/MyPageQnA.vue'
export default {
    data() {
        return {

        }
    },
    components: {
        MyPageButton,
        MyPageReview,
        MyPageQnA
    }
}
</script>

<style></style>
