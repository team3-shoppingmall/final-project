<template>
<v-container>
    Main Page
    <v-container>
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
        <v-row justify="center">
            <v-col cols="8">
                <v-row>
                    <v-col v-for="count in 8" :key="count">
                        <v-card align="center">
                            <v-img lazy-src="https://picsum.photos/id/11/10/6" max-height="200" max-width="250" src="https://picsum.photos/id/11/500/300"></v-img>
                            
                        </v-card>
                    </v-col>
                </v-row>
            </v-col>
        </v-row>

    </v-container>
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
}
</script>

<style>

</style>
