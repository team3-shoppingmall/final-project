<template>
<v-row justify="center" no-gutters>
    <!-- <v-divider vertical="true" class="white"></v-divider> -->
    <v-btn v-for="link in links" :key="link" color="white" text rounded class="my-2">
        {{ link }}
    </v-btn>
    <v-col class="primary lighten-2 py-4 text-center white--text" cols="12">
        <v-row justify="center">
            <v-col cols="3">
                <v-card class="primary lighten-2 pa-3 text-start white--text " outlined>
                    <div class="text-h6">COMPANY INFO</div>
                    <div class="caption my-1"> 법인명(상호) : 스프링(주) 대표자(성명) : 홍길동 </div>
                    <div class="caption my-1"> 서울시 가나구 다라동 87-3, 2층 202호 </div>
                    <div class="caption my-1"> (도로명주소 : 서울시 가나구 남부순환로 2275, 2층 202호) </div>
                    <div class="caption my-1"> 사업자등록번호 : 111-22-33333 </div>
                    <div class="caption my-1"> 통신판매신고 : 제20XX-XXXX-XXXX호 </div>
                    <div class="caption my-1"> 개인정보 보호 책임자 : 스프링(spring@naver.com) </div>
                    <div class="caption my-1"> hosting by THIRD TEAM </div>
                </v-card>
            </v-col>
            <v-col cols="3">
                <v-card class="primary lighten-2 pa-3 text-start white--text" outlined>
                    <div class="text-h6">CALL CENTER</div>
                    <div class="text-h5 my-2"> 070-1234-1234 </div>
                    <div class="caption my-1"> WEEKDAYS, AM 10 : 00 - PM 5 : 00 </div>
                    <div class="caption my-1"> LUNCH, PM 1 : 00 - 2 : 00 </div>
                    <div class="caption my-1"> SAT, SUN, HOLIDAY OFF. </div>
                </v-card>
            </v-col>
            <v-col cols="3">
                <v-card class="primary lighten-2 pa-3 text-start white--text" outlined>
                    <div class="text-h6">NOTIFICATION</div>
                    <div class="caption my-1"> [반품 주소] </div>
                    <div class="caption my-1"> 우체국 : 서울시 가나구 다라동 87-3, 2층 202호 스프링 </div>
                    <div class="caption my-1"> 타택배 : 서울시 가나구 다라동 87-3, 2층 202호 스프링 </div>
                    <div class="caption my-1"> (선불결제) </div>
                    <div class="text-h6">BANK INFO</div>
                    <div class="caption my-1"> 마바은행 000-000000-00-000 (예금주:스프링) </div>
                    <div class="caption my-1"> (무통장 입금시 실시간으로 확인되고 있습니다.) </div>
                </v-card>
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
