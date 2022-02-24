<template>
<v-container fluid="fluid">
    <v-row justify="center">
        <v-col cols="2" align-self="center">
            <v-select :items="searchType" v-model="condition" hide-details="hide-details"></v-select>
        </v-col>
        <v-col cols="3" align-self="center">
            <v-text-field hide-details="hide-details" v-model="value" @keyup.enter="searchMember"></v-text-field>
        </v-col>
        <v-col cols="auto" align-self="center">
            <v-btn class="primary " large="large" @click="searchMember">검색</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers" :options.sync="options" :items="items" :server-items-length="totalContents" :loading="loading">
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
        <v-card class="pa-2" >
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
                            <v-text-field v-model="editItem.name" hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">전화번호</td>
                        <td>
                            <v-text-field v-model="editItem.tel" hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">이메일</td>
                        <td>
                            <v-text-field v-model="editItem.email" hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">우편번호</td>
                        <td>
                            <v-text-field v-model="editItem.zipcode" hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">주소1</td>
                        <td>
                            <v-text-field v-model="editItem.addr1" hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">주소2</td>
                        <td>
                            <v-text-field v-model="editItem.addr2" hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">포인트</td>
                        <td>
                            <v-text-field v-model="editItem.point" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <v-row justify="end">
                                <v-col cols="auto">
                                    <v-btn class="error" @click="dialog = false">취소</v-btn>
                                    <v-btn class="primary ml-3" @click="updateMember">수정</v-btn>
                                </v-col>
                            </v-row>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
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
                        param: this.value
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
            console.log(item)
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
                point: temp.point
            }
        },
        updateMember() {
            axios.put('/api/member/updateMember', this.editItem)
                .then(() => {
                    this.getAllMembers();
                    this.dialog = false;
                })
        },

    },
    watch: { //변수 값이 변경될 때 연산을 처리하거나 변수 값에 따라 화면을 제어할 때 사용
        options: {
            handler() {
                this.getAllMembers();
            },
            deep: true,
        },
    },
    // mounted() {
    //     this.getAllMembers();
    // },
    data() {
        return {
            dialog: false,
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
                width: '7%',
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
                width: '15%',
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
            items: []
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
