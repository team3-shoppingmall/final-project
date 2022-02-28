<template>
<v-container fluid>
    <v-row justify="center">
        <v-col>
            <MyPageReview class="mt-0" />
        </v-col>
    </v-row>
    <v-divider class="my-5"></v-divider>
    <v-row justify="center">
        <v-col>
            <MyPageQnA class="mt-0" />
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import MyPageReview from '@/components/MyPageReview.vue'
import MyPageQnA from '@/components/MyPageQnA.vue'
export default {
    data() {
        return {

        }
    },
    components: {
        MyPageReview,
        MyPageQnA
    }
}
</script>

<style></style>
