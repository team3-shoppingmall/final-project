<template>
<v-container>
    <v-data-table :headers="headers" :items="contents" :items-per-page="5" :to="contents.noticeno" class="elevation-1"></v-data-table>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            headers: [{
                    text: '번호',
                    align: 'start',
                    sortable: false,
                    value: 'noticeno',
                },
                {
                    text: '제목',
                    value: 'title'
                },
                {
                    text: '작성자',
                    value: 'ID'
                },
            ],
            contents: [],
        }
    },
    methods:{
        getNotice(){
            axios.get(`/api/notice/`).then(res => {
                let temp = res.data;
                temp.foreach(element => {
                    this.contents.push(element);
                })
            })
        }
    },
    mounted(){
        this.getNotice();
    }
}
</script>

<style>

</style>
