<template>
<v-container fluid="fluid">
    <v-row>
        <v-col cols="12" align-self="center">
            <v-data-table :headers="headers" :options.sync="options" :page.sync="pageView" :items-per-page="8" :items="banners" :server-items-length="totalContents" :loading="loading" hide-default-footer="hide-default-footer" @page-count="pageCount = $event" :sort-by="'num'" @click:row="select" dense="dense" height="288px"></v-data-table>
            <v-pagination v-model="pageView" :length="pageCount"></v-pagination>
        </v-col>
    </v-row>
    <v-row>
        <v-col cols="12" align-self="center">
            <v-row>
                <v-img :src="image" min-height="400px" max-height="400px" contain="contain" @click="clickImage" style="cursor:pointer"></v-img>
            </v-row>
            <v-row justify="center">
                <v-col cols="2" align-self="center" class="pa-2">
                    <v-file-input v-model="file" @change="createImageURL" id="imageInput" accept="image/png, image/jpeg, image/bmp" outlined="outlined" dense="dense" hide-details="hide-details"></v-file-input>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2">
                    <v-text-field v-model="link" outlined="outlined" dense="dense" hide-details="hide-details" label="link"></v-text-field>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2">
                    <v-text-field v-model="num" outlined="outlined" dense="dense" hide-details="hide-details" label="INDEX"></v-text-field>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2">
                    <v-btn class="error pa-0" @click="removeBanner">제거</v-btn>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2" v-if="isUpdate">
                    <v-btn class="success " @click="insertBanner">추가</v-btn>
                </v-col>
                <v-col cols="auto" align-self="center" class="pa-2" v-else>
                    <v-btn class="info " @click="insertBanner">수정</v-btn>
                    <v-btn class="info ml-4" @click="isUpdate = true, file = null, link = null, num = null">초기화</v-btn>
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
            document
                .getElementById('imageInput')
                .click();
        },
        select(value) {
            this.isUpdate = false;
            this.image = value.image;
            this.link = value.link;
            this.num = value.num;

            // axios
            //     .get(`/api/banner/image/${value.image}`)
            //     .then(res => {
            //         this.file = new File([res.data], value.image, {
            //             type: "image/*",
            //             lastModified: Date.now()
            //         })

            //         this.createImageURL();
            //     })
            //     .catch(err => console.log(err))

        },
        getBanners() {

            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options;
            axios
                .get('/api/banner/getBanners', {
                    params: {
                        page: page,
                        perPage: itemsPerPage
                    }
                })
                .then(res => {
                    this.banners = res.data;
                    this.totalContents = res.data.count;
                })
                .catch(err => {
                    console.log(err.response.status);
                })
                .finally(this.loading = false);
        },
        insertBanner() {

            // if (this.link == null)
            //     this.link = '';
            // let data = {
            //     image: this.file.name,
            //     link: this.link,
            //     num: this.num
            // }
            // let formData = new FormData();
            // formData.append(
            //     'data',
            //     new Blob([JSON.stringify(data)], {
            //         type: "application/json"
            //     })
            // );
            // formData.append(`banner`, this.file);
            // axios
            //     .post('/api/banner/insertBanner', formData)
            //     .then(() => {
            //         console.log("성공");
            //         this
            //             .banners
            //             .sort((a, b) => a.num - b.num);
            //         this.changeOrder(data);
            //         this.getBanners();
            //         // this.banners.push(data);
            //         console.log("배열길이", this.banners.length)
            //         for (let i = 0; i < this.banners.length; i++) {
            //             console.log(this.banners[i].image)
            //             axios
            //                 .put('/api/banner/update', this.banners[i])
            //                 .catch(err => {
            //                     console.log(err.response.data)
            //                 })
            //         }

            //         this.file = null;
            //         this.link = null;
            //         this.image = null;
            //         console.log(this.banners)
            //     })
            //     .catch((err) => {
            //         console.log(err)
            //     })
        },
        removeBanner() {},
        changeOrder(data) {
            let index = -1;
            for (let i = 0; i < this.banners.length; i++) {
                if (data.num == this.banners[i].num)
                    index = i;
            }

            if (index != -1) {
                for (let i = this.banners.length - 1; i >= index; i--) {
                    this
                        .banners[i]
                        .num = Number(this.banners[i].num) + 1;
                }
            }
        },
        createImageURL() {
            console.log("111")
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
    watch: {
        options: {
            handler() {
                this.getBanners();
            },
            deep: true
        }
    },
    data() {
        return {
            isUpdate: true,
            pageView: 1,
            pageCount: 0,
            banners: [],
            totalContents: 0,
            file: null,
            // images: null,
            image: null,
            link: '',
            num: null,
            options: {
                itemsPerPage: 8
            },
            headers: [{
                text: 'IMAGE',
                value: 'image',
                align: 'center',
                sortable: false,
                divider: true
            }, {
                text: 'LINK',
                value: 'link',
                align: 'center',
                sortable: false,
                divider: true
            }, {
                text: 'INDEX',
                value: 'num',
                align: 'center',
                sortable: false
            }],

            rules: {}
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
