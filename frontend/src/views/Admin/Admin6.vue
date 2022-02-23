<template>
<v-container fluid="fluid">
    <v-row>
        <v-col cols="2" align-self="center">
            <v-select :items="searchType" v-model="condition" hide-details="hide-details"></v-select>
        </v-col>
        <v-col cols="4" align-self="center">
            <v-text-field hide-details="hide-details" v-model="value" @keyup.enter="searchMember"></v-text-field>
        </v-col>
        <v-col cols="auto" align-self="center">
            <v-btn class="primary " large="large" @click="searchMember">검색</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers" :items="items">
                <template v-slot:[`item.tel`]="{ item }">
                    <div>{{telFormatter(item.tel)}}</div>
                </template>
                 <template v-slot:[`item.btn`]="{ }">
                   <v-dialog></v-dialog>
                </template>
            </v-data-table>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    methods: {
        getAllMembers() {
            axios
                .get('/api/member/getAllMembers')
                .then(res => {
                    this.items = res.data;
                })
                .catch(err => {
                    console.log(err.response.status);
                })
        },
        searchMember() {
            console.log(this.condition, this.value);
            axios
                .get('/api/member/getMembers', {
                    params: {
                        condition: this.condition,
                        param: this.value,
                    }
                })
                .then(res => {
                    this.items = res.data;
                })
                .catch(err => {
                    console.log(err.response.status);
                })
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
            } else if (tel.length == 10) {
                first = tel.substr(0, 3);
                second = tel.substr(3, 3);
                third = tel.substr(6, 4);
                return `${first}-${second}-${third}`;
            } else {
                return tel;
            }
        }
    },
    mounted() {
        this.getAllMembers()
    },
    data() {
        return {
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
            },  {
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
            }, {
                text: '이름',
                value: 'name',
                divider: true,
                align: 'center',
                width: '7%',
                sortable: false,
            }, {
                text: '전화번호',
                value: 'tel',
                divider: true,
                align: 'center',
                width: '10%',
                sortable: false,
            }, {
                text: '이메일',
                value: 'email',
                divider: true,
                align: 'center',
                width: '18%',
                sortable: false,
            }, {
                text: '우편번호',
                value: 'zipcode',
                divider: true,
                align: 'center',
                width: '9%',
                sortable: false,
            }, {
                text: '주소1',
                value: 'addr1',
                divider: true,
                align: 'center',
                width: '16%',
                sortable: false,
            }, {
                text: '주소2',
                value: 'addr2',
                divider: true,
                align: 'center',
                width: '16%',
                sortable: false,
            }, {
                text: '수신여부',
                value: 'terms',
                divider: true,
                align: 'center',
                width: '6%',
                sortable: false,
            }, {
                text: '포인트',
                value: 'point',
                divider: true,
                align: 'center',
                width: '6%',
            }, {
                text: '수정',
                  value: 'btn',
                align: 'center',
                width: '5%',
                sortable: false,
            }],
            items: []
        }
    }
}
</script>

<style></style>
