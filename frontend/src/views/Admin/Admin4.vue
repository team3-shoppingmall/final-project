<template>
<v-container fluid>
     <v-row justify="center">
        <v-col align-self="center" cols="auto">
            <v-menu ref="menu1" v-model="menu1" :close-on-content-click="false" :return-value.sync="date1" transition="scale-transition" offset-y="offset-y" min-width="auto">
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="date1" label="처음" prepend-icon="mdi-calendar" readonly="readonly" v-bind="attrs" v-on="on" hide-details="hide-details"></v-text-field>
                </template>
                <v-date-picker v-model="date1" no-title="no-title" scrollable="scrollable">
                    <v-spacer></v-spacer>
                    <v-btn text="text" color="primary" @click="menu1 = false">
                        Cancel
                    </v-btn>
                    <v-btn text="text" color="primary" @click="$refs.menu1.save(date1)">
                        OK
                    </v-btn>
                </v-date-picker>
            </v-menu>
        </v-col>
        <v-col align-self="center" cols="auto">
            <v-menu ref="menu2" v-model="menu2" :close-on-content-click="false" :return-value.sync="date2" transition="scale-transition" offset-y="offset-y" min-width="auto">
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="date2" label="끝" prepend-icon="mdi-calendar" readonly="readonly" v-bind="attrs" v-on="on" hide-details="hide-details"></v-text-field>
                </template>
                <v-date-picker v-model="date2" no-title="no-title" scrollable="scrollable">
                    <v-spacer></v-spacer>
                    <v-btn text="text" color="primary" @click="menu2 = false">
                        Cancel
                    </v-btn>
                    <v-btn text="text" color="primary" @click="$refs.menu2.save(date2)">
                        OK
                    </v-btn>
                </v-date-picker>
            </v-menu>
        </v-col>
        <v-col align-self="center" cols="auto">
            <v-btn class="primary">조회하기</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>

        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers"></v-data-table>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            menu1: false,
            date1: '',
            menu2: false,
            date2: '',

            headers: [{
                    text: '상품명',
                    value: '1',
                    align:'center',
                    divider:true,
                },
                {
                    text: '판매금액',
                    value: '1',
                    align:'center',
                },
            ]
        }
    }
}
</script>

<style>

</style>
