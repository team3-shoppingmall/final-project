<template>
<v-container>
    <div>
        <v-data-table :headers="headers" :items="contents" :page.sync="page" :items-per-page="itemsPerPage" hide-default-footer class="elevation-1" @page-count="pageCount = $event"></v-data-table>
        <div class="text-center pt-2">
            <v-pagination v-model="page" :length="pageCount"></v-pagination>
            <v-text-field :value="itemsPerPage" label="Items per page" type="number" min="-1" max="15" @input="itemsPerPage = parseInt($event, 10)"></v-text-field>
        </div>
    </div>
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
        }
    },
    methods: {
        getNotice() {
            axios.get(`/api/notice/notice/list`).then(res => {
                this.contents = res.data
            })
        }
    },
    mounted() {
        this.getNotice();
    }
}
</script>

<style>

</style>
