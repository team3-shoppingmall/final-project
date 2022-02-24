<template>
<v-container>
    <!-- <div class="text-h3">공지사항</div> -->
    <v-simple-table>
        <template slot="default" v-if="dataLoaded">
            <tbody>
                <tr>
                    <td style="width:10%"> 제목 </td>
                    <td>
                        {{this.titleDetail}}
                    </td>
                </tr>
                <tr>
                    <td style="width:10%"> 작성자 </td>
                    <td>
                        <HideId :id="notice.id" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div v-html="notice.content" style="border:1px black solid"></div>
                        <!-- {{this.content}} -->
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
            <v-btn @click="moveToBefore" outlined>목록</v-btn>
        </v-col>
        <v-col cols="auto">
            <v-btn @click="moveToUpdate" outlined>수정</v-btn>
        </v-col>
        <v-col cols="auto">
            <v-btn @click="deleteNotice" outlined>삭제</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
export default {
    components: {
        HideId,
    },
    data() { //Vue component에서 사용할 변수들을 선언, data=key:value
        return {
            dataLoaded: false,
            pageID: '',
            admin: true,
            notice: '',
            image1: '',
            image2: '',
            image3: '',
            image4: '',
            image5: '',
        }
    },
    methods: { //Vue component에서 사용할 메서드를 선언, template에서 이벤트로 호출될 수 있음
        //Router는 Vue component와 웹 경로를 연결해줌
        getNotice() {
            this.pageID = this.$route.params.id;
            this.num = this.$route.params.num;
            this.dataLoaded = false;
            axios({
                method: 'get',
                url: `/api/notice/list/${this.pageID}`,
                params: {
                    noticeNo: this.pageID
                }
            }).then((res) => {
                {
                    this.titleDetail = res.data.title;
                    this.content = res.data.content;
                    this.dataLoaded = true;
                    console.log(res.status);
                } 
            }).catch((err) => {
                alert("목록을 불러오는데 실패했습니다.");
                console.log(err);
            })
        },
        // getNotice() {
        //     axios.get(`/api/notice/list/${this.pageID}`).then (res => {
        //         this.titleDetail = res.data.title;
        //         this.content = res.data.content;
        //         this.dataLoaded = true;
        //         console.log(res.status);
        //     }).catch((err) => {
        //         alert("목록을 불러오는데 실패했습니다.");
        //         console.log(err);
        //     })
        // },

        moveToBefore() {
            this.$router.go(-1);
        },
        moveToUpdate() {
            this.$router.push(`/updatePost/notice/${this.pageID}`)
        },
        deleteNotice() {
            console.log(this.pageID);
            axios({
                method: 'delete',
                url: `/api/notice/deleteNotice`,
                params: {
                    noticeNo: this.pageID
                }
            }).then((res) => {
                console.log(res.data);
                alert("공지사항 삭제 완료");
                this.$router.go(-1);
            }).catch((err) => {
                console.log(err);
            })
        },

    },
    mounted() { //method를 호출하거나 DOM으로 <template>안에 있는 태그를 처리할 때 사용
        this.pageID = this.$route.params.id;
        this.num = this.$route.params.num;
        this.getNotice();
    }
}
</script>

<style>

</style>
