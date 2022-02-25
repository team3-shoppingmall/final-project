<template>
<v-container fluid>
    <v-row>
        <v-col cols="12" align-self="center">
            <v-data-table :headers="headers" :page.sync="page" :items-per-page="8" :items="banners" hide-default-footer @page-count="pageCount = $event" :sort-by="'num'" dense height="288px"></v-data-table>
            <v-pagination v-model="page" :length="pageCount"></v-pagination>
        </v-col>
    </v-row>
    <v-row>
        <v-col cols="12" align-self="center">
            <v-row>
                <v-img :src="image" min-height="400px" max-height="400px" contain @click="clickImage" style="cursor:pointer"></v-img>
            </v-row>
            <v-row justify="center">
                <v-col cols="2" align-self="center" class="pa-2">
                    <v-file-input v-model="file" @change="createImageURL" id="imageInput" accept="image/png, image/jpeg, image/bmp" outlined dense hide-details></v-file-input>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2">
                    <v-text-field v-model="link" outlined dense hide-details label="link"></v-text-field>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2">
                    <v-text-field v-model="num" outlined dense hide-details label="INDEX"></v-text-field>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2">
                    <v-btn class="error pa-0" @click="removeBanner">제거</v-btn>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2">
                    <v-btn class="success " @click="insertBanner">추가</v-btn>
                </v-col>
            </v-row>
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import axios from 'axios'
export default {
    methods: {
        clickImage() {
            document.getElementById('imageInput').click();
        },
        insertBanner() {
            // 추가할 경우 해당 인덱스가 있는지 확인하고 있으면 해당 인덱스 이후 까지 마지막에서부터 더하기 1을 하여 순서를 미룬다
            // 문제 1 추가할시 DB에 추가하는데 이걸 순서를 어떻게 할지 
            let data = {
                image: this.file.name,
                link: this.link,
                num: this.num,
            }
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));
            formData.append(`banner`, this.file);
            axios.post('/api/banner/insertBanner', formData)
                .then(() => {
                    console.log("성공");
                    this.banners.sort((a, b) => a.num - b.num);
                    // 정렬 후 하나씩 더해서 추가하기
                    this.changeOrder(data);
                    this.banners.push(data);
                    this.file = null;
                    this.link = null;
                    this.image = null;
                    console.log(this.banners)
                })
                .catch((err) => {
                    console.log(err)
                })
        },
        removeBanner() {},
        changeOrder(data) {
            let index = -1;
            for (let i = 0; i < this.banners.length; i++) {
                if (data.num == this.banners[i].num)
                    index = i;
            }
            console.log("배열길이", this.banners.length)
            if (index != -1) {
                for (let i = this.banners.length - 1; i >= index; i--) {
                    this.banners[i].num = Number(this.banners[i].num) + 1;
                }
            }
        },
        createImageURL() {
            const file = this.file;
            if (file) {
                this.image = URL.createObjectURL(file);
                URL.revokeObjectURL(file);
            } else {
                this.image = null;
            }
        },
        indexRule() {
            return false;
        }
    },
    watch: {},
    data() {
        return {
            page: 1,
            pageCount: 0,
            banners: [],
            file: null,
            images: null,
            link: null,
            num: null,
            options: {
                itemsPerPage: 8,
            },
            headers: [{
                    text: 'IMAGE',
                    value: 'image',
                    align: 'center',
                    sortable: false,
                    divider: true,
                },
                {
                    text: 'LINK',
                    value: 'link',
                    align: 'center',
                    sortable: false,
                    divider: true,
                },
                {
                    text: 'INDEX',
                    value: 'num',
                    align: 'center',
                    sortable: false,
                },
            ],
            rules: {

            }
        }
    }
}
</script>

<style scoped>
.imagePreviewWrapper {
    width: 200px;
    height: 200px;
    display: block;
    cursor: pointer;
    margin: 0;
    background-size: cover;
    /* background-position: center center; */
}
</style>
