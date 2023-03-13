<template>
    <a-layout-header class="header">
        <div class="logo" />
        <a-menu
                v-model:selectedKeys="selectedKeys1"
                theme="dark"
                mode="horizontal"
                :style="{ lineHeight: '64px' }"
        >
            <a-menu-item key="1">首页</a-menu-item>
            <a-menu-item key="/admin/newslist">
                <router-link to="/admin/newslist">新闻列表管理</router-link>
            </a-menu-item>
            <a-menu-item key="3">nav 3</a-menu-item>
        </a-menu>
    </a-layout-header>
</template>

<script lang="ts">
    import { defineComponent } from 'vue';

    export default defineComponent({
        name: 'the-header'
    });
</script>


<style scoped>

</style>