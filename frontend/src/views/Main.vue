<template>
<v-container class="pt-1">
    <v-row justify="center">
        <v-col class="px-0">
            <v-carousel cycle height="400" hide-delimiter-background show-arrows-on-hover>
                <v-carousel-item v-for="(slide, i) in slides" :key="i">
                    <v-sheet :color="colors[i]" height="100%">
                        <v-row class="fill-height" align="center" justify="center">
                            <div class="text-h2">
                                {{ slide }} Slide
                            </div>
                        </v-row>
                    </v-sheet>
                </v-carousel-item>
            </v-carousel>
        </v-col>
    </v-row>
    <v-row class="text-h2 font-weight-black" justify="center">
        EVENT
    </v-row>
    <v-row class="my-3" justify="center">
        <v-col cols="10">
            <v-row>
                <v-col v-for="count in 4" :key="count" cols="3">
                    <v-card align="center">
                        <v-img  max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/300/250`"></v-img>
                        <v-card-title>Lorem ipsum dolor sit amet consectetur adipisicing elit. Nobis error dolore, natus rerum facilis vel perspiciatis incidunt, expedita officiis libero corrupti consequuntur impedit aperiam, cupiditate magni cum ut sapiente omnis?</v-card-title>
                    </v-card>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-row class="text-h2 font-weight-medium" justify="center">
        WEEKLY BEST
    </v-row>
    <v-row class="my-10" justify="center">
        <v-col cols="9">
            <v-row>
                <v-col v-for="count in 8" :key="count" cols="3">
                    <v-card align="center">
                        <v-img  max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/300/250`"></v-img>
                        <v-card-title>Lorem ipsum dolor sit amet consectetur adipisicing elit. Nobis error dolore, natus rerum facilis vel perspiciatis incidunt, expedita officiis libero corrupti consequuntur impedit aperiam, cupiditate magni cum ut sapiente omnis?</v-card-title>
                    </v-card>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-row class="text-h2 font-weight-medium" justify="center">
        BEW
    </v-row>
    <v-row class="my-10" justify="center">
        <v-col cols="9">
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
    <v-row class="text-h2 font-weight-medium" justify="center">
        BEST
    </v-row>
    <v-row class="my-10" justify="center">
        <v-col cols="9">
            <v-row>
                <v-col v-for="count in 8" :key="count" cols="3">
                    <v-card align="center">
                        <v-img lazy-src="https://picsum.photos/id/11/10/6" max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/300/250`"></v-img>
                        <v-card-title>Lorem ipsum dolor sit amet consectetur adipisicing elit. Nobis error dolore, natus rerum facilis vel perspiciatis incidunt, expedita officiis libero corrupti consequuntur impedit aperiam, cupiditate magni cum ut sapiente omnis?</v-card-title>
                    </v-card>
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
            colors: [
                'indigo',
                'warning',
                'pink darken-2',
                'red lighten-1',
                'deep-purple accent-4',
            ],
            slides: [
                'First',
                'Second',
                'Third',
                'Fourth',
                'Fifth',
            ],
        }
    },
    methods: {
        randomNumber(count) {
           return  Math.floor(Math.random() * 100)+count;
        }
    }
}
</script>

<style>

</style>
