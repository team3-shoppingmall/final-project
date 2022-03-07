<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" item-key="reviewNo" class="elevation-1" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
        <template #[`item.image`]="{item}">
            <v-row justify="center">
                <v-col cols="auto">
                    <v-carousel :show-arrows="false" cycle interval="3000" hide-delimiters style="height:100px;width:100px">
                        <v-carousel-item>
                            <v-dialog max-width="700">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-img v-bind="attrs" v-on="on" min-height="100" max-height="100" :src="`/api/review/reviewImage/${item.reviewNo}/${item.image}`" contain v-if="item.image != null"></v-img>
                                </template>
                                <v-card>
                                    <v-img :src="`/api/review/reviewImage/${item.reviewNo}/${item.image}`"></v-img>
                                </v-card>
                            </v-dialog>
                        </v-carousel-item>
                    </v-carousel>
                </v-col>
            </v-row>
        </template>
        <template #[`item.productName`]="{item}">
            <v-btn text :to="`/productDetail/${item.productNo}`">
                <div class="text-left text-truncate" style="max-width: 100px;">
                    {{ item.productName }}
                </div>
            </v-btn>
        </template>
        <template #[`item.star`]="{item}">
            <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" length="5" readonly size="10" :value="item.star"></v-rating>
        </template>
        <template #[`item.content`]="{item}">
            <v-row justify="space-between" align="center">
                <v-col>
                    <div v-html="item.content" class="text-left"></div>
                </v-col>
                <v-col cols="auto" v-if="getLogin != null && (getLogin.user.authority == 'ROLE_ADMIN' || getLogin.user.id == item.id)">
                    <v-icon @click="updateReview(item.reviewNo)" v-if="getLogin != null && getLogin.user.authority != 'ROLE_ADMIN'">mdi-pencil</v-icon>
                    <v-icon @click="deleteReview(item.reviewNo)">mdi-delete</v-icon>
                </v-col>
            </v-row>
        </template>
        <template #[`item.id`]="{item}">
            <div>
                <HideId :id="item.id" />
            </div>
        </template>
        <template #[`item.regDate`]="{item}">
            <DateDisplay :regDate="item.regDate" />
        </template>
    </v-data-table>

    <v-row align="center" justify="space-between">
        <v-col class="d-flex" cols="8" sm="7" md="6" lg="5" xl="4">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord" @keyup.enter="getReview"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn @click="getReview" color="primary">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        HideId,
        DateDisplay,
    },
    data() {
        return {
            totalContents: 0,
            contents: [],
            nameList: [],
            options: {},
            loading: true,
            headers: [{
                text: '번호',
                value: 'reviewNo',
                width: '5%',
                align: 'center',
                divider: true
            }, {
                text: '이미지',
                value: 'image',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '상품명',
                value: 'productName',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '별점',
                value: 'star',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '후기',
                value: 'content',
                width: '51%',
                align: 'center',
                divider: true
            }, {
                text: '작성자',
                value: 'id',
                width: '7%',
                align: 'center',
                divider: true
            }, {
                text: '작성일',
                value: 'regDate',
                width: '7%',
                align: 'center'
            }],
            searches: [{
                text: '상품명',
                value: 'productName'
            }, {
                text: '작성자',
                value: 'id'
            }],
            search: 'id',
            searchWord: ''
        }
    },
    methods: {
        getReview() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options;
            axios({
                method: 'get',
                url: `/api/review/getAllReviews`,
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    search: this.search,
                    searchWord: this.searchWord,
                    productNo: 0,
                }
            }).then(res => {
                this.contents = res.data.reviewList;
                this.totalContents = res.data.count;
            }).finally(() => {
                this.loading = false;
            })
        },
        deleteReview(num) {
            axios.delete(`/api/review/delete/${num}`)
                .then(() => {
                    alert("삭제가 완료되었습니다.")
                    this.$router.go();
                }).catch(err => {
                    console.log(err);
                })
        },
        updateReview(num) {
            this.$router.push(`/updatePost/review/${num}`);
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        options: {
            handler() {
                this.getReview()
            },
            deep: true
        }
    }
}
</script>

<style scoped="scoped"></style>
