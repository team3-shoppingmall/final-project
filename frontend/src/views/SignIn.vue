<template>
<v-container>
    <v-row justify="center" style="height: 970px">
        <v-col cols="3" align-self="center">
            <!-- <v-row class="mb-10" justify="center">
                <v-img :src="require(`@/assets/SignIn_Logo.png`)" max-height="300" max-width="450"></v-img>
            </v-row> -->
            <v-form ref="form">

                <v-text-field v-model="id" :rules="rules.id" counter="25" label="ID" required ref="id"></v-text-field>
                <v-text-field v-model="password" :rules="rules.pwd" label="PASSWORD" type="password" required ref="pwd" @keyup.enter="signIn"></v-text-field>
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
import axios from 'axios'
// import router from '@/router/index'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    data() {
        return {
            id: '',
            password: '',
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
            let validate = this.$refs.form.validate();
            if (validate) {
                axios.get('/api/member/login', {
                        params: {
                            id: this.id,
                            password: this.password,
                        }
                    })
                    .then(res => {
                        let temp = res.data;
                        let user = {
                            id: temp.id,
                            authority: temp.authority,
                        };
                        this.Login({
                            user
                        })
                    })
                    .then(() => {
                        // console.log(this.getPath)
                        this.$router.push(this.getPath)
                    })
                    .catch(err => {
                        if (err.response.data == 'ID NOT FOUND') {
                            alert("아이디가 존재하지 않습니다.");
                            this.$refs.pwd.reset();
                            this.$refs.id.focus();
                        } else if (err.response.data == 'PASSWORD NOT MATCHED') {
                            alert("비밀번호가 일치하지 않습니다.");
                            this.$refs.pwd.reset();
                            this.$refs.pwd.focus();
                        }
                    })
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
        ...LoginStore.mapActions(['Login']),
        ...LoginStore.mapMutations(['setPath']),
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
        ...LoginStore.mapGetters(['getPath'])
    },
    mounted() {
        this.$vuetify.goTo(0);
        if (this.$route.params.nextPage != null)
            this.setPath(`${this.$route.params.nextPage}`)
        else
            this.setPath('/')
    }
}
</script>

<style>

</style>
