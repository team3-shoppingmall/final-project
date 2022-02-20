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
                        <hideId :id="'ididididid'" />
                    </td>
                </tr>
                <tr>
                    <td style="width:10%"> 작성일 </td>
                    <td>
                        <dateDisplay :regDate="'2022-02-18T15:00:00.000+00:00'" />
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
            <v-btn @click="moveto">목록</v-btn>
        </v-col>
        <v-col cols="auto">
            <v-btn @click="moveto">수정</v-btn>
        </v-col>
        <v-col cols="auto">
            <v-btn @click="moveto">삭제</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
// import axios from 'axios'
import hideId from '@/components/hideId.vue'
import dateDisplay from '@/components/dateDisplay.vue'
export default {
    components: {
        hideId,
        dateDisplay,
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
        }
    },
    mounted() {
        this.getNotice();
    }
}
</script>

<style>

</style>
