<template>
<v-container>
    <v-simple-table v-if="dataLoaded">
        <template slot="default">
            <tbody>
                <tr>
                    <td style="width:10%"> 제목 </td>
                    <td>
                        {{notice.title}}
                    </td>
                </tr>
                <tr>
                    <td style="width:10%"> 작성자 </td>
                    <td>
                        <HideId :id="notice.id" />
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <v-row>
                            <v-col cols="8">
                                <div v-html="notice.content"></div>
                            </v-col>
                        </v-row>
                        <v-row v-if="images != []" justify="center">
                            <v-col cols="10" v-for="(image, idx) in images" :key="idx">
                                <v-img contain :src="`/api/notice/noticeImage/${pageID}/${image}`" max-height="auto" max-width="auto"></v-img>
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
        <v-col cols="auto" v-if="getLogin != null && getLogin.user.authority == 'ROLE_ADMIN'">
            <v-btn @click="moveToUpdate" color="primary">수정</v-btn>
        </v-col>
        <v-col cols="auto" v-if="getLogin != null && getLogin.user.authority == 'ROLE_ADMIN'">
            <v-btn @click="deleteNotice" color="primary">삭제</v-btn>
        </v-col>
    </v-row>
    <v-dialog v-model="alertDialog" :persistent="alertPath != null" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
            <v-row justify="end">
                <v-col cols="auto">
                    <v-btn text :to="alertPath" v-if="alertPath != null">확인</v-btn>
                </v-col>
            </v-row>
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        HideId,
    },
    data() {
        //Vue component에서 사용할 변수들을 선언, data=key:value
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            alertPath: null,
            dataLoaded: false,
            pageID: '',
            notice: '',
            images: [],
        }
    },
    methods: {
        moveToBefore() {
            this.$router.go(-1);
        },
        moveToUpdate() {
            this.$router.push(`/updatePost/notice/${this.pageID}`)
        },

        //Vue component에서 사용할 메서드를 선언, template에서 이벤트로 호출될 수 있음
        //Router는 Vue component와 웹 경로를 연결해줌
        getNotice() {
            this.dataLoaded = false;
            axios.get(`/api/notice/list/${this.pageID}`)
                .then((res) => {
                    this.notice = res.data;
                    if (this.notice.image != null) {
                        this.images = this.notice.image.split(';');
                    }
                }).catch((err) => {
                    this.alertDialog = true;
                    this.alertType = 'error';
                    this.alertMessage = '정보를 불러오는데 실패했습니다';
                    console.log(err);
                }).finally(
                    this.dataLoaded = true
                )
        },
        deleteNotice() {
            axios.delete(`/api/notice/deleteNotice/${this.pageID}`)
                .then(() => {
                    this.alertDialog = true;
                    this.alertType = 'success';
                    this.alertMessage = '공지사항 삭제 완료';
                    this.alertPath = `/community/notice`;
                }).catch((err) => {
                    console.log(err);
                })
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    mounted() { //method를 호출하거나 DOM으로 <template>안에 있는 태그를 처리할 때 사용
        this.pageID = this.$route.params.id;
        this.getNotice();
    }
}
</script>

<style>

</style>
