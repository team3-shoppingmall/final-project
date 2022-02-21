<template>
<v-container class="mt-5">
    <v-row justify="center">
        <v-col cols="9">
            <v-row>
                <v-col cols="6" class="pa-5">
                    <v-img max-height="500" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/500/500`"></v-img>
                </v-col>
                <v-col cols="6" class="pa-5">
                    <v-simple-table>
                        <template slot="default">
                            <tbody>
                                <tr>
                                    <td colspan="3">
                                        상품명
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%"> 가격 </td>
                                    <td colspan="2">
                                        1234원
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%"> 할인가 </td>
                                    <td colspan="2">
                                        1000원
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%"> COLOR </td>
                                    <td colspan="2">
                                        <v-btn>옵션</v-btn>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%"> SIZE </td>
                                    <td colspan="2">
                                        <v-btn>옵션</v-btn>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        선택된 옵션
                                    </td>
                                    <td>
                                        횟수
                                        삭제
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        Total Price : 1234원
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        <v-btn>
                                            BUY IT NOW
                                        </v-btn>
                                        <v-btn>
                                            ADD TO CART
                                        </v-btn>
                                        <v-btn>
                                            WISH LIST
                                        </v-btn>
                                    </td>
                                </tr>
                            </tbody>
                        </template>
                    </v-simple-table>
                </v-col>
            </v-row>

            <v-row class="text-h4 mt-10" justify="center">
                Recommended Items
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row justify="center">
                        <v-col v-for="count in 5" :key="count" cols="2">
                            <v-card @click="moveToDetail(product[0].productno)">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/200/150`"></v-img>
                                <v-card-text>
                                    <div>{{product[0].productname}} - <span v-if="product[0].size != 0">{{product[0].size}} size</span><span v-if="product[0].size==0">{{product[0].color}} color</span></div>
                                    <div v-if="product[0].discount != 0" class="text-decoration-line-through">{{product[0].price}}원</div>
                                    <div v-if="product[0].discount == 0">{{product[0].price}}원</div>
                                    <div v-if="product[0].discount != 0">{{product[0].price*(100-product[0].discount)/100}}원</div>
                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="detailSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')" color="primary">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    상세내용
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="guideSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')" color="primary">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    GUIDE
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="reviewSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')" color="primary">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    REVIEW
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="qnaSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')" color="primary">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    QNA
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                    <div>asdf</div>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            product: [{
                productno: 1,
                imageName: '',
                productname: '블랙트위드 스커트',
                size: 3,
                color: 4,
                price: 20000,
                discount: 5,
            }]

        }
    },
    methods: {
        randomNumber(count) {
            return Math.floor(Math.random() * 100) + count;
        },
        scrollTo(tag) {
            let scroll = 0;
            if (tag == 'detail') {
                scroll = document.querySelector("#detailSelected").offsetTop;
            } else if (tag == 'guide') {
                scroll = document.querySelector("#guideSelected").offsetTop;
            } else if (tag == 'review') {
                scroll = document.querySelector("#reviewSelected").offsetTop;
            } else if (tag == 'qna') {
                scroll = document.querySelector("#qnaSelected").offsetTop;
            }
            window.scrollTo({
                top: scroll,
                behavior: 'smooth'
            });
        }
    },
    mounted() {}
}
</script>

<style>

</style>
