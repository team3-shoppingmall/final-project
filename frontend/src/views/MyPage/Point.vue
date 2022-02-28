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
            <v-data-table :headers="headers" :items="points"></v-data-table>

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
            }, 
             
             ],
             points: '',
            
        }
    },
    method: {
            getPoint() {
            this.loading = true;
            axios({
                method: 'get',
                url: `/api/point/getAll`

            })
            .then(res => {
             
                
                this.points = res.data;
                
                this.loading = false;
            })
        },
        getMemberPoint() {
            this.loading = true;
            axios({
                method: 'get',
                url:`/api/member/getMemberPoint`

            })
            .then(res => {
                
                this.point = res.data.point;
                this.loading = false;
            })

        }
    },
}
</script>

<style></style>
