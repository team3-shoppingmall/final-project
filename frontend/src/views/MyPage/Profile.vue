<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="9">
            <v-divider class="my-5"></v-divider>
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col align-self="center" cols="5">
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr>
                                <td>
                                    아이디
                                </td>
                                <td>
                                    <v-text-field v-model="id" outlined="outlined" hide-details="hide-details" dense="dense"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    비밀번호
                                </td>
                                <td>
                                    <v-text-field v-model="pwd1" type="password" outlined="outlined" hide-details="hide-details" dense="dense"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    비밀번호 확인
                                </td>
                                <td>
                                    <v-text-field v-model="pwd2" type="password" outlined="outlined" hide-details="hide-details" dense="dense"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    이름
                                </td>
                                <td>
                                    <v-text-field v-model="name" outlined="outlined" hide-details="hide-details" dense="dense"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    주소
                                </td>
                                <td>
                                    <div class="d-flex ">
                                        <v-text-field v-model="zipcode" outlined="outlined" hide-details="hide-details" label="우편번호" dense="dense"></v-text-field>
                                        <v-btn class="align-self-center ml-2 pa-2 primary" height="100%" style="font-size:1rem">검색</v-btn>
                                    </div>
                                    <v-text-field v-model="addr1" class="mt-1" outlined="outlined" hide-details="hide-details" label="기본주소" dense="dense"></v-text-field>
                                    <v-text-field v-model="addr2" class="mt-2" outlined="outlined" hide-details="hide-details" label="상세주소" dense="dense"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    전화번호
                                </td>
                                <td>
                                    <v-text-field v-model="tel" outlined="outlined" hide-details="hide-details" dense="dense"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    이메일
                                </td>
                                <td>
                                    <v-text-field v-model="email" outlined="outlined" hide-details="hide-details" dense="dense"></v-text-field>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
                <v-divider class="mt-8"></v-divider>
                <v-row class="my-5" justify="center">
                    <v-btn class="primary text-h5 pa-3 " height="100%" width="120px">취소</v-btn>
                    <v-btn class="primary text-h5 pa-3 ml-5" height="100%" width="120px" @click="update">수정</v-btn>
                </v-row>
            </v-form>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    methods: {
        getData() {
            axios.get(`/api/member/getMemberInfo/${this.getLogin.user.id}`)
                .then(res => {
                    this.id = this.getLogin.user.id;
                    this.name = res.data.name;
                    this.zipcode = res.data.zipcode;
                    this.addr1 = res.data.addr1;
                    this.addr2 = res.data.addr2;
                    this.tel = res.data.tel;
                    this.email = res.data.email;
                })
        },
        update() {
            let member;
            if (this.pwd1 != null)
                if (this.pwd1 == this.pwd2) {
                    member = {
                        id: this.id,
                        password: this.pwd1,
                        name: this.name,
                        zipcode: this.zipcode,
                        addr1: this.addr1,
                        addr2: this.addr2,
                        tel: this.tel,
                        email: this.email,
                    }
                }
            else {
                member = {
                    id: this.id,
                    name: this.name,
                    zipcode: this.zipcode,
                    addr1: this.addr1,
                    addr2: this.addr2,
                    tel: this.tel,
                    email: this.email,
                }
            }
            console.log(member)
            axios.put('/api/member/updateMember', member)
                .then(() => {
                    alert("수정 성공")
                    this.getData();
                })
                .catch(() => alert("수정 실패"))
        }
    },
    components: {},
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    mounted() {
        console.log(this.getLogin)
        if (this.getLogin) {
            this.getData();
        }
    },
    data() {
        return {
            id: '',
            pwd1: '',
            pwd2: '',
            name: '',
            zipcode: '',
            addr1: '',
            addr2: '',
            tel: '',
            email: '',
        }
    }
}
</script>

<style scoped>
.v-text-field {
    margin: 5px 0;
}
</style>
