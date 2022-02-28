<template>
<v-container>
    <v-row justify="center" style="height: 970px">
        <v-col align-self="center" cols="4">
            <div class="text-h3">JOIN US</div>
            <v-divider class="my-5"></v-divider>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr>
                                <td> 아이디 </td>
                                <td>
                                    <v-text-field v-model="id" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 비밀번호 </td>
                                <td>
                                    <v-text-field v-model="pwd1" type="password" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 비밀번호 확인 </td>
                                <td>
                                    <v-text-field v-model="pwd2" type="password" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 이름 </td>
                                <td>
                                    <v-text-field v-model="name" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 주소 </td>
                                <td>
                                    <div class="d-flex ">
                                        <v-text-field v-model="zipcode" class="my-2" outlined hide-details label="우편번호" dense></v-text-field>
                                        <v-btn class="align-self-center ml-2 py-2 px-1 primary" height="100%" style="font-size:1.1rem">검색</v-btn>
                                    </div>

                                    <v-text-field v-model="addr1" class="mt-1" outlined hide-details label="기본주소" dense></v-text-field>
                                    <v-text-field v-model="addr2" class="mt-3 mb-2" outlined hide-details label="상세주소" dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 전화번호 </td>
                                <td>
                                    <v-text-field v-model="tel" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 이메일 </td>
                                <td>
                                    <v-text-field v-model="email" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
                <v-row class="mt-16">
                    <v-col class="py-0">
                        <div class="text-h5 ">약관동의</div>
                        <v-divider class="my-2"></v-divider>
                        <v-checkbox v-model="agreement1" hide-details><template v-slot:label>
                                <div class="black--text">(필수)사이트 이용 약관 동의</div>
                            </template></v-checkbox>
                        <v-checkbox v-model="agreement2" hide-details><template v-slot:label class="ma-0">
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
                    <v-btn class="primary text-h5 pa-3 ml-5" height="100%">가입하기</v-btn>
                </v-row>
            </v-form>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    methods: {
        goBack() {
            this.$router.push('/');
        }
    },
    mounted() {
        window.scrollTo(0, 0)
    }
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
