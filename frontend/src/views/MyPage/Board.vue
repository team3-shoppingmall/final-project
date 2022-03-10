<template>
<v-container fluid>
    <v-row justify="center">
        <v-col>
            <MyPageReview class="mt-0" :id="id" />
        </v-col>
    </v-row>
    <v-divider class="my-5"></v-divider>
    <v-row justify="center">
        <v-col>
            <MyPageQnA class="mt-0" :id="id" />
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import MyPageReview from '@/components/MyPageReview.vue'
import MyPageQnA from '@/components/MyPageQnA.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    data() {
        return {
            id: null,
        }
    },
    components: {
        MyPageReview,
        MyPageQnA
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    mounted() {
        this.id = this.getLogin.user.id;
    }

}
</script>

<style></style>
