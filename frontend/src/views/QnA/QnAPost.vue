<template>
<v-container>
    <v-row justify="center">
        <v-col align-self="center" cols="7">
            <div class="text-h3">문의</div>
            <div v-if="qna.type == 'product' && qna.productNo != 0">
                <ProductDetailDisplay :productNo="qna.productNo" />
            </div>
            <v-simple-table>
                <template slot="default" v-if="dataLoaded">
                    <tbody>
                        <tr>
                            <td style="width:10%"> 제목 </td>
                            <td>
                                <QnATitleDisplay :type="qna.type" />
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%"> 작성자 </td>
                            <td>
                                <HideId :id="qna.id" />
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%"> 작성일 </td>
                            <td>
                                <DateDisplay :regDate="qna.regDate" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <v-row v-if="images != []" justify="center">
                                    <v-col cols="auto" v-for="(image, idx) in images" :key="idx">
                                        <v-img max-width="900px" contain :src="`/api/qna/qnaImage/${pageID}/${image}`"></v-img>
                                    </v-col>
                                </v-row>
                                <v-row>
                                    <v-col>
                                        <div v-html="qna.content"></div>
                                    </v-col>
                                </v-row>
                            </td>
                        </tr>
                    </tbody>
                </template>
            </v-simple-table>
            <v-divider></v-divider>
            <v-row justify="end" class="mt-3">
                <v-col cols="auto">
                    <v-btn @click="moveToBefore" color="primary">목록</v-btn>
                </v-col>
                <v-col cols="auto" v-if="qna.reply">
                    <v-btn @click="moveToReply" color="primary">답변보기</v-btn>
                </v-col>
                <v-col cols="auto" v-if="getLogin != null && getLogin.user.authority == 'ROLE_ADMIN' && (qna.qnaNo == qna.originalNo) && !qna.reply">
                    <v-btn @click="moveToWriteReply" color="primary">답변하기</v-btn>
                </v-col>
                <v-col cols="auto" v-if="getLogin != null && getLogin.user.id == qna.id">
                    <v-btn @click="moveToUpdate" color="primary">수정</v-btn>
                </v-col>
                <v-col cols="auto" v-if="getLogin != null && (getLogin.user.authority == 'ROLE_ADMIN' || getLogin.user.id == qna.id)">
                    <v-btn @click="deleteQnA" color="primary">삭제</v-btn>
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
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        HideId,
        DateDisplay,
        QnATitleDisplay,
        ProductDetailDisplay,
    },
    data() {
        return {
            dataLoaded: false,
            pageID: '',
            admin: true,
            qna: '',
            images: [],
            returnCount: -1,
        }
    },
    methods: {
        getQnA() {
            this.dataLoaded = false;
            axios.get(`/api/qna/getQna/${this.pageID}`)
                .then((res) => {
                    this.qna = res.data;
                    //답글일 경우 image 없음
                    if (res.data.image != null) {
                        this.images = this.qna.image.split(';');
                    }
                    this.dataLoaded = true;
                }).catch((err) => {
                    console.log(err);
                })
        },
        moveToBefore() {
            this.$router.go(this.returnCount);
        },
        moveToReply() {
            axios.get(`/api/qna/getQnaByOriginalNo`, {
                params: {
                    originalNo: this.pageID
                }
            }).then((res) => {
                const link = res.data;
                console.log(link);
                this.$router.push(`/qna/${link}`)

            }).catch((err) => {
                console.log(err);
            })
        },
        moveToWriteReply() {
            if (this.qna.reply == true)
                alert("이미 답변이 등록된 문의글입니다.");
            else
                this.$router.push(`/replyPost/${this.qna.type}/${this.qna.qnaNo}`)
        },
        moveToUpdate() {
            if (this.qna.reply == true)
                alert("이미 답변이 완료된 문의글이므로 수정이 불가합니다.");
            else
                this.$router.push(`/updatePost/qna/${this.qna.qnaNo}`);
        },
        deleteQnA() {
            axios.delete(`/api/qna/deleteqna/${this.qna.qnaNo}`)
                .then(res => {
                    console.log(res.data);
                    alert("삭제되었습니다.");
                    this.$router.go(this.returnCount);
                }).catch((err) => {
                    console.log(err);
                })
        },

    },
    watch: {
        '$route'(from) {
            if (from.name == 'QnAPost') {
                this.returnCount = -2;
            }
            this.pageID = this.$route.params.id;
            this.getQnA();
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    mounted() {
        this.pageID = this.$route.params.id;
        this.getQnA();
    }
}
</script>

<style>

</style>
