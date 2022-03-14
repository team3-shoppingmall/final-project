<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="1" align-self="center">
            <v-select :items="searches" v-model="search" hide-details="hide-details"></v-select>
        </v-col>
        <v-col cols="4" align-self="center" v-if="search == 'orderIdx' || search == 'productNo' || search == 'productName'">
            <v-text-field hide-details="hide-details" v-model="searchWord1" @keyup.enter="searchOrder"></v-text-field>
        </v-col>

        <v-col cols="2" align-self="center" v-if="search == 'orderDate'">
            <v-menu v-model="menu1" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="searchWord1" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                </template>
                <v-date-picker v-model="searchWord1" @input="menu1 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
            </v-menu>
        </v-col>
        <v-col cols="2" align-self="center" v-if="search == 'orderDate'">
            <v-menu v-model="menu2" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="searchWord2" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                </template>
                <v-date-picker v-model="searchWord2" @input="menu2 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
            </v-menu>
        </v-col>

        <v-col cols="4" align-self="center" v-if="search == 'state'">
            <v-select v-model="stateSelected" :items="states" hide-details></v-select>
        </v-col>

        <v-col cols="auto" align-self="center">
            <v-btn class="primary" large="large" @click="searchOrder">검색</v-btn>
        </v-col>
        <v-col cols="auto" align-self="center">
            <v-btn class="primary" large="large" @click="reset">초기화</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers" :options.sync="options" :items="orders" item-key="orderIdx" :server-items-length="totalContents" :loading="loading" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
                <template #[`item.productName`]="{item}">
                    <v-btn text :to="`/productDetail/${item.productNo}`">
                        <div class="text-truncate" style="max-width: 250px;">
                            {{ item.productName }}
                        </div>
                    </v-btn>
                </template>
                <template #[`item.imageName`]="{item}">
                    <v-row justify="center">
                        <v-col cols="auto">
                            <v-img min-height="100" max-height="100" max-width="100" :src="`/api/product/productImage/${item.productNo}/${item.imageName.split(';')[0]}`"></v-img>
                        </v-col>
                    </v-row>
                </template>
                <template #[`item.totalPrice`]="{item}">
                    {{ AddComma(item.totalPrice) }}원
                </template>
                <template #[`item.orderDate`]="{item}">
                    <div>
                        <DateDisplay :regDate="item.orderDate" />
                    </div>
                </template>
                <template v-slot:[`item.id`]="{item}">
                    <v-btn text @click.stop="selectItem(item), dialog = true">
                        <div class="text-truncate" style="max-width: 100px;">
                            {{ item.id }}
                        </div>
                    </v-btn>
                </template>
                <template v-slot:[`item.stateChange`]="{index}">
                    <v-select :items="states" v-model="stateChanges[index]" item-key="value" hide-details="hide-details"></v-select>
                </template>
                <template v-slot:[`header.stateChange`]="{}">
                    <v-row class="ma-0" justify="center">
                        <v-col cols="auto">
                            <v-btn class="primary" @click="stateChange">상태 변경</v-btn>
                        </v-col>
                    </v-row>
                </template>
            </v-data-table>
        </v-col>
    </v-row>
    <v-dialog v-model="dialog" width="600px">
        <v-card class="pa-2">
            <v-simple-table>
                <thead>
                    <tr>
                        <th class="text-h5" colspan="2">배송지 확인</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="text-h6">ID</td>
                        <td>
                            <v-text-field v-model="memberInfo.id" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">이름</td>
                        <td>
                            <v-text-field v-model="memberInfo.name" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">전화번호</td>
                        <td>
                            <v-text-field v-model="memberInfo.tel" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">우편번호</td>
                        <td>
                            <v-text-field v-model="memberInfo.zipCode" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">기본주소</td>
                        <td>
                            <v-text-field v-model="memberInfo.address" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">상세주소</td>
                        <td>
                            <v-text-field v-model="memberInfo.detailAddr" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <v-row justify="end">
                                <v-col cols="auto">
                                    <v-btn class="primary" @click="dialog = false">확인</v-btn>
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
import DateDisplay from '@/components/DateDisplay.vue'
export default {
    components: {
        DateDisplay,
    },
    data() {
        return {
            orders: [],
            totalContents: 0,
            loading: false,
            options: {},
            headers: [{
                text: '주문번호',
                value: 'orderIdx',
                divider: true,
                align: 'center',
                width: '5%',
            }, {
                text: '상품번호',
                value: 'productNo',
                divider: true,
                align: 'center',
                width: '5%',
            }, {
                text: '이미지',
                value: 'imageName',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '20%',
            }, {
                text: '가격',
                value: 'totalPrice',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '개수',
                value: 'orderAmount',
                divider: true,
                align: 'center',
                width: '5%',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
                divider: true,
                align: 'center',
                width: '15%',
            }, {
                text: '구매자(배송지)',
                value: 'id',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '주문 상태',
                value: 'state',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '상태 변경',
                value: 'stateChange',
                align: 'center',
                width: '10%',
            }, ],

            searches: [{
                text: '주문번호',
                value: 'orderIdx',
            }, {
                text: '상품번호',
                value: 'productNo',
            }, {
                text: '상품명',
                value: 'productName',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
            }, {
                text: '주문 상태',
                value: 'state',
            }, ],
            search: 'orderIdx',
            searchWord1: '',
            searchWord2: '',

            states: [{
                text: '기준 선택',
                value: null,
            }, {
                text: '입금전',
                value: '입금전',
            }, {
                text: '결제완료',
                value: '결제완료',
            }, {
                text: '배송준비중',
                value: '배송준비중',
            }, {
                text: '배송중',
                value: '배송중',
            }, {
                text: '배송완료',
                value: '배송완료',
            }, {
                text: '구매확정',
                value: '구매확정',
            }, {
                text: '취소완료',
                value: '취소완료',
            }, {
                text: '교환완료',
                value: '교환완료',
            }, {
                text: '환불완료',
                value: '환불완료',
            }, ],
            stateSelected: null,

            stateChanges: [],

            menu1: false,
            menu2: false,

            dialog: false,
            memberInfo: {},
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        searchOrder() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                method: 'get',
                url: `/api/order/getOrder`,
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    state: this.stateSelected,
                    search: this.search,
                    searchWord1: this.searchWord1,
                    searchWord2: this.searchWord2,
                }
            }).then(res => {
                this.orders = res.data.orderList;
                this.totalContents = res.data.count;
                for (let i = 0; i < this.orders.length; i++) {
                    this.stateChanges.push(this.orders[i].state);
                }
            }).catch((err) => {
                this.orders = [];
                this.totalContents = 0;
                console.log(err);
            }).finally(
                this.loading = false
            )
        },
        reset() {
            this.searchWord1 = null;
            this.searchWord2 = null;
            this.stateSelected = null;
            if (this.search == 'orderDate') {
                let date = new Date();
                this.searchWord1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
                this.searchWord2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
            }
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.searchOrder();
        },
        selectItem(item) {
            this.memberInfo = {
                id: item.id,
                name: item.name,
                tel: item.tel,
                zipCode: item.zipCode,
                address: item.address,
                detailAddr: item.detailAddr,
            }
        },
        stateChange() {
            let states = [];
            for (let i = 0; i < this.orders.length; i++) {
                if (this.orders[i].state != this.stateChanges[i]) {
                    if (this.stateChanges[i] == null) {
                        alert('선택되지 않은 상태가 있습니다.');
                        return;
                    }
                    if (this.orders[i].state == '구매확정' || this.orders[i].state == '취소완료' || this.orders[i].state == '교환완료' || this.orders[i].state == '환불완료') {
                        alert(`주문번호 : ${this.orders[i].orderIdx}\n구매 확정, 취소 완료, 교환 완료, 환불 완료된 주문은 변경이 불가능합니다.`);
                        continue;
                    }
                    let data = {
                        orderIdx: this.orders[i].orderIdx,
                        state: this.stateChanges[i],
                    }
                    states.push(data);
                }
            }
            if (states.length == 0) {
                alert('수정할 사항이 없습니다');
                return;
            }
            axios.patch(`/api/order/update`, states)
                .then(res => {
                    if (res.data.length == 0) {
                        alert('변경하셨습니다');
                        this.searchOrder();
                    } else {
                        alert(`미완료된 변경(총 ${res.data.length}건)\n주문번호 : ${res.data}`)
                    }
                }).catch(err => {
                    console.log(err)
                })

        },
    },
    watch: { //변수 값이 변경될 때 연산을 처리하거나 변수 값에 따라 화면을 제어할 때 사용
        options: {
            handler() {
                this.searchOrder();
            },
            deep: true,
        },
        search: {
            handler() {
                this.searchWord1 = '';
                this.searchWord2 = '';
                this.stateSelected = null;
                if (this.search == 'orderDate') {
                    let date = new Date();
                    this.searchWord1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
                    this.searchWord2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
                }
            }
        },

    },
}
</script>

<style scoped>
</style>
