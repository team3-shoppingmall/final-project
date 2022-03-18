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
                </v-row>
                <v-row class="mt-10" justify="center">
                    <v-col cols="auto" class="mt-1">
                        <div id="naver_id_login"></div>
                    </v-col>
                    <v-col cols="auto">
                        <v-img class="kakao_btn" src="@/assets/kakao_login_medium.png" @click="loginWithKakao" />
                    </v-col>
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
                            name: temp.name,
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
        loginWithKakao() {
            if (window.Kakao.isInitialized() == false) {
                window.Kakao.init('255b187f87731368f5e47c3310b3cf02');
            }
            window.Kakao.Auth.login({
                success: this.kakaoLoginCheck(),
                fail: function (error) {
                    console.log(error);
                },
            })
        },
        kakaoLoginCheck() {
            window.Kakao.API.request({
                url: '/v2/user/me',
                success: res => {
                    axios.get('/api/member/login', {
                            params: {
                                id: 'kakao' + res.id,
                                password: 'kakao' + res.id + 'rh7369#n',
                            }
                        })
                        .then(result => {
                            let temp = result.data;
                            let user = {
                                id: temp.id,
                                name: temp.name,
                                authority: temp.authority,
                            };
                            this.Login({
                                user
                            })
                        })
                        .then(() => {
                            this.$router.push(this.getPath)
                        })
                        .catch(err => {
                            if (err.response.data == 'ID NOT FOUND') {
                                alert('회원가입되지 않은 아이디입니다. 회원가입 페이지로 이동합니다.')
                                this.$router.push({
                                    name: "Social",
                                    params: {
                                        kakao: res,
                                    }
                                });
                            }
                        })
                },
                fail: error => {
                    this.$router.push("/authentication/signIn");
                    console.log(error);
                }
            })
        },
        naverLogin(token) {
            axios.get(`/api/member/getNaverLogin/${token}`)
                .then(res => {
                    axios.get('/api/member/login', {
                            params: {
                                id: 'naver' + res.data.response.id,
                                password: 'naver' + res.data.response.id + 'rh7369#n',
                            }
                        })
                        .then(res => {
                            let temp = res.data;
                            let user = {
                                id: temp.id,
                                name: temp.name,
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
                                alert('회원가입되지 않은 아이디입니다. 회원가입 페이지로 이동합니다.')
                                this.$router.push({
                                    name: "Social",
                                    params: {
                                        naver: res.data.response
                                    }
                                });
                            }
                        })
                })
        },
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

        // const naver_id_login = new window.naver_id_login("INmTkpuK5mPhbhHfYG_Q", "http://localhost:9000/authentication/signIn/naver");
        const naver_id_login = new window.naver_id_login("INmTkpuK5mPhbhHfYG_Q", "http://localhost:8085/authentication/signIn/naver");
        let link = document.location.href;
        if (link.indexOf('access_token') != -1) {
            this.naverLogin(naver_id_login.getAccessToken());
        } else {
            const state = naver_id_login.getUniqState();
            naver_id_login.setButton("white", 2, 40); // 버튼 설정
            naver_id_login.setState(state);
            // naver_id_login.setPopup(); // popup 설정을 위한 코드
            naver_id_login.init_naver_id_login();
        }
    }
}
</script>

<style>

</style>
