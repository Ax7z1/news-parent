<template>
    <a-layout-content style="padding: 0 50px">
        <a-layout style="padding: 24px 0; background: #fff">
            <a-layout-sider width="200" style="background: #fff">
                <a-menu
                        v-model:selectedKeys="selectedKeys2"
                        v-model:openKeys="openKeys"
                        mode="inline"
                        style="height: 100%"
                >
                    <a-sub-menu key="sub1">
                        <template #title>
                <span>
                  <user-outlined />
                  subnav 1
                </span>
                        </template>
                        <a-menu-item key="1">option1</a-menu-item>
                        <a-menu-item key="2">option2</a-menu-item>
                        <a-menu-item key="3">option3</a-menu-item>
                        <a-menu-item key="4">option4</a-menu-item>
                    </a-sub-menu>
                    <a-sub-menu key="sub2">
                        <template #title>
                <span>
                  <laptop-outlined />
                  subnav 2
                </span>
                        </template>
                        <a-menu-item key="5">option5</a-menu-item>
                        <a-menu-item key="6">option6</a-menu-item>
                        <a-menu-item key="7">option7</a-menu-item>
                        <a-menu-item key="8">option8</a-menu-item>
                    </a-sub-menu>
                    <a-sub-menu key="sub3">
                        <template #title>
                <span>
                  <notification-outlined />
                  subnav 3
                </span>
                        </template>
                        <a-menu-item key="9">option9</a-menu-item>
                        <a-menu-item key="10">option10</a-menu-item>
                        <a-menu-item key="11">option11</a-menu-item>
                        <a-menu-item key="12">option12</a-menu-item>
                    </a-sub-menu>
                </a-menu>
            </a-layout-sider>
            <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
                新闻首页
                <p>
                    <a-button type="primary" @click="add">添加</a-button>
                </p>
                <a-table :dataSource="newsList"
                         :columns="columns"
                         :pagination="pagination"
                         :loading="loding"
                         @change="handleTableChange">

                    <template v-slot:action="{text,record}">
                        <a-button type="primary">编辑</a-button>
                        <a-button type="danger">删除</a-button>

                    </template>

                </a-table>

            </a-layout-content>
        </a-layout>
    </a-layout-content>

</template>

<!--
【错误记录】You may need an additional loader to handle the result of these loaders.
解决方法：
tsconfig.json
“jsx”: “preserve”
改为
“jsx”: “react”

-->

<script lang="ts">
    // vue v-m 双向绑定   vue3使用ref作为“中介”
    import { defineComponent,onMounted,ref} from 'vue';
    import axios from 'axios';

    export default {
        name: 'newsList',
        setup(){ //vue3 函数 定义 data 定义

            //定义一个新闻的列表（绑定前端table）
            const newsList = ref();
            //const news = ref({});

            const columns = [
                {
                    title: '标题',
                    dataIndex: 'title',
                    key: 'title',
                },
                {
                    title: '摘要',
                    dataIndex: 'summary',
                    key: 'summary',
                },                {
                    title: '作者',
                    dataIndex: 'author',
                    key: 'author',
                },
                {
                    title:'Action',
                    key:'action',
                    slots:{customRender:'action'}
                }
            ];
            const pagination = ref({
                current:1,
                pageSize:3,
                total:0
            });
            const loading = ref<boolean>(false);
            const handleQuery=(params:any)=>{//查询新闻列表的函数
                loading.value = true;
                axios.get('http://localhost:8899/news-query/listByPage',{
                    params:{
                        page:params.page,
                        size:params.size
                    }
                }).then((response)=>{
                    loading.value = false;
                    newsList.value = response.data.list; //在vue3中变量要通过value来赋值
                    console.log("新闻列表："+newsList.value);
                    // 重置分页按钮
                    pagination.value.current = params.age;
                    pagination.value.total = response.data.total;
                });
            };

            // 分页导航链接函数,带参函数，参数由table组件传递
            const handleTableChange=(pagination:any)=>{
                handleQuery({
                    page:pagination.current,
                    size:pagination.pageSize
                });
            };

            onMounted(()=>{
                handleQuery({
                    page:1,
                    size:pagination.value.pageSize
                });
            });

            return{
                //返回模型数据和函数
                newsList,
                columns,
                handleQuery,
                pagination,
                handleTableChange,
                loading
            }
        }
    }

</script>

<style scoped>

</style>