<template>
<v-container>
    <p>Naver Login Access Pages</p>
</v-container>
</template>

<script>
import axios from 'axios';
export default {
    methods: {
        login(token) {
            axios.get(`/api/member/getNaverLogin/${token}`)
                .then(res => {
                    console.log(res.data);
                })
        }
    },
    mounted() {
        const naver_id_login = new window.naver_id_login("INmTkpuK5mPhbhHfYG_Q", "http://localhost:9000/authentication/signIn/naver");
        this.login(naver_id_login.getAccessToken());
    },
};
</script>

<style>

</style>
