<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="11">
            <v-row>
                <v-col cols="auto">
                    <v-btn :color="colorPicker('주문 내역조회')" @click="selectOrder('주문 내역조회')" width="240px">주문 내역조회</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :color="colorPicker('취소/반품/교환 내역조회')" @click="selectOrder('취소/반품/교환 내역조회')" width="240px">취소/반품/교환 내역조회</v-btn>
                </v-col>
            </v-row>
            <v-row justify="center">
                <v-col align-self="center" cols="auto">
                    <v-btn class="primary mr-2" @click="changeDate('today')">오늘</v-btn>
                    <v-btn class="primary mr-2" @click="changeDate('1week')">1주일</v-btn>
                    <v-btn class="primary mr-2" @click="changeDate('1month')">1개월</v-btn>
                    <v-btn class="primary mr-2" @click="changeDate('3month')">3개월</v-btn>
                    <v-btn class="primary" @click="changeDate('6month')">6개월</v-btn>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-menu v-model="menu1" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field v-model="searchDate1" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                        </template>
                        <v-date-picker v-model="searchDate1" @input="menu1 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
                    </v-menu>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-menu v-model="menu2" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field v-model="searchDate2" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                        </template>
                        <v-date-picker v-model="searchDate2" @input="menu2 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
                    </v-menu>
                </v-col>
            </v-row>
            <v-row justify="center">
                <v-col cols="3" align-self="center">
                    <v-select v-model="stateSelected" :items="states" hide-details></v-select>
                </v-col>
                <v-col cols="7" align-self="center">
                    <v-text-field hide-details="hide-details" v-model="searchWord" @keyup.enter="searchOrder(selectedOrder)"></v-text-field>
                </v-col>
                <v-col cols="2" align-self="center">
                    <v-btn class="primary mr-2" @click="searchOrder(selectedOrder)">조회</v-btn>
                    <v-btn class="primary" @click="reset">초기화</v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12">
                    <v-data-table :headers="headers" :items="orders" :options.sync="options" item-key="orderIdx" :server-items-length="totalContents" :loading="loading" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
                        <template #top="{ }">
                            <div class="text-h5 pa-3">{{selectedOrder}}</div>
                        </template>
                        <template #[`item.productName`]="{item}">
                            <v-btn text :to="`/productDetail/${item.productNo}`">
                                <div class="text-left text-truncate" style="max-width: 190px;">
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
                        <template #[`item.delivery`]="{item}">
                            <v-btn color="primary" @click="selectItem(item), dialog4 = true">
                                배송정보
                            </v-btn>
                        </template>
                        <template v-slot:[`item.btn`]="{item}">
                            <v-btn color="primary" @click="selectItem(item), dialog2 = true" v-if="selectedOrder=='주문 내역조회' && (item.state == '입금전' || item.state == '결제완료' || item.state == '배송준비중')">
                                취소
                            </v-btn>
                            <v-btn color="primary" @click.stop="selectItem(item), dialog = true" v-if="selectedOrder=='주문 내역조회' && item.state == '배송완료'">
                                구매 확정
                            </v-btn>
                            <v-btn color="primary" @click="cancelOrder(item)" v-if="item.state == '배송완료'" class="mt-1">
                                환불 및 교환
                            </v-btn>
                            <v-btn color="primary" @click.stop="selectItem(item), dialog3 = true" v-if="item.reviewable">
                                리뷰 작성하기
                            </v-btn>
                        </template>
                    </v-data-table>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-dialog v-model="dialog" persistent max-width="290" v-if="purchaseItem != null">
        <v-card>
            <v-card-title class="text-body-1">
                <span>구매 확정 시 포인트가 적립되며 </span>
                <span> 반품 및 교환이 <span class="red--text">불가능</span> 합니다.</span>
            </v-card-title>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text @click="purchaseConfirm()">
                    구매확정
                </v-btn>
                <v-btn color="red darken-1" text @click="purchaseItem = null; dialog = false">
                    취소
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog2" persistent max-width="290" v-if="purchaseItem != null">
        <v-card>
            <v-card-title class="text-body-1">
                <span>주문을 취소하시겠습니까?</span>
            </v-card-title>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text @click="cancelOrder(purchaseItem)">
                    예
                </v-btn>
                <v-btn color="red darken-1" text @click="purchaseItem = null; dialog2 = false">
                    아니오
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog3" persistent max-width="750px" v-if="purchaseItem != null">
        <v-card>
            <v-card-title>
                <span class="text-h5">리뷰 작성</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-row justify="center">
                        <v-col cols="12">
                            <div class="text-h6">상품 정보</div>
                            <ProductDetailDisplay :productNo="purchaseItem.productNo" />
                        </v-col>
                        <v-col cols="12">
                            <div class="black--text text-body-1">구매 옵션 : <span v-if="purchaseItem.selectedColor != null">{{purchaseItem.selectedColor}}</span><span v-if="purchaseItem.selectedColor != null && purchaseItem.selectedSize != null">/</span><span>{{purchaseItem.selectedSize}}</span></div>
                        </v-col>
                        <v-col cols="12">
                            <div class="text-h6">별점</div>
                            <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" hover length="5" size="64" v-model="star"></v-rating>
                        </v-col>
                        <v-col cols="12">
                            <div class="text-h6">리뷰</div>
                            <ckeditor :editor="editor" v-model="content" :config="editorConfig"></ckeditor>
                            <span :class="contentColor">{{content.length}}/600</span>
                        </v-col>
                        <v-col cols="12" align="center">
                            <v-card :loading="false" class="mx-auto my-5">
                                <v-card-title>
                                    <v-img max-height="250" :src="imageUrl" min-height="250" contain @click="fileInputClick" />
                                </v-card-title>
                                <v-card-actions>
                                    <v-file-input v-model="imageFile" :id="`fileInput`" accept="image/*" truncate-length="14" class="pa-0" hide-details @change="onImageChange"></v-file-input>
                                </v-card-actions>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="resetReview">
                    Close
                </v-btn>
                <v-btn color="blue darken-1" text @click="addReview">
                    Save
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog4" width="600px" v-if="purchaseItem != null">
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
                            <v-text-field v-model="purchaseItem.id" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">이름</td>
                        <td>
                            <v-text-field v-model="purchaseItem.name" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">전화번호</td>
                        <td>
                            <v-text-field v-model="purchaseItem.tel" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">우편번호</td>
                        <td>
                            <v-text-field v-model="purchaseItem.zipCode" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">기본주소</td>
                        <td>
                            <v-text-field v-model="purchaseItem.address" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-h6">상세주소</td>
                        <td>
                            <v-text-field v-model="purchaseItem.detailAddr" hide-details readonly></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <v-row justify="end">
                                <v-col cols="auto">
                                    <v-btn class="primary" @click="purchaseItem = null; dialog4 = false">확인</v-btn>
                                </v-col>
                            </v-row>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>
    </v-dialog>
    <v-dialog v-model="alertDialog" :persistent="alertPath != null" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
            <v-row justify="end" v-if="alertPath != null">
                <v-col cols="auto" class="pr-1 pb-1">
                    <v-btn text :to="alertPath">이동하기</v-btn>
                    <v-btn text @click="alertDialog = false; alertPath = null; purchaseItem = null; dialog2 = false">취소</v-btn>
                </v-col>
            </v-row>
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
import DateDisplay from '@/components/DateDisplay.vue'
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import ProductDetailDisplay from '@/components/ProductDetailDisplay.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        DateDisplay,
        ProductDetailDisplay,
    },
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            alertPath: null,
            editor: ClassicEditor,
            editorConfig: {
                ckfinder: {},
                toolbar: {
                    shouldNotGroupWhenFull: true
                }
            },
            dialog: false,
            dialog2: false,
            dialog3: false,
            dialog4: false,
            orders: [],
            totalContents: 0,
            loading: false,
            options: {},
            headers: [{
                text: '주문번호',
                value: 'orderIdx',
                divider: true,
                align: 'center',
                width: '8%',
            }, {
                text: '이미지',
                value: 'imageName',
                divider: true,
                align: 'center',
                width: '12%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '19%',
            }, {
                text: '개수',
                value: 'orderAmount',
                divider: true,
                align: 'center',
                width: '6%',
            }, {
                text: '가격',
                value: 'totalPrice',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
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
                text: '배송',
                value: 'delivery',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '',
                value: 'btn',
                align: 'center',
                width: '15%',
            }, ],
            searchWord: '',
            searchDate1: '',
            searchDate2: '',

            states: [],
            stateSelected: null,

            menu1: false,
            menu2: false,

            selectedOrder: '주문 내역조회',
            selectedColor: true,

            purchaseItem: '',

            star: 5,
            content: '',
            contentColor: 'black--text',
            imageFile: null,
            imageUrl: null,
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        colorPicker(put) {
            if (this.selectedOrder == put) {
                return 'secondary'
            }
        },
        selectOrder(put) {
            this.orders = [];
            this.selectedOrder = put;
            this.reset();
        },
        searchOrder(selectedOrder) {
            if (this.options.page != 1) {
                this.options.page = 1;
            } else {
                this.getOrder(selectedOrder);
            }
        },
        getOrder(put) {
            this.loading = true;
            let pageInfo = '';
            if (put == '주문 내역조회') {
                pageInfo = 'orders';
                this.states = [{
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
                }, ];
            } else {
                pageInfo = 'returns';
                this.states = [{
                    text: '기준 선택',
                    value: null,
                }, {
                    text: '취소완료',
                    value: '취소완료',
                }, {
                    text: '교환완료',
                    value: '교환완료',
                }, {
                    text: '환불완료',
                    value: '환불완료',
                }, ];
            }
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                method: 'get',
                url: `/api/order/getOrderById`,
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    pageInfo: pageInfo,
                    state: this.stateSelected,
                    searchWord: this.searchWord,
                    searchDate1: this.searchDate1,
                    searchDate2: this.searchDate2,
                    id: this.getLogin.user.id,
                }
            }).then(res => {
                this.orders = res.data.orderList;
                this.totalContents = res.data.count;
            }).catch((err) => {
                this.orders = [];
                this.totalContents = 0;
                console.log(err);
            }).finally(
                this.loading = false
            )
        },
        cancelOrder(item) {
            if (item.state == '입금전' || item.state == '결제완료') {
                let states = [];
                let data = {
                    orderIdx: item.orderIdx,
                    state: '취소완료',
                }
                states.push(data);
                axios.patch(`/api/order/update`, states)
                    .then(res => {
                        if (res.data.length == 0) {
                            this.alertDialog = true;
                            this.alertType = 'success';
                            this.alertMessage = '주문을 취소하셨습니다';
                            this.dialog2 = false;
                            if (this.options.page != 1) {
                                this.options.page = 1;
                            } else {
                                this.getOrder(this.selectedOrder);
                            }
                        } else {
                            this.alertDialog = true;
                            this.alertType = 'warning';
                            this.alertMessage = `미완료된 변경(총 ${res.data.length}건)\n주문번호 : ${res.data}`;
                        }
                    }).catch(err => {
                        console.log(err)
                    })
            } else if (item.state == '배송준비중') {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '배송 준비중인 주문입니다. 배송 전 변경/취소 게시판에서 요청해주시기 바랍니다';
                this.alertPath = `/qna/beforeDeliveryQnA`;
            }
        },
        reset() {
            this.stateSelected = null;
            this.searchWord = null;
            this.searchDate1 = null;
            this.searchDate2 = null;
            let date = new Date();
            this.searchDate1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
            this.searchDate2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.getOrder(this.selectedOrder);
        },
        changeDate(period) {
            let date = new Date();
            let year = date.getFullYear();
            let month = date.getMonth() + 1;
            let day = date.getDate();

            if (period == '1week') {
                if (day > 6) {
                    day = day - 6;
                } else {
                    if (month > 1) {
                        month = month - 1;
                    } else {
                        year = year - 1;
                        month = month + 11;
                    }
                    day = day + 6;
                }
            } else if (period == '1month') {
                if (month > 1) {
                    month = month - 1;
                } else {
                    year = year - 1;
                    month = month + 11;
                }
            } else if (period == '3month') {
                if (month > 3) {
                    month = month - 3;
                } else {
                    year = year - 1;
                    month = month + 9;
                }
            } else if (period == '6month') {
                if (month > 6) {
                    month = month - 6;
                } else {
                    year = year - 1;
                    month = month + 6;
                }
            }
            let lastDay = new Date(year, month, 0).getDate();
            if (day > lastDay) {
                if (month != 12) {
                    month = month + 1;
                    day = day - lastDay;
                } else {
                    year = year + 1;
                    month = 1;
                    day = day - lastDay;
                }
            }
            this.searchDate1 = `${year}-${month}-${day}`;
            this.searchDate2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;

            this.stateSelected = null;
            this.searchWord = null;
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.getOrder(this.selectedOrder);
        },
        selectItem(item) {
            this.purchaseItem = item;
        },
        purchaseConfirm() {
            let states = [];
            let data = {
                orderIdx: this.purchaseItem.orderIdx,
                state: '구매확정',
            }
            states.push(data);
            axios.patch(`/api/order/update`, states)
                .then(res => {
                    if (res.data.length == 0) {
                        this.alertDialog = true;
                        this.alertType = 'success';
                        this.alertMessage = '구매를 확정하셨습니다';
                        if (this.options.page != 1) {
                            this.options.page = 1;
                        } else {
                            this.getOrder(this.selectedOrder);
                        }
                    } else {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = `미완료된 변경(총 ${res.data.length}건)\n주문번호 : ${res.data}`;
                    }
                }).catch(err => {
                    console.log(err)
                }).finally(() => {
                    this.purchaseItem = '';
                    this.dialog = false;
                })
        },
        // 이미지
        fileInputClick() {
            document.getElementById(`fileInput`).click();
        },
        onImageChange() {
            const file = this.imageFile;
            if (file) {
                this.imageUrl = URL.createObjectURL(file);
                URL.revokeObjectURL(file);
            } else {
                this.imageUrl = null;
            }
        },

        addReview() {
            if (this.content == '') {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '후기를 입력해주세요';
                return;
            }
            let image = null;
            if (this.imageFile != null) {
                image = this.imageFile.name;
            }
            let data = {
                productNo: this.purchaseItem.productNo,
                star: this.star,
                content: this.content,
                image: image,
                id: this.getLogin.user.id,
            };
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));
            if (this.imageFile != null) {
                formData.append(`fileList`, this.imageFile);
            }
            formData.append(`fileList`, this.imageFile);
            axios.post(`/api/review/insert/${this.purchaseItem.orderIdx}`, formData)
                .then(() => {
                    this.dialog = false;
                    this.content = '';
                    this.alertDialog = true;
                    this.alertType = 'success';
                    this.alertMessage = '리뷰 등록 완료';
                    this.resetReview();
                    this.getOrder(this.selectedOrder);
                }).catch((err) => {
                    this.alertDialog = true;
                    this.alertType = 'error';
                    this.alertMessage = '리뷰 작성에 실패했습니다';
                    console.log(err);
                })
        },
        resetReview() {
            this.dialog3 = false;
            this.star = 5;
            this.content = '';
            this.imageFile = null;
            this.imageUrl = null;
            this.purchaseItem = null;
        }
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        options: {
            handler() {
                this.getOrder(this.selectedOrder);
            },
            deep: true,
        },
        content: {
            handler() {
                if (this.content.length > 600) {
                    this.contentColor = 'red--text';
                } else {
                    this.contentColor = 'black--text';
                }
            }
        }
    },
    mounted() {
        let date = new Date();
        this.searchDate1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
        this.searchDate2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
    }
}
</script>

<style scoped>
.v-btn:not(.v-btn--round).v-size--default {
    height: 36px;
    min-width: 45px;
    padding: 5px 10px;
}
</style>
