<template>
<v-container style="min-height: 910px;">
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">POINT</div>
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="9">
            <MyPageButton class="mt-0" />
        </v-col>
    </v-row>
      <v-row justify="center">
        <v-col cols="9">
            <div class="text-h6">
                총 포인트 : 0P
            </div>
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="9">
            <v-data-table :headers="headers"></v-data-table>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import MyPageButton from '@/components/MyPageButton.vue'
export default {
    data() {
        return {
            headers: [{
                text: '날짜',
                value: 'date',
                align:'center',
            }, {
                text: '포인트',
                value: 'point',
                align:'center',
            }, {
                text: '내용',
                value: 'content',
                align:'center',
            }, ],
        }
    },
    components: {
        MyPageButton
    }
}
</script>

<style></style>
