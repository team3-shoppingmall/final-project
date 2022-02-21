<template>
  <span>
      {{year}}/{{month}}/{{day}} {{hour}}:{{minute}}:{{second}}
  </span>
</template>

<script>
export default {
    computed:{
        year(){
            return this.regDate.slice(2,4);
        },
        month(){
            return this.regDate.slice(5,7);
        },
        day(){
            return this.regDate.slice(8,10);
        },
        hour(){
            return this.regDate.slice(11,13);
        },
        minute(){
            return this.regDate.slice(14,16);
        },
        second(){
            return this.regDate.slice(17,19);
        }
    },
    props:[`regDate`]
}
</script>

<style>

</style>
