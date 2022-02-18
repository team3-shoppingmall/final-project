<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :single-expand="true" hide-default-footer :expanded.sync="expanded" :loading="loading" show-expand class="elevation-1" @click:row="(item, slot) => slot.expand(!slot.isExpanded)">
        <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="headers.length">
                {{ item.content }}
            </td>
        </template>
    </v-data-table>
    <v-row justify="end" class="mt-2">
        <v-col cols="auto">
            <v-btn :to="'/community/writePost/faq'">글쓰기</v-btn>
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
            expanded: [],
            headers: [{
                    text: '종류',
                    value: 'type',
                    sortable: false,
                    width: '10%'
                },
                {
                    text: '제목',
                    value: 'title',
                    sortable: false,
                    width: '90%'
                },
                {
                    text: '',
                    value: 'data-table-expand'
                },
            ],
        }
    },
    methods: {
        getReview() {
            this.loading = true
            axios({
                    method: 'get',
                    url: `/api/notice/getNotice`,
                    params: {
                        page: 1,
                        perPage: 100,
                        search: 'title',
                        searchWord: '',
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    this.loading = false
                })
        },
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

<style>

</style>
