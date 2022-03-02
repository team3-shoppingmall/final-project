<template>
<v-container>
    <v-row justify="center" style="height: 970px">
        <v-col cols="3" align-self="center">
            <!-- <v-row class="mb-10" justify="center">
                <v-img :src="require(`@/assets/SignIn_Logo.png`)" max-height="300" max-width="450"></v-img>
            </v-row> -->
            <v-form ref="form">

                <v-text-field v-model="id" :rules="rules.id" counter="25" hint="This field uses counter prop" label="ID" required></v-text-field>
                <v-text-field v-model="password" :rules="rules.pwd" label="PASSWORD" type="password" required></v-text-field>
                <v-row class="mt-10" justify="center">
                    <v-btn class="mr-5 primary pl-2 pr-3" @click="goBack">
                        <v-icon class="pr-1">mdi-home</v-icon>
                        Home
                    </v-btn>
                    <v-btn class="mr-5 primary pl-2 pr-3" @click="signIn">
                        <v-icon class="pr-1">mdi-login-variant</v-icon>
                        sign In
                    </v-btn>
                    <v-btn class="primary pl-2 pr-3" @click="signUp">
                        <v-icon class="pr-1">mdi-account</v-icon>
                        sign Up
                    </v-btn>
                    <v-btn class="mr-5 primary pl-2 pr-3" @click="loginKakao">
                        <v-icon class="pr-1">mdi-login-variant</v-icon>
                        kakao
                    </v-btn>
                </v-row>
            </v-form>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            id: '',
            pwd: '',
            rules: {
                id: [v => !!v || '아이디는 필수 입력사항입니다.',
                    v => /^[a-zA-Z0-9]*$/.test(v) || '아이디는 영문+숫자만 입력 가능합니다.',
                ],
                pwd: [v => !!v || '비밀번호는 필수 입력사항입니다.', ],
            }
        }
    },
    methods: {
        goBack() {
            this.$router.push('/')
        },
        signIn() {
            let validate = this.$refs.form.validate()
            if (validate) {
                this.$router.push('/');
            }
        },
        signUp() {
            this.$router.push('/authentication/signUp');
        },
        // loginKakao() {
        //     window.location.replace(
        //     "https://kauth.kakao.com/oauth/authorize?client_id=AppKey
        //     &redirect_uri=http://localhost:9000/kakaologin&response_type=code"
        // );
        // },
    },
    mounted() {
        window.scrollTo(0, 0)
    }
}
</script>

<style>

</style>
