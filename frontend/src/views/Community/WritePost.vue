<template>
<v-container>
        <v-row align="center">
            <v-col cols="1">
                제목
            </v-col>
            <v-col>
                <v-select v-model="titleSelected" :items="titles" v-if="!admin"></v-select>
                <v-text-field v-if="admin"></v-text-field>
            </v-col>
        </v-row>
    <v-row align="center">
        <v-col cols="1">
            내용
        </v-col>
        <v-col cols="11">
            <v-textarea counter name="input-7-1" height="300px" v-model="content"></v-textarea>
        </v-col>
    </v-row>
    <v-row align="center">
        <v-col cols="1">
            파일 첨부
        </v-col>
        <v-col>
            <v-row>
                <v-col>
                    <v-file-input accept="image/*"></v-file-input>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-file-input accept="image/*"></v-file-input>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-file-input accept="image/*"></v-file-input>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-file-input accept="image/*"></v-file-input>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-file-input accept="image/*"></v-file-input>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-row align="center">
        <v-col cols="1">
            비밀글
        </v-col>
        <v-col>
            <v-radio-group v-model="secret" row>
                <v-radio label="비밀글" :value="true"></v-radio>
                <v-radio label="공유글" :value="false"></v-radio>
            </v-radio-group>
        </v-col>
    </v-row>
    <v-row justify="end">
        <v-col cols="auto">
            <v-btn @click="form">작성</v-btn>
        </v-col>
        <v-col cols="auto">
            <v-btn @click="moveto">취소</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
// import axios from 'axios'

export default {
    data() {
        return {
            admin: false,
            titles: [{
                    text: '상품 문의입니다',
                    value: 'product'
                },
                {
                    text: '일반 문의입니다',
                    value: 'general'
                }
            ],
            titleSelected: 'product',
            contents: ['상품 문의관련 text', '일반 문의관련 text'],
            content: '상품 문의관련 text',
            secret: true,

        }
    },
    methods: {
        currentURL() {
            let link = document.location.href;
            let pageList = ['notice', 'faq']
            for (let i = 0; i < pageList.length; i++) {
                if (link.indexOf(pageList[i]) != -1) {
                    this.admin = true;
                    this.titleSelected = '',
                        this.content = ''
                }
            }
        },
        moveto() {
            const id = this.$route.params.id;
            this.$router.push(`/community/${id}`)
        }
    },
    watch: {
        titleSelected: {
            handler() {
                for (let i = 0; i < this.titles.length; i++) {
                    if (this.titles[i].value == this.titleSelected) {
                        this.contents = this.contents[i];
                    }
                }
            }
        }
    },
    mounted() {
        this.currentURL();
    }
}
</script>

<style>

</style>
