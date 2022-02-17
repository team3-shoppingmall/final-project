<template>
<v-row justify="center" no-gutters>
    <!-- <v-divider vertical="true" class="white"></v-divider> -->
    <v-btn v-for="link in links" :key="link" color="white" text rounded class="my-2">
        {{ link }}
    </v-btn>
    <v-col class="primary lighten-2 py-4 text-center white--text" cols="12">
        <v-row justify="center">
            <v-col cols="3">
                <v-card class="primary lighten-2 pa-3 text-center white--text" outlined>부끄러운 위에 별 된 못 아이들의 했던 계십니다. 이제 별에도 별 별 하나에 가슴속에 새겨지는 봅니다. 잠, 하나의 하나에 이름자를 어머니 아이들의 아스라히 아무 프랑시스 봅니다.</v-card>
            </v-col>
            <v-col cols="3">
                <v-card class="primary lighten-2 pa-3 text-center white--text" outlined>별 하나에 나의 별 있습니다. 시인의 나의 소학교 별 이름과, 슬퍼하는 하나에 책상을 있습니다. 멀리 어머니 소학교 된 많은 불러 이름과, 버리었습니다. 언덕 이 멀듯이, 봅니다.</v-card>
            </v-col>
            <v-col cols="3">
                <v-card class="primary lighten-2 pa-3 text-center white--text" outlined>경, 추억과 나는 별 애기 차 무엇인지 강아지, 그리고 버리었습니다. 북간도에 말 별빛이 하늘에는 거외다. 다 나는 같이 말 별 지나가는 버리었습니다. 까닭이요, 딴은 추억과 시인의 노새, 지나가는 걱정도 불러 까닭입니다.</v-card>
            </v-col>
        </v-row>
        {{ new Date().getFullYear() }} — <strong>Spring</strong>
    </v-col>
</v-row>
</template>

<script>
export default {
    data() {
        return {
            links: [
                '홈으로',
                '회사소개',
                '이용약관',
                '개인정보취급방침',
                '이용안내',
                '고객센터',
                '자주묻는질문',
            ],
        }
    }
}
</script>

<style>

</style>
