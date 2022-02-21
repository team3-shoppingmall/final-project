<template>
<v-container>
    <div class="text-h3">문의</div>
    <v-simple-table>
        <template slot="default">
            <tbody>
                <tr>
                    <td style="width:10%"> 제목 </td>
                    <td>
                        {{title}}
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
// import axios from 'axios'
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
export default {
    components: {
        HideId,
        DateDisplay,
    },
    data() {
        return {
            pageID: '',
            admin: false,
            title: '공지사항 제목',
            content: '',
            image1: '',
            image2: '',
            image3: '',
            image4: '',
            image5: '',
        }
    },
    methods: {
        getNotice() {
            this.pageID = this.$route.params.id;
        },
        moveToBefore() {
            this.$router.go(-1);
        },
        moveToUpdate() {
            this.$router.push(`/updatePost/notice/${this.pageID}`)
        },
        deleteNotice(){
            console.log(this.pageID);
        },
    },
    mounted() {
        this.getNotice();
    }
}
</script>

<style>

</style>
