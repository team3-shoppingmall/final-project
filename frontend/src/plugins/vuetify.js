import '@mdi/font/css/materialdesignicons.css' // Ensure you are using css-loader
import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
            light: {
                primary: '#000000',
                secondary: '#9E9E9E',
                // accent: '#82B1FF', error: '#FF5252', info: '#2196F3', success: '#4CAF50',
                // warning: '#FB8C00',
            }
        }
    },
    icons: {
        iconfont: 'mdi', // default - only for display purposes
    }
});
