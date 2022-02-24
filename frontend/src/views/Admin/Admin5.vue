<template>
<v-container fluid>
    <v-row v-for="(image,idx) in images" :key="idx">
        <v-col cols="8" align-self="center">
            <div class="imagePreviewWrapper" :style="{ 'background-image': `url(${image}` }">
            </div>
        </v-col>
        <v-col cols="2" align-self="center">
            <v-file-input v-model="images[idx]" @change="test" accept="image/png, image/jpeg, image/bmp" hide-details></v-file-input>
        </v-col>
        <v-col cols="1" align-self="center">
            <v-text-field hide-details></v-text-field>
        </v-col>
        <v-col cols="1" align-self="center">
            <v-btn class="error" @click="remove(idx)">{{idx}}제거</v-btn>
        </v-col>
    </v-row>
    <v-row justify="end">
        <v-col cols="1" align-self="center">
            <v-btn class="success" @click="insert">추가</v-btn>
        </v-col>
        <v-col cols="1" align-self="center" class="ml-3">
            <v-btn class="primary" @click="check">확인</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    methods: {
        // 현재 변경시 추가 됨 이것은 idx 나 v-model을 이용해서 splice 할것 혹은 if을 통한 null 체크 후 remove 실행 후 추가 할것
        pickFile(target) {
            let input = target;
            if (input) {
                let reader = new FileReader
                reader.onload = e => {
                    this.image = e.target.result
                }
                reader.readAsDataURL(input)
                console.log(this.image);
                // this.$emit('input', input)
            }

            // this.imagename = input.name
            // this.imageFile1 = input;
            // this.formData.append('fileList', input[0])
        },
        insert() {
            // this.count++;
            this.images.push(null);
            this.images1.push(null);
        },
        remove(idx) {
            this.images.splice(idx, 1)
            this.count--;
        },
        check() {
            console.log(this.images)
        },
        test(input) {
            if (input) {
                let reader = new FileReader
                reader.onload = e => {
                    this.image = e.target.result;
                }
                reader.readAsDataURL(input)
                // this.$emit('input', input)
            }
            console.log("image", this.image)
            
        }
    },
    watch: {
        // images: {
        //     handler() {
        //         this.images1 = this.images.map(element => {
        //             console.log(element)
        //             if (element) {
        //                 let reader = new FileReader
        //                 reader.onload = e => {
        //                     this.image = e.target.result;

        //                 }
        //                 reader.readAsDataURL(element)
        //                 // this.$emit('input', input)
        //             }
        //             console.log("image", this.image)
        //             // return image;
        //         });
        //     },
        //     deep: true
        // }
    },
    data() {
        return {
            image: null,
            images: [],
            images1: [],
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
