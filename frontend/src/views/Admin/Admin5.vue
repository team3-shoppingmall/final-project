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
                    <v-btn class="info " @click="updateBanner">수정</v-btn>
                    <v-btn class="info ml-4" @click="isUpdate = true, file = null, link = null, num = null, image = null">초기화</v-btn>
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
            // this.image = value.image;

            this.link = value.link;
            this.num = value.num;
            this.oldImage = value.image;
            // axios
            //     .get(`/api/banner/image/${value.image}`)
            //     .then(res => {
            //         this.file = new File([res.data], value.image, {
            //             type: "image/*",
            //             lastModified: Date.now()
            //         })

            //     })
            //     .catch(err => console.log(err))
            this.image = `/api/banner/image/${value.image}`
            this.file = new File([], value.image, {
                type: "image/*",
                lastModified: Date.now()
            })
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
            if (this.link == null)
                this.link = '';
            if (this.num == null)
                this.num = 1;
            let data = {
                image: this.file.name,
                link: this.link,
                num: this.num
            }

            let formData = new FormData();
            formData.append(
                'data',
                new Blob([JSON.stringify(data)], {
                    type: "application/json"
                })
            );
            formData.append(`banner`, this.file);
            axios
                .post('/api/banner/insertBanner', formData)
                .then(() => {
                    this.getBanners();
                    this.file = null;
                    this.link = null;
                    this.num = null;
                    this.image = null;
                })
                .catch((err) => {
                    console.log(err)
                })
        },
        updateBanner() {
            let data = {
                image: this.file.name,
                link: this.link,
                num: this.num
            }
            axios.put(`/api/banner/update/${this.oldImage}`, data)
                .then(() => {
                    this.getBanners();
                    this.file = null;
                    this.link = null;
                    this.num = null;
                    this.image = null;
                });
        },
        removeBanner() {
            axios.delete(`/api/banner/delete/${this.file.name}`)
                .then(() => {
                    this.getBanners();
                    this.file = null;
                    this.link = null;
                    this.num = null;
                    this.image = null;
                    this.isUpdate = true
                });
        },
        createImageURL() {
            console.log(this.file)
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
            oldImage: null,
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
