<template>
<v-container fluid>
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
import axios from 'axios'
export default {
      components: {
    },
    data() {
        return {
            headers: [{
                text: '날짜',
                value: 'pointDate',
                align: 'center',
            }, {
                text: '포인트',
                value: 'point',
                align: 'center',
            }, {
                text: '내용',
                value: 'content',
                align: 'center',
            }, ],
            
        }
    },
    method: {
        getPoint() {
            this.loading = true
            axios({
                method: 'get',
                url: `/api/point/getAll`,

            })
            .then(res => {
                
                this.pointdate = res.data.pointDate;
                this.point = res.data.point;
                this.content = res.data.content;
            })
        }
    },
}
</script>

<style></style>
