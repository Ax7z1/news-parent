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

    <a-modal v-model:visible="modalVisible"
             title="新闻编辑表单"
             @ok="handleModalOK"
             cancel-text="取消"
             ok-text="确定">
        <a-form :model="news" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
            <a-form-item label="新闻类别">
                <a-select v-model:value="news.categoryid">
                    <a-select-option v-for="item in categoryList" :value="item.id" :key="item.id">
                        {{item.cname}}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="标题">
                <a-input v-model:value="news.title" />
            </a-form-item>
            <a-form-item label="新闻摘要">
                <a-input v-model:value="news.summary"/>
            </a-form-item>
            <a-form-item label="作者">
                <a-input v-model:value="news.author" />
            </a-form-item>
        </a-form>
    </a-modal>

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
    import {message} from "ant-design-vue";

    export default {
        name: 'newsList',
        setup(){ //vue3 函数 定义 data 定义

            //定义一个新闻的列表（绑定前端table）
            const newsList = ref();
            const news = ref({});

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

            const modalVisible = ref<boolean>(false);
            const categoryList = ref();

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

            //弹出添加模式窗口
            const add=()=>{
                modalVisible.value=true;
                //初始化news对象
                news.value={};
            };

            // 模式窗口确定按钮触发的函数
            const handleModalOK=()=>{
                axios.post('http://localhost:8899/news-edit/saveNews',news.value).then(response=>{
                    if (response.data.code===200){
                        modalVisible.value=false;
                        //加载最新数据
                        handleQuery({
                            page:pagination.value.current,
                            size:pagination.value.pageSize
                        });
                    }else {
                        message.error(response.data.message);
                    }
                });

            };

            // 查询新闻类别列表
            const handleLoadNewsCategory=()=>{
                axios.get('http://localhost:8899/news-query/findAllCategory').then(response=>{
                    categoryList.value = response.data;
                });
            };


            onMounted(()=>{
                handleQuery({
                    page:1,
                    size:pagination.value.pageSize
                });
               handleLoadNewsCategory();
            });

            return{
                //返回模型数据和函数
                newsList,
                news,
                columns,
                handleQuery,
                pagination,
                handleTableChange,
                loading,
                modalVisible,
                add,
                handleLoadNewsCategory,
                categoryList,
                handleModalOK
            }
        }
    }

</script>

<style scoped>

</style>