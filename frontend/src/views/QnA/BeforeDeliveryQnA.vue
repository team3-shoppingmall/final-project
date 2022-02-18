<template>
<v-container>
    <div>
        <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" class="elevation-1"></v-data-table>
    </div>

    <v-row align="center" justify="space-between">
        <v-col class="d-flex" cols="8" sm="7" md="5" lg="4" xl="3">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn icon @click="getNotice">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="auto">
            <v-btn :to="'/writePost/beforeDeliveryQnA'">글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                    text: '번호',
                    value: 'noticeNo',
                    sortable: false,
                    width: '10%'
                },
                {
                    text: '제목',
                    value: 'title',
                    sortable: false,
                    width: '70%'
                },
                {
                    text: '작성자',
                    value: 'id',
                    sortable: false,
                    width: '10%'
                },
                {
                    text: '작성일',
                    value: 'regdate',
                    sortable: false,
                    width: '10%'
                },
            ],
            searches: [{
                    text: '제목',
                    value: 'title'
                },
                {
                    text: '내용',
                    value: 'content'
                },
                {
                    text: '작성자',
                    value: 'id'
                }
            ],
            search: 'title',
            searchWord: '',
        }
    },
    methods: {
        getNotice() {
            this.loading = true
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                    method: 'get',
                    url: `/api/notice/getNotice`,
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
                            url: '/api/notice/getCount',
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
    },
    watch: {
        options: {
            handler() {
                this.getNotice()
            },
            deep: true,
        },
    },
}
</script>

<style>

</style>
