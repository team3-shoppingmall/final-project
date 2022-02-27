<template>
<v-container>
    <v-row justify="center" class="mt-1">
        <v-col xs="12" sm="12" md="12" lg="9" xl="9">
            <div class="text-h3">{{pagename}}</div>
            <v-row justify="center" v-if="post">
                <v-col cols="3">
                    <v-row justify="space-between">
                        <v-btn width="100px" :to="'/community/notice'">notice</v-btn>
                        <v-btn width="100px" :to="'/community/review'">review</v-btn>
                        <v-btn width="100px" :to="'/community/faq'">FAQ</v-btn>
                    </v-row>
                </v-col>
            </v-row>
            <router-view class="mt-5"></router-view>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            pagename: '',
            post: true,
        }
    },
    methods: {
        currentURL() {
            this.post = true;
            let link = document.location.href;
            let pageList = [{
                text: '공지사항',
                value: 'notice',
            }, {
                text: '자주 묻는 질문',
                value: 'faq',
            }, {
                text: '후기',
                value: 'review',
            }, ]
            for (let i = 0; i < pageList.length; i++) {
                if (link.indexOf(pageList[i].value) != -1) {
                    this.pagename = pageList[i].text;
                }
            }
            if (link.indexOf('noticePost') != -1) {
                this.post = false;
            }
        },
    },
    computed: {},
    watch: {
        '$route'() {
            this.currentURL();
        }
    },
    mounted() {
        this.currentURL();
    }
}
</script>

<style>

</style>
