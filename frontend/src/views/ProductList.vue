<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <v-row class="text-h4">
                Best Items
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="count in 8" :key="count" cols="3">
                            <v-card align="center">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/300/250`"></v-img>
                                <v-card-title>Lorem ipsum dolor sit amet consectetur adipisicing elit. Nobis error dolore, natus rerum facilis vel perspiciatis incidunt, expedita officiis libero corrupti consequuntur impedit aperiam, cupiditate magni cum ut sapiente omnis?</v-card-title>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
            <v-divider></v-divider>
            <v-row class="text-h4 mt-10">
                {{mainCategory}}
            </v-row>
            <v-row>
                <v-col cols="auto">

                </v-col>
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="count in 20" :key="count" cols="3">
                            <v-card align="center">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/300/250`"></v-img>
                                <v-card-title>Lorem ipsum dolor sit amet consectetur adipisicing elit. Nobis error dolore, natus rerum facilis vel perspiciatis incidunt, expedita officiis libero corrupti consequuntur impedit aperiam, cupiditate magni cum ut sapiente omnis?</v-card-title>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            mainCategory: '데님',
            subCategory: '',
        }
    },
    methods: {
        randomNumber(count) {
            return Math.floor(Math.random() * 100) + count;
        }
    }
}
</script>

<style>

</style>
