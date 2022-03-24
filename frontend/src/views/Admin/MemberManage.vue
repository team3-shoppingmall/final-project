<template>
<v-container fluid="fluid">
    <v-row justify="space-between">
        <v-col cols="2" align-self="center">
            <v-switch v-model="showManager" :label="`${showManager? '관리자':'유저'} 보기`"></v-switch>
        </v-col>
        <v-col cols="8">
            <v-row justify="center">
                <v-col cols="2" align-self="center">
                    <v-select :items="searchType" v-model="condition" hide-details="hide-details"></v-select>
                </v-col>
                <v-col cols="6" align-self="center">
                    <v-text-field hide-details="hide-details" v-model="value" @keyup.enter="searchMember"></v-text-field>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-btn class="primary " large="large" @click="searchMember">검색</v-btn>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-btn class="primary " large="large" @click="reset">초기화</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="2" align-self="center">
            <v-row justify="end">
                <v-col cols="auto">
                    <v-btn class="primary " @click="dialog3 = true" v-if="getLogin.user.id == 'spring'">관리자 추가</v-btn>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers" :options.sync="options" :items="items" :server-items-length="totalContents" :loading="loading">
                <template v-slot:[`item.id`]="{ item }">
                    <v-row justify="center">
                        <v-col cols="auto">
                            <div class="text-left text-truncate" style="max-width: 130px;">
                                {{ item.id }}
                            </div>
                        </v-col>
                    </v-row>
                </template>
                <template v-slot:[`item.tel`]="{ item }">
                    <div>{{telFormatter(item.tel)}}</div>
                </template>
                <template v-slot:[`item.btn`]="{ item }">
                    <v-btn color="primary" @click.stop="selectItem(item), dialog = true">
                        수정
                    </v-btn>
                </template>
            </v-data-table>
        </v-col>
    </v-row>
    <v-dialog v-model="dialog" width="600px">
        <v-card class="pa-2">
            <v-simple-table>
                <thead>
                    <tr>
                        <th class="text-h5" colspan="2">회원정보수정</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="text-h6">ID</td>
                        <td>
                            <v-text-field v-model="editItem.id" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">이름</td>
                        <td>
                            <v-text-field v-model="editItem.name" hide-details :readonly="editItem.id == 'spring' && getLogin.user.id != 'spring'"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">전화번호</td>
                        <td>
                            <v-text-field v-model="editItem.tel" hide-details :readonly="editItem.id == 'spring' && getLogin.user.id != 'spring'"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">이메일</td>
                        <td>
                            <v-text-field v-model="editItem.email" hide-details :readonly="editItem.id == 'spring' && getLogin.user.id != 'spring'"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">우편번호</td>
                        <td>
                            <v-text-field v-model="editItem.zipcode" hide-details :readonly="editItem.id == 'spring' && getLogin.user.id != 'spring'"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">주소1</td>
                        <td>
                            <v-text-field v-model="editItem.addr1" hide-details :readonly="editItem.id == 'spring' && getLogin.user.id != 'spring'"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">주소2</td>
                        <td>
                            <v-text-field v-model="editItem.addr2" hide-details :readonly="editItem.id == 'spring' && getLogin.user.id != 'spring'"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">포인트</td>
                        <td>
                            <v-text-field v-model="editItem.point" hide-details :readonly="getLogin.user.id != 'spring'" @change="pointCheck"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <v-row justify="space-between">
                                <v-col cols="auto">
                                    <v-btn class="error" @click="dialog2 = true" v-if="getLogin.user.id == 'spring' && editItem.id != 'spring'">탈퇴</v-btn>
                                </v-col>
                                <v-col cols="auto">
                                    <v-btn class="primary" @click="updateMember">수정</v-btn>
                                    <v-btn class="error ml-3" @click="dialog = false; editItem = {};">취소</v-btn>
                                </v-col>
                            </v-row>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog2" persistent max-width="350">
        <v-card>
            <v-card-title class="text-body-1">
                <span>정말 이 회원을 탈퇴시키시겠습니까?</span>
            </v-card-title>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text @click="deleteMember">
                    예
                </v-btn>
                <v-btn color="red darken-1" text @click="dialog2 = false">
                    아니오
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog3" persistent max-width="500">
        <v-card>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <thead>
                            <tr>
                                <th class="text-h5" colspan="2">관리자 등록</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td> 아이디 </td>
                                <td>
                                    <div class="d-flex ">
                                        <v-text-field v-model="manager.id" :rules="rules.id" outlined hide-details="auto" dense></v-text-field>
                                        <v-btn class="align-self-center ml-2 py-2 px-1 primary" height="100%" style="font-size:1.1rem" @click="idCheck">검사</v-btn>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td> 비밀번호 </td>
                                <td>
                                    <v-text-field v-model="manager.pwd1" :rules="rules.pwd1" type="password" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 비밀번호 확인 </td>
                                <td>
                                    <v-text-field v-model="manager.pwd2" :rules="rules.pwd2" type="password" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 이름 </td>
                                <td>
                                    <v-text-field v-model="manager.name" :rules="rules.name" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 전화번호 </td>
                                <td>
                                    <v-text-field v-model="manager.tel" :rules="rules.tel" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <v-row justify="end">
                                        <v-col cols="auto">
                                            <v-btn class="primary" @click="addManager">등록</v-btn>
                                            <v-btn class="error ml-3" @click="managerReset">취소</v-btn>
                                        </v-col>
                                    </v-row>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
            </v-form>
        </v-card>
    </v-dialog>
    <v-dialog v-model="alertDialog" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
        </v-alert>
    </v-dialog>
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
        getAllMembers() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options;

            axios
                .get('/api/member/getMembers', {
                    params: {
                        page: page,
                        perPage: itemsPerPage,
                        condition: this.condition,
                        param: this.value,
                        role: this.showManager,
                    }
                })
                .then(res => {
                    this.items = res.data.res;
                    this.totalContents = res.data.count;
                })
                .catch(err => {
                    console.log(err.response.status);
                })
                .finally(this.loading = false);
        },
        searchMember() {
            if (this.options.page != 1) {
                this.options.page = 1
            } else {
                this.getAllMembers();
            }
        },
        telFormatter(tel) {
            let first;
            let second;
            let third;
            if (tel.length == 11) {
                first = tel.substr(0, 3);
                second = tel.substr(3, 4);
                third = tel.substr(7, 4);
                return `${first}-${second}-${third}`;
            } else if (tel.substr(0, 2) == '02' && tel.length == 10) {
                first = tel.substr(0, 2);
                second = tel.substr(2, 4);
                third = tel.substr(6, 4);
                return `${first}-${second}-${third}`;
            } else if (tel.substr(0, 2) == '02' && tel.length == 9) {
                first = tel.substr(0, 2);
                second = tel.substr(2, 3);
                third = tel.substr(5, 4);
                return `${first}-${second}-${third}`;
            } else if (tel.length == 10) {
                first = tel.substr(0, 3);
                second = tel.substr(3, 3);
                third = tel.substr(6, 4);
                return `${first}-${second}-${third}`;
            } else {
                return tel;
            }
        },
        selectItem(item) {
            let temp = this.items[
                this
                .items
                .indexOf(item)
            ];
            this.editItem = {
                id: temp.id,
                name: temp.name,
                tel: temp.tel,
                email: temp.email,
                zipcode: temp.zipcode,
                addr1: temp.addr1,
                addr2: temp.addr2,
                point: temp.point,
                pointTemp: temp.point,
                authority: temp.authority
            }
        },
        updateMember() {
            axios.put('/api/member/updateMember', this.editItem)
                .then(() => {
                    this.getAllMembers();
                    this.dialog = false;
                })
        },
        deleteMember() {
            axios.delete(`/api/member/delete/${this.id}`)
                .then(() => {
                    this.alertDialog = true;
                    this.alertType = 'success';
                    this.alertMessage = '탈퇴가 완료되었습니다';
                    this.editItem = {};
                    this.dialog2 = false;
                    this.dialog = false;
                    this.getAllMembers();
                })
                .catch(() => {
                    this.alertDialog = true;
                    this.alertType = 'warning';
                    this.alertMessage = '배송중인 상품이 있어 탈퇴에 실패했습니다';
                })
        },
        reset() {
            this.condition = 'id';
            this.value = null;
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.getAllMembers();
        },
        addManager() {
            let validate = this.$refs.form.validate();
            if (!this.check) {

                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '아이디 중복검사를 해야합니다';
            } else if (validate) {
                let member = {
                    id: this.manager.id,
                    password: this.manager.pwd1,
                    name: this.manager.name,
                    tel: this.manager.tel,
                    authority: 'ROLE_ADMIN',
                }
                axios.post('/api/member/insert', member)
                    .then(() => {
                        this.alertDialog = true;
                        this.alertType = 'success';
                        this.alertMessage = '관리자 등록 완료';
                        this.managerReset();
                        this.getAllMembers();
                    }).catch(() => {
                        this.alertDialog = true;
                        this.alertType = 'error';
                        this.alertMessage = '등록 실패';
                    })
            }
        },
        idCheck() {
            if (this.manager.id == null) {
                return;
            } else if (this.manager.id.slice(0, 5) == 'kakao' || this.manager.id.slice(0, 5) == 'naver') {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '사용 불가합니다';
                this.check = false;
            } else {
                axios.get(`/api/member/check/${this.manager.id}`)
                    .then(() => {
                        this.alertDialog = true;
                        this.alertType = 'success';
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
        pointCheck() {
            if (!(this.editItem.point >= 0 && (this.editItem.point == Math.round(this.editItem.point)) && this.editItem.point != '')) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '포인트가 유효하지 않습니다';
                this.editItem.point = this.editItem.pointTemp;
                return;
            }
        },
        managerReset() {
            this.manager = {
                id: '',
                pwd1: '',
                pwd2: '',
                name: '',
                tel: '',
            }
            this.dialog3 = false
        }

    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: { //변수 값이 변경될 때 연산을 처리하거나 변수 값에 따라 화면을 제어할 때 사용
        options: {
            handler() {
                this.getAllMembers();
            },
            deep: true,
        },
        search: {
            handler() {
                this.value = '';
            }
        },
        showManager: {
            handler() {
                this.searchMember();
            }
        }
    },
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            dialog: false,
            dialog2: false,
            dialog3: false,
            showManager: false,
            check: false,
            totalContents: 0,
            loading: false,
            editItem: {},
            condition: 'id',
            value: '',
            searchType: [{
                text: 'ID',
                value: 'id'
            }, {
                text: '이름',
                value: 'name'
            }, {
                text: '전화번호',
                value: 'tel'
            }, {
                text: '이메일',
                value: 'email'
            }, {
                text: '우편번호',
                value: 'zipcode'
            }, {
                text: '주소1',
                value: 'addr1'
            }, {
                text: '주소2',
                value: 'addr2'
            }, {
                text: '포인트',
                value: 'point'
            }],
            options: {},
            headers: [{
                text: 'ID',
                value: 'id',
                divider: true,
                align: 'center',
                width: '10%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '이름',
                value: 'name',
                divider: true,
                align: 'center',
                width: '7%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '전화번호',
                value: 'tel',
                divider: true,
                align: 'center',
                width: '10%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '이메일',
                value: 'email',
                divider: true,
                align: 'center',
                width: '12%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '우편번호',
                value: 'zipcode',
                divider: true,
                align: 'center',
                width: '7%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '주소1',
                value: 'addr1',
                divider: true,
                align: 'center',
                width: '22%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '주소2',
                value: 'addr2',
                divider: true,
                align: 'center',
                width: '12%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '수신여부',
                value: 'terms',
                divider: true,
                align: 'center',
                width: '8%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '포인트',
                value: 'point',
                divider: true,
                align: 'center',
                width: '6%',
                sortable: false,
                class: 'text-subtitle-1'
            }, {
                text: '수정',
                value: 'btn',
                align: 'center',
                width: '6%',
                sortable: false,
                class: 'text-subtitle-1'
            }],
            items: [],
            manager: {
                id: '',
                pwd1: '',
                pwd2: '',
                name: '',
                tel: '',
            },
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
                    v => v === this.manager.pwd1 || '비밀번호가 일치하지 않습니다.'
                ],
                name: [v => !!v || '이름은 필수 입력사항입니다.', ],
                tel: [v => !!v || '전화번호는 필수 입력사항입니다.',
                    v => /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/.test(v) || '전화번호는 숫자만 입력 가능합니다.',
                ],
            },
        }
    }
}
</script>

<style scoped>
.v-small-dialog__menu-content {
    background-color: black !important;
    position: absolute;
    left: 50%;
}
</style>
