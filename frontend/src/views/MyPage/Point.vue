<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h6">
                총 포인트 : {{memberPoint}}P
            </div>
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="9">
            <v-data-table :headers="headers" :items="pointList" :options.sync="options" :server-items-length="totalContents" :loading="loading" item-key="num" class="elevation-1" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
                <template v-slot:[`item.pointDate`]="{ item }">
                    <DateDisplay :regDate="item.pointDate" />
                </template>
                <template v-slot:[`item.point`]="{ item }">
                    {{AddComma((item.point))}}P
                </template>
                <template v-slot:[`item.content`]="{ item }">
                    <div class="text-left">
                        {{item.content}}
                    </div>
                </template>
            </v-data-table>
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import axios from 'axios'
import DateDisplay from '@/components/DateDisplay.vue'
export default {
    components: {
        DateDisplay,
    },
    data() {
        return {
            headers: [{
                text: '날짜',
                value: 'pointDate',
                divider: true,
                align: 'center',
                width: '20%',
            }, {
                text: '포인트',
                value: 'point',
                divider: true,
                align: 'center',
                width: '20%',
            }, {
                text: '내용',
                value: 'content',
                align: 'center',
                width: '60%',
            }, ],
            memberPoint: 0,
            pointList: [],
            options: {},
            totalContents: 0,
            loading: false,

            id: 'tester',
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        getPoint() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                    method: 'get',
                    url: `/api/point/getPointList`,
                    params: {
                        page: page,
                        perPage: itemsPerPage,
                        id: this.id,
                    }
                })
                .then(res => {
                    this.pointList = res.data.pointList;
                    this.totalContents = res.data.count;
                    this.loading = false;
                })
        },
        getMemberPoint() {
            this.loading = true;
            axios.get(`/api/member/getMemberInfo/${this.id}`)
                .then(res => {
                    this.memberPoint = res.data.point;
                })
        }
    },
    watch: {
        options: {
            handler() {
                this.getPoint();
                this.getMemberPoint();
            },
            deep: true,
        },
    },
}
</script>

<style></style>
