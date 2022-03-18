<template>
<v-container>
    <v-row justify="center" style="min-height: 970px">
        <v-col align-self="center" cols="4">
            <div class="text-h3">JOIN US</div>
            <v-divider class="mt-5 mb-0"></v-divider>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr v-if="naverInfo == undefined && kakaoInfo == undefined">
                                <td> 아이디 </td>
                                <td>
                                    <div class="d-flex ">
                                        <v-text-field v-model="id" :rules="rules.id" outlined hide-details="auto" dense></v-text-field>
                                        <v-btn class="align-self-center ml-2 py-2 px-1 primary" height="100%" style="font-size:1.1rem" @click="idCheck">검사</v-btn>
                                    </div>
                                </td>
                            </tr>
                            <tr v-if="naverInfo == undefined && kakaoInfo == undefined">
                                <td> 비밀번호 </td>
                                <td>
                                    <v-text-field v-model="pwd1" :rules="rules.pwd1" type="password" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr v-if="naverInfo == undefined && kakaoInfo == undefined">
                                <td> 비밀번호 확인 </td>
                                <td>
                                    <v-text-field v-model="pwd2" :rules="rules.pwd2" type="password" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 이름 </td>
                                <td>
                                    <v-text-field v-model="name" :rules="rules.name" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 주소 </td>
                                <td>
                                    <div class="d-flex ">
                                        <v-text-field v-model="zipcode" :rules="rules.zipcode" class="my-2" outlined hide-details="auto" label="우편번호" dense required readonly @click="execDaumPostcode"></v-text-field>
                                        <v-btn class="align-self-center ml-2 py-2 px-1 primary" height="100%" style="font-size:1.1rem" @click="execDaumPostcode">검색</v-btn>
                                    </div>

                                    <v-text-field v-model="addr1" :rules="rules.addr1" class="mt-1" outlined hide-details="auto" label="기본주소" dense required readonly @click="execDaumPostcode"></v-text-field>
                                    <v-text-field v-model="addr2" :rules="rules.addr2" class="mt-3 mb-2" outlined hide-details="auto" label="상세주소" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 전화번호 </td>
                                <td>
                                    <v-text-field v-model="tel" :rules="rules.tel" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 이메일 </td>
                                <td>
                                    <v-text-field v-model="email" :rules="rules.email" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
                <v-row class="mt-16">
                    <v-col class="py-0">
                        <div class="text-h5 ">약관동의</div>
                        <v-divider class="my-2"></v-divider>
                        <v-checkbox v-model="agreement1" hide-details :rules="rules.agreement1"><template v-slot:label required>
                                <div class="black--text">(필수)사이트 이용 약관 동의</div>
                            </template></v-checkbox>
                        <v-checkbox v-model="agreement2" hide-details :rules="rules.agreement2"><template v-slot:label class="ma-0" required>
                                <div class="black--text">(필수)개인정보 수집 및 이용 동의</div>
                            </template></v-checkbox>
                        <v-checkbox v-model="agreement3" hide-details><template v-slot:label class="ma-0">
                                <div class="black--text">(선택)광고성 정보 수신 동의</div>
                            </template></v-checkbox>
                    </v-col>
                </v-row>
                <v-divider class="mt-8"></v-divider>
                <v-row class="my-5" justify="center">
                    <v-btn class="primary text-h5 pa-3" height="100%" @click="goBack">돌아가기</v-btn>
                    <v-btn class="primary text-h5 pa-3 ml-5" height="100%" @click="signUp" v-if="naverInfo == undefined && kakaoInfo == undefined">가입하기</v-btn>
                    <v-btn class="primary text-h5 pa-3 ml-5" height="100%" @click="signUpBySocial" v-if="naverInfo != undefined || kakaoInfo != undefined">가입하기</v-btn>
                </v-row>
            </v-form>
        </v-col>
    </v-row>
    <v-dialog v-model="alertDialog" :persistent="alertPath != null" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
            <v-row justify="end" v-if="alertPath != null">
                <v-col cols="auto" class="pr-1 pb-1">
                    <v-btn text :to="alertPath">확인</v-btn>
                </v-col>
            </v-row>
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    methods: {
        goBack() {
            this.$router.push('/');
        },
        signUp() {
            let validate = this.$refs.form.validate();
            if (!this.check) {

                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '아이디 중복검사를 해야합니다';
            } else if (validate) {
                let member = {
                    id: this.id,
                    password: this.pwd1,
                    name: this.name,
                    zipcode: this.zipcode,
                    addr1: this.addr1,
                    addr2: this.addr2,
                    tel: this.tel,
                    email: this.email,
                    terms: this.agreement3,
                }
                axios.post('/api/member/insert', member)
                    .then(() => {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '가입 완료';
                        this.alertPath = `/`;
                    }).catch(() => {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '가입 실패';
                    })
            }
        },
        signUpBySocial() {
            let validate = this.$refs.form.validate();
            if (validate) {
                let tempId = null;
                if (this.naverInfo != undefined) {
                    tempId = 'naver' + this.naverInfo.id;
                } else if (this.kakaoInfo != undefined) {
                    tempId = 'kakao' + this.kakaoInfo.id;
                }
                let member = {
                    id: tempId,
                    password: tempId + 'rh7369#n',
                    name: this.name,
                    zipcode: this.zipcode,
                    addr1: this.addr1,
                    addr2: this.addr2,
                    tel: this.tel,
                    email: this.email,
                    terms: this.agreement3,
                }
                axios.post('/api/member/insert', member)
                    .then(() => {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '가입 완료';
                        this.alertPath = `/`;
                    }).catch(() => {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '가입 실패';
                    })
            }
        },
        idCheck() {
            if (this.id.slice(0, 5) == 'kakao' || this.id.slice(0, 5) == 'naver') {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '사용 불가합니다';
                this.check = false;
            } else {
                axios.get(`/api/member/check/${this.id}`)
                    .then(() => {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '사용 가능합니다';
                        this.check = true;

                    })
                    .catch(() => {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '사용 불가합니다';
                        this.check = false;

                    })
            }
        },
        execDaumPostcode() {
            new window.daum.Postcode({
                oncomplete: (data) => {
                    if (this.extraAddress !== "") {
                        this.addr2 = "";
                    }
                    if (data.userSelectedType === "R") {
                        // 사용자가 도로명 주소를 선택했을 경우
                        this.addr1 = data.roadAddress;
                    } else {
                        // 사용자가 지번 주소를 선택했을 경우(J)
                        this.addr1 = data.jibunAddress;
                    }
                    // // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                    // if (data.userSelectedType === "R") {
                    //     // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    //     // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    //     if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
                    //         // this.addr2 += data.bname;
                    //     }
                    //     // 건물명이 있고, 공동주택일 경우 추가한다.
                    //     if (data.buildingName !== "" && data.apartment === "Y") {
                    //         this.addr2 +=
                    //             this.addr2 !== "" ?
                    //             `, ${data.buildingName}` :
                    //             data.buildingName;
                    //     }
                    //     // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    //     if (this.addr2 !== "") {
                    //         this.addr2 = `(${this.addr2})`;
                    //     }
                    // } else {
                    //     this.addr2 = "";
                    // }
                    // 우편번호를 입력한다.
                    this.zipcode = data.zonecode;
                },
            }).open();
        }
    },
    mounted() {
        this.$vuetify.goTo(0);
        this.naverInfo = this.$route.params.naver;
        this.kakaoInfo = this.$route.params.kakao;
        if (this.naverInfo != undefined) {
            // 개인정보 때문에 전화번호는 제거함
            this.name = this.naverInfo.name;
            this.email = this.naverInfo.email;
        } else if (this.kakaoInfo != undefined) {
            // 개인정보 때문에 전화번호는 제거함            
            this.name = this.kakaoInfo.kakao_account.profile.nickname;
        }
    },
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            alertPath: null,
            id: '',
            pwd1: '',
            pwd2: '',
            name: '',
            zipcode: '',
            addr1: '',
            addr2: '',
            tel: '',
            email: '',
            agreement1: '',
            agreement2: '',
            agreement3: '',
            naverInfo: '',
            check: false,
            rules: {
                id: [v => !!v || '아이디는 필수 입력사항입니다.',
                    v => /^[a-zA-Z0-9]*$/.test(v) || '아이디는 영문+숫자만 입력 가능합니다.',
                    v => v.length >= 4 || '최소 4글자입니다.',
                    v => v.length <= 24 || '최대 24글자입니다.',
                ],
                pwd1: [v => !!v || '비밀번호는 필수 입력사항입니다.',
                    v => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^*+=-]).{0,}$/.test(v) || '비밀번호는 영문 대소문자, 숫자, 특수문자로 구성돼야합니다.',
                    v => v.length >= 8 || '최소 8글자입니다.',
                    v => v.length <= 20 || '최대 20글자입니다.',
                ],
                pwd2: [v => !!v || '비밀번호확인은 필수 입력사항입니다.',
                    v => v === this.pwd1 || '비밀번호가 일치하지 않습니다.'
                ],
                name: [v => !!v || '이름은 필수 입력사항입니다.', ],
                zipcode: [v => !!v || '우편번호는 필수 입력사항입니다.', ],
                addr1: [v => !!v || '기본주소는 필수 입력사항입니다.', ],
                addr2: [v => !!v || '상세주소는 필수 입력사항입니다.', ],
                tel: [v => !!v || '전화번호는 필수 입력사항입니다.',
                    v => /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/.test(v) || '전화번호는 숫자만 입력 가능합니다.',
                ],
                email: [v => !!v || '이메일는 필수 입력사항입니다.',
                    v => /^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\.[A-Za-z0-9\\-]+/.test(v) || '옳바른 이메일 양식을 입력해야합니다.'
                ],
                agreement1: [v => !!v],
                agreement2: [v => !!v],
            },
        }
    },
}
</script>

<style scoped>
.v-text-field {
    margin: 5px 0px;
}

.v-data-table>.v-data-table__wrapper>table>tbody>tr>td {
    font-size: 1rem;
}
</style>
