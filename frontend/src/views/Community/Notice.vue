<template>
<v-container>
    <v-data-table :headers="headers" :items="contents" :items-per-page="5" class="elevation-1"></v-data-table>
</v-container>
</template>

<script>
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
            contents: [{
                    noticeno: '16',
                    title: '2022년 02월 07일 이전 개인정보 처리방침',
                    ID: '슬로우엔드',
                },
            ],
        }
    },
}
</script>

<style>

</style>
