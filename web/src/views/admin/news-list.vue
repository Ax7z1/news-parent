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
                ????????????
                <p>
                    <a-button type="primary" @click="add">??????</a-button>
                </p>
                <a-table :dataSource="newsList"
                         :columns="columns"
                         :pagination="pagination"
                         :loading="loding"
                         @change="handleTableChange">

                    <template v-slot:action="{text,record}">
                        <a-button type="primary" @click="edit(record)">??????</a-button>
                        <a-popconfirm
                                title="?????????????????????????????????????"
                                ok-text="???"
                                cancel-text="???"
                                @confirm="del(record.id)"
                        >

                            <a-button type="danger">??????</a-button>
                        </a-popconfirm>

                    </template>

                </a-table>

            </a-layout-content>
        </a-layout>
    </a-layout-content>

    <a-modal v-model:visible="modalVisible"
             title="??????????????????"
             @ok="handleModalOK"
             cancel-text="??????"
             ok-text="??????">
        <a-form :model="news" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
            <a-form-item label="????????????">
                <a-select v-model:value="news.categoryid">
                    <a-select-option v-for="item in categoryList" :value="item.id" :key="item.id">
                        {{item.cname}}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="??????">
                <a-input v-model:value="news.title" />
            </a-form-item>
            <a-form-item label="????????????">
                <a-input v-model:value="news.summary"/>
            </a-form-item>
            <a-form-item label="??????">
                <a-input v-model:value="news.author" />
            </a-form-item>
        </a-form>
    </a-modal>

</template>

<!--
??????????????????You may need an additional loader to handle the result of these loaders.
???????????????
tsconfig.json
???jsx???: ???preserve???
??????
???jsx???: ???react???

-->

<script lang="ts">
    // vue v-m ????????????   vue3??????ref??????????????????
    import { defineComponent,onMounted,ref} from 'vue';
    import axios from 'axios';
    import {message} from "ant-design-vue";
    import {Tool} from "@/utils/tool";

    export default {
        name: 'newsList',
        setup(){ //vue3 ?????? ?????? data ??????

            //??????????????????????????????????????????table???
            const newsList = ref();
            const news = ref({});

            const columns = [
                {
                    title: '??????',
                    dataIndex: 'title',
                    key: 'title',
                },
                {
                    title: '??????',
                    dataIndex: 'summary',
                    key: 'summary',
                },                {
                    title: '??????',
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

            const handleQuery=(params:any)=>{//???????????????????????????
                loading.value = true;
                axios.get('http://localhost:8899/news-query/listByPage',{
                    params:{
                        page:params.page,
                        size:params.size
                    }
                }).then((response)=>{
                    loading.value = false;
                    newsList.value = response.data.list; //???vue3??????????????????value?????????
                    console.log("???????????????"+newsList.value);
                    // ??????????????????
                    pagination.value.current = params.age;
                    pagination.value.total = response.data.total;
                });
            };

            // ????????????????????????,????????????????????????table????????????
            const handleTableChange=(pagination:any)=>{
                handleQuery({
                    page:pagination.current,
                    size:pagination.pageSize
                });
            };

            //????????????????????????
            const add=()=>{
                modalVisible.value=true;
                //?????????news??????
                news.value={};
            };

            //????????????????????????
            const edit=(record:any)=>{
                modalVisible.value=true;
                news.value=Tool.copy(record); // ????????????
            }

            // ???????????????????????????????????????
            const handleModalOK=()=>{
                axios.post('http://localhost:8899/news-edit/saveNews',news.value).then(response=>{
                    if (response.data.code===200){
                        modalVisible.value=false;
                        //??????????????????
                        handleQuery({
                            page:pagination.value.current,
                            size:pagination.value.pageSize
                        });
                    }else {
                        message.error(response.data.message);
                    }
                });

            };

            // ????????????????????????
            const handleLoadNewsCategory=()=>{
                axios.get('http://localhost:8899/news-query/findAllCategory').then(response=>{
                    categoryList.value = response.data;
                });
            };

            // ????????????
            const del=(id:number)=>{
                axios.delete('http://localhost:8899/news-edit/deleteNews/' +id).then(response=>{
                    if (response.data.code===200){
                        //??????????????????
                        handleQuery({
                            page:pagination.value.current,
                            size:pagination.value.pageSize
                        });
                    }else {
                        message.error(response.data.message);
                    }
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
                //???????????????????????????
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
                handleModalOK,
                edit,
                del
            }
        }
    }

</script>

<style scoped>

</style>