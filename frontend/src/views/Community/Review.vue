<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" item-key="reviewNo" class="elevation-1" disable-sort>
        <template #[`item.productNo`]="{item}">
            <div class="text-left">
                <ProductNameDisplay :productNo="item.productNo" />
            </div>
        </template>
        <template #[`item.star`]="{item}">
            <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" length="5" readonly size="10" :value="item.star"></v-rating>
        </template>
        <template #[`item.content`]="{item}">
            <v-row justify="space-between">
                <v-col>
                    <div v-html="item.content" class="text-left"></div>
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
        <v-col class="d-flex" cols="8" sm="7" md="6" lg="5" xl="4">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn icon="icon" @click="getReview">검색</v-btn>
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
import ProductNameDisplay from '@/components/ProductNameDisplay.vue'
export default {
    components: {
        HideId,
        DateDisplay,
        ProductNameDisplay
    },
    data() {
        return {
            admin: true,
            totalContents: 0,
            reviewNo: '',
            contents: [],
            options: {},
            loading: true,
            headers: [{
                text: '번호',
                value: 'reviewNo',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '상품명',
                value: 'productNo',
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
                width: '45%',
                align: 'center',
                divider: true
            }, {
                text: '작성자',
                value: 'id',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '작성일',
                value: 'regDate',
                width: '10%',
                align: 'center'
            }],
            searches: [{
                text: '상품명',
                value: 'productname'
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
                    searchWord: this.searchWord
                }
            }).then(res => {
                this.contents = res.data;
                axios({
                    method: 'get',
                    url: `/api/review/getCount`,
                    params: {
                        search: this.search,
                        searchWord: this.searchWord
                    }
                }).then(res => {
                    this.totalContents = res.data;
                    this.loading = false;
                })
            })
        },

        deleteReview(num) {
            axios({
                    method: 'delete',
                    url: `/api/review/delete`,
                    params: {
                        reviewNo: num
                    }
                })
                .then(res => {
                    console.log(res.data);
                    if (res.status == 200) {
                        alert("삭제가 완료되었습니다.")
                        this.$router.go();
                    }
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
            deep: true
        }
    }
}
</script>

<style scoped="scoped"></style>
