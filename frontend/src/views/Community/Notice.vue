<template>
<v-container>
    <div>
        <v-data-table :headers="headers" :items="contents" :page.sync="page" :items-per-page="itemsPerPage" hide-default-footer class="elevation-1" @page-count="pageCount = $event"></v-data-table>
        <div class="text-center pt-2">
            <v-pagination v-model="page" :length="pageCount"></v-pagination>
        </div>
    </div>

    <v-row align="center" justify="space-between">
        <v-col class="d-flex" cols="8" sm="7" md="5" lg="4" xl="3">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="selectedSearch"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field solo v-model="search"></v-text-field>
                </v-col>
                <v-col cols="1">
                    <v-btn x-large icon @click="searchNotice">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col class="d-flex" cols="2" sm="2" md="2" lg="2" xl="1">
            <v-select :items="pages" label="Items per page" v-model="itemsPerPage"></v-select>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            page: 1,
            pageCount: 0,
            itemsPerPage: 10,
            headers: [{
                    text: '번호',
                    value: 'noticeNo',
                },
                {
                    text: '제목',
                    value: 'title'
                },
                {
                    text: '작성자',
                    value: 'id'
                },
            ],
            contents: [],
            pages: [5, 10, 15],
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
            selectedSearch: 'title',
            search: '',
        }
    },
    methods: {
        getNotice() {
            axios({
                    method: 'get',
                    url: `/api/notice/getNotice`,
                    params: {
                        page: this.page,
                        perPage: this.itemsPerPage,
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    axios.get('/api/notice/getCountAll').then(res => {
                        this.pageCount = Math.ceil(res.data / this.itemsPerPage);
                    })
                })
        },
        searchNotice() {
            console.log(this.selectedSearch);
            console.log(this.search);
        }
    },
    watch: {
        page() {
            this.getNotice();
        },
        itemsPerPage() {
            if (this.page == 1) {
                this.getNotice();
            } else {
                this.page = 1;
            }
        }
    },
    mounted() {
        this.getNotice();
    }
}
</script>

<style>

</style>
