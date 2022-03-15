<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="9">
            <v-data-table v-model="selected" :headers="headers" :items="wishLists" :options.sync="options" :server-items-length="totalContents" :loading="loading" show-select="show-select" item-key="productNo" class="elevation-1" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
                <template v-slot:[`item.selection`]="{ item }">
                    <div>
                        <v-btn width="120px" class="text-center my-1" @click="openDialog(item)" :disabled="item.amount == 0 || item.onSale == false" outlined="outlined">주문하기</v-btn>
                        <v-btn width="120px" class="text-center my-1" @click="openDialog(item)" :disabled="item.amount == 0 || item.onSale == false" outlined="outlined">장바구니 담기</v-btn>
                        <v-btn width="120px" class="text-center my-1" @click="deleteWishList(item)" outlined="outlined">삭제</v-btn>
                    </div>
                </template>
                <template v-slot:[`item.image`]="{ item }">
                    <v-row justify="center" v-if="item.imageName != null">
                        <v-col cols="auto">
                            <v-carousel :show-arrows="false" cycle interval="3000" hide-delimiters style="height:100px;width:100px">
                                <v-carousel-item>
                                    <v-dialog max-width="700">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-img v-bind="attrs" v-on="on" min-height="100" max-height="100" :src="`/api/product/productImage/${item.productNo}/${item.imageName.split(';')[0]}`" contain></v-img>
                                        </template>
                                        <v-card>
                                            <v-img :src="`/api/product/productImage/${item.productNo}/${item.imageName.split(';')[0]}`"></v-img>
                                        </v-card>
                                    </v-dialog>
                                </v-carousel-item>
                            </v-carousel>
                        </v-col>
                    </v-row>
                </template>
                <template #[`item.productName`]="{item}">
                    <v-btn text :to="`/productDetail/${item.productNo}`" v-if="item.productNo > 0">
                        <div class="text-truncate" style="max-width: 250px;">
                            {{ item.productName }}
                        </div>
                    </v-btn>
                </template>
                <template v-slot:[`item.point`]="{ item }">
                    {{AddComma((item.price-item.discount)*0.02)}}원
                </template>
                <template v-slot:[`item.price`]="{ item }">
                    <div class="text-right" max-width="150">
                        <div v-if="item.discount != 0" class="text-decoration-line-through">
                            {{AddComma(item.price)}}원
                        </div>
                        <div v-if="item.discount == 0">
                            {{AddComma(item.price)}}원
                        </div>
                        <div v-if="item.discount != 0">
                            {{AddComma(item.price-item.discount)}}원
                        </div>
                        <div v-if="item.amount == 0 || item.onSale == false" class="red--text">
                            품절 상품입니다
                        </div>
                    </div>
                </template>
            </v-data-table>
            <v-btn class="primary mt-3" @click="deleteWishList('selected')">선택 삭제하기</v-btn>
        </v-col>
    </v-row>
    <v-dialog v-model="dialog" width="600px">
        <v-card class="pa-2">
            <v-simple-table>
                <thead>
                    <tr>
                        <th class="text-h5" colspan="2">상품 옵션 선택</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="colorOption != null">
                        <td style="width:10%"> COLOR </td>
                        <td colspan="2">
                            <v-chip-group v-model="colorSelection" active-class="deep-purple--text text--accent-4">
                                <v-chip label outlined v-for="color in colorOption" :key="color" :value="color">
                                    {{ color }}
                                </v-chip>
                            </v-chip-group>
                        </td>
                    </tr>
                    <tr v-if="sizeOption != null">
                        <td style="width:10%"> SIZE </td>
                        <td colspan="2">
                            <v-chip-group v-model="sizeSelection" active-class="deep-purple--text text--accent-4">
                                <v-chip label outlined v-for="size in sizeOption" :key="size" :value="size" :disabled="colorOption != null && colorSelection == null">
                                    {{ size }}
                                </v-chip>
                            </v-chip-group>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <v-row justify="end">
                                <v-col cols="auto">
                                    <v-btn class="error" @click="dialog = false">취소</v-btn>
                                    <v-btn class="primary ml-3" @click="buyItNow">BUY IT NOW</v-btn>
                                    <v-btn class="primary ml-3" @click="addToBasket">ADD TO Basket</v-btn>
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
import axios from 'axios';
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {},
    data() {
        return {
            dialog: false,
            loading: false,
            options: {},
            headers: [{
                text: '이미지',
                value: 'image',
                divider: true,
                align: 'center',
                width: '20%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '40%',
            }, {
                text: '적립금',
                value: 'point',
                divider: true,
                align: 'center',
                width: '12%',
            }, {
                text: '가격',
                value: 'price',
                divider: true,
                align: 'center',
                width: '18%',
            }, {
                text: '선택',
                value: 'selection',
                align: 'center',
                width: '10%',
            }],
            selected: [],
            wishLists: [],
            totalContents: 0,
            colorOption: null,
            colorSelection: null,
            sizeOption: null,
            sizeSelection: null,
            selectedItem: '',

            id: 'tester',
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        getWishList() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                    method: 'get',
                    url: `/api/wishList/getWishList`,
                    params: {
                        page: page,
                        perPage: itemsPerPage,
                        id: this.getLogin.user.id,
                    }
                })
                .then(res => {
                    this.wishLists = res.data.wishLists;
                    this.totalContents = res.data.count;
                }).finally(
                    this.loading = false
                )
        },
        deleteWishList(item) {
            let deletes = [];
            if (item == 'selected') {
                console.log(this.selected);
                for (let i = 0; i < this.selected.length; i++) {
                    let data = {
                        id: this.getLogin.user.id,
                        productNo: this.selected[i].productNo,
                    }
                    deletes.push(data);
                }
            } else {
                console.log(item);
                let data = {
                    id: this.getLogin.user.id,
                    productNo: item.productNo,
                }
                deletes.push(data);
            }

            axios.delete(`/api/wishList/deleteWishList`, {
                    data: deletes
                })
                .then(() => {
                    alert('선택한 관심상품이 삭제되었습니다.');
                    this.getWishList();
                }).catch(err => {
                    alert('삭제에 실패하였습니다');
                    console.log(err);
                })
        },
        openDialog(item) {
            this.colorOption = null;
            this.colorSelection = null;
            this.sizeOption = null;
            this.sizeSelection = null;
            if (item.color != null) {
                this.colorOption = item.color.split(';');
            }
            if (item.size != null) {
                this.sizeOption = item.size.split(';');
            }
            this.selectedItem = item;
            this.dialog = true;
        },
        buyItNow() {
            let selection = [];
            let data = {
                id: this.getLogin.user.id,
                productNo: this.selectedItem.productNo,
                selectedColor: this.colorSelection,
                selectedSize: this.sizeSelection,
                basketAmount: 1,
                price: this.selectedItem.price - this.selectedItem.discount
            }
            selection.push(data);
            this.$router.push({
                name: "Payment",
                params: {
                    Payment: selection
                }
            });
        },
        addToBasket() {
            let selection = [];
            let data = {
                id: this.getLogin.user.id,
                productNo: this.selectedItem.productNo,
                selectedColor: this.colorSelection,
                selectedSize: this.sizeSelection,
                basketAmount: 1,
                price: this.selectedItem.price - this.selectedItem.discount
            }
            selection.push(data);
            axios.get(`/api/basket/getBasketCount/${this.getLogin.user.id}`)
                .then(res => {
                    if (res.data == 50) {
                        alert('장바구니에는 50개까지만 저장이 가능합니다.')
                    } else {
                        axios.post(`/api/basket/insert`, selection)
                            .then(res => {
                                if (res.data > 0) {
                                    alert('저장에 실패하셨습니다');
                                } else {
                                    alert('장바구니에 저장하셨습니다');
                                }
                                // this.$router.go();
                                this.getWishList();
                                this.dialog = false;
                            }).catch((err) => {
                                alert('저장에 실패하셨습니다');
                                console.log(err);
                            })
                    }
                })
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        options: {
            handler() {
                this.getWishList()
            },
            deep: true,
        },
    },
}
</script>

<style scoped>
.v-small-dialog__menu-content {
    background-color: black !important;
    position: absolute;
    left: 50%;
}
</style>
