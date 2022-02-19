<template>
<v-container>
    <div>
        <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" class="elevation-1" @click:row="moveto" item-key="qnaNo" disable-sort>
            <template #[`item.id`]="{item}">
                <hideId :id="item.id" />
            </template>
            <template #[`item.regDate`]="{item}">
                <dateDisplay :regDate="item.regDate" />
            </template>
            <template #[`item.type`]="{item}">
                <qnaTitleDisplay :type="item.type" />
            </template>
        </v-data-table>
    </div>

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
                    <v-btn icon @click="getQnA">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="auto">
            <v-btn :to="'/writePost/productQnA'">글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import hideId from '@/components/hideId.vue'
import dateDisplay from '@/components/dateDisplay.vue'
import qnaTitleDisplay from '@/components/qnaTitleDisplay.vue'
export default {
    components: {
        hideId,
        dateDisplay,
        qnaTitleDisplay,
    },
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                    text: '번호',
                    value: 'qnaNo',
                    width: '10%',
                    divider: true,
                    align: 'center'
                },
                {
                    text: '상품명',
                    value: 'productname',
                    width: '10%',
                    divider: true
                },
                {
                    text: '제목',
                    value: 'type',
                    width: '60%',
                    divider: true
                },
                {
                    text: '작성자',
                    value: 'id',
                    width: '10%',
                    divider: true
                },
                {
                    text: '작성일',
                    value: 'regDate',
                    width: '10%',
                    divider: true,
                    align: 'center'
                },
            ],
            searches: [{
                    text: '상품명',
                    value: 'productname'
                }, {
                    text: '제목',
                    value: 'title'
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
        getQnA() {
            this.loading = true
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                    method: 'get',
                    url: `/api/qna/getQnaPage`,
                    params: {
                        page: page,
                        perPage: itemsPerPage,
                        search: this.search,
                        searchWord: this.searchWord,
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    this.loading = false
                    axios({
                            method: 'get',
                            url: '/api/qna/getCount',
                            params: {
                                search: this.search,
                                searchWord: this.searchWord,
                            }
                        })
                        .then(res => {
                            this.totalContents = res.data;
                        })
                })
        },
        moveto(event, {
            item
        }) {
            this.$router.push(`/qna/${item.qnaNo}`)
        },
    },
    watch: {
        options: {
            handler() {
                this.getQnA()
            },
            deep: true,
        },
    },
}
</script>

<style>

</style>
