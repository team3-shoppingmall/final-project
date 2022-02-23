<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" item-key="reviewNo" class="elevation-1" disable-sort>
        <template #[`item.productno`]="{item}">
            <div class="text-left">
                <ProductNameDisplay :productno="item.productno" />
            </div>
        </template>
        <template #[`item.star`]="{item}">
            <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" length="5" readonly size="10" :value="item.star"></v-rating>
        </template>
        <template #[`item.content`]="{item}">
            <v-row justify="space-between">
                <v-col>
                    <div class="text-left">{{ item.content }}</div>
                </v-col>
                <v-col cols="auto">
                    <v-icon @click="updateReview(item.reviewNo)" v-if="admin">mdi-pencil</v-icon>
                    <v-icon @click="deleteReview(item.reviewNo)" v-if="admin">mdi-delete</v-icon>
                </v-col>
            </v-row>
        </template>
        <template #[`item.id`]="{item}">
            <div class="text-left">
                <HideId :id="item.id" />
            </div>
        </template>
        <template #[`item.regDate`]="{item}">
            <DateDisplay :regDate="item.regDate" />
        </template>
    </v-data-table>

    <v-row align="center" justify="space-between">
        <v-col cols="8" sm="7" md="6" lg="5" xl="4">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn icon @click="getReview">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="auto" class="mr-3">
            <v-row>
                <v-dialog v-model="dialog" persistent max-width="600px">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn outlined v-bind="attrs" v-on="on">
                            리뷰 작성하기
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">리뷰 작성</span>
                        </v-card-title>
                        <v-card-text>
                            <v-container>
                                <v-row>
                                    <v-col cols="12">
                                        상품 정보
                                        <ProductDetailDisplay :type="'product'" />
                                    </v-col>
                                    <v-col cols="12">
                                        별점
                                        <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" hover length="5" size="64" v-model="star"></v-rating>
                                    </v-col>
                                    <v-col cols="12">
                                        리뷰
                                        <v-textarea counter rows="7" v-model="content" :rules="rules"></v-textarea>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-file-input accept="image/*"></v-file-input>
                                    </v-col>
                                </v-row>
                            </v-container>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="dialog = false">
                                Close
                            </v-btn>
                            <v-btn color="blue darken-1" text @click="dialog = false">
                                Save
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-row>
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
import ProductNameDisplay from '@/components/ProductNameDisplay.vue'
import ProductDetailDisplay from '@/components/ProductDetailDisplay.vue'
export default {
    components: {
        HideId,
        DateDisplay,
        ProductNameDisplay,
        ProductDetailDisplay,
    },
    props: ['productno'],
    data() {
        return {
            admin: true,
            dialog: false,
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            star: 5,
            rules: [v => v.length <= 600 || '600자 이하'],
            headers: [{
                    text: '번호',
                    value: 'reviewNo',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '상품명',
                    value: 'productno',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '별점',
                    value: 'star',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '후기',
                    value: 'content',
                    width: '45%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '작성자',
                    value: 'id',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '작성일',
                    value: 'regDate',
                    width: '10%',
                    align: 'center',
                },
            ],
            searches: [{
                    text: '상품명',
                    value: 'productname'
                },
                {
                    text: '작성자',
                    value: 'id'
                }
            ],
            search: 'id',
            searchWord: '',
        }
    },
    methods: {
        getReview() {
            this.loading = true
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                    method: 'get',
                    url: `/api/review/getReview`,
                    params: {
                        page: page,
                        perPage: itemsPerPage,
                        search: this.search,
                        searchWord: this.searchWord,
                        // productNo: this.productNo,
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    this.loading = false
                    axios({
                            method: 'get',
                            url: '/api/review/getCount',
                            params: {
                                search: this.search,
                                searchWord: this.searchWord,
                                // productNo: this.productNo,
                            }
                        })
                        .then(res => {
                            this.totalContents = res.data;
                        })
                })
        },
        //deleteReview(num) {
        //console.log(num);
        deleteReview() {
            axios({
                    method: 'delete',
                    url: `/api/review/delete`,
                    params: {
                        reviewNo: this.reviewNo
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    this.loading = false
                    alert("삭제가 완료되었습니다.")
                    this.$router.push(`/community/review`);
                })
        },

        updateReview(num) {
            this.$router.push(`/updatePost/review/${num}`);
        }
    },
    watch: {
        options: {
            handler() {
                this.getReview()
            },
            deep: true,
        },
    },
}
</script>

<style scoped>
</style>
