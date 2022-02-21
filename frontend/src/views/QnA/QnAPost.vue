<template>
<v-container>
    <v-row justify="center">
        <v-col align-self="center" cols="7">
            <div class="text-h3">문의</div>
            <ProductDetailDisplay :type="'product'" />
            <v-simple-table>
                <template slot="default">
                    <tbody>
                        <tr>
                            <td style="width:10%"> 제목 </td>
                            <td>
                                <QnATitleDisplay :type="type" />
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%"> 작성자 </td>
                            <td>
                                <HideId :id="'ididididid'" />
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%"> 작성일 </td>
                            <td>
                                <DateDisplay :regDate="'2022-02-18T15:00:00.000+00:00'" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                {{content}}
                            </td>
                        </tr>
                        <tr v-if="image1 != ''">
                            <td rowspan="5"> 파일 첨부 </td>
                            <td>
                                {{image1}}
                            </td>
                        </tr>
                        <tr v-if="image2 != ''">
                            <td>
                                {{image2}}
                            </td>
                        </tr>
                        <tr v-if="image3 != ''">
                            <td>
                                {{image3}}
                            </td>
                        </tr>
                        <tr v-if="image4 != ''">
                            <td>
                                {{image4}}
                            </td>
                        </tr>
                        <tr v-if="image5 != ''">
                            <td>
                                {{image5}}
                            </td>
                        </tr>
                    </tbody>
                </template>
            </v-simple-table>
            <v-divider></v-divider>
            <v-row justify="end" class="mt-3">
                <v-col cols="auto">
                    <v-btn @click="moveToBefore">목록</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="moveToUpdate">수정</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="deleteQnA">삭제</v-btn>
                </v-col>
            </v-row>
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
import QnATitleDisplay from '@/components/QnATitleDisplay.vue'
import ProductDetailDisplay from '@/components/ProductDetailDisplay.vue'
export default {
    components: {
        HideId,
        DateDisplay,
        QnATitleDisplay,
        ProductDetailDisplay,
    },
    data() {
        return {
            pageID: '',
            admin: false,
            type: 'product',
            content: '',
            image1: '',
            image2: '',
            image3: '',
            image4: '',
            image5: '',
        }
    },
    methods: {
        getQnA() {
            this.pageID = this.$route.params.id;
        },
        moveToBefore() {
            this.$router.go(-1);
        },
        moveToUpdate() {
            this.$router.push(`/updatePost/notice/${this.pageID}`)
        },
        deleteQnA(){
            console.log(this.pageID);
            axios({
                 method: 'delete',
                    url: `/api/qna/deleteqna`,
                    params: {
                       qnaNo: this.pageID,
                    }
            }).then((res)=>{
                console.log(res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
    },
    mounted() {
        this.getQnA();
    }
}
</script>

<style>

</style>