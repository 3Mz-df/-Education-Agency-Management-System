<script setup lang="ts">
import { onMounted, ref, shallowRef } from 'vue';
import { displayText, formatDateTime, userStatusText } from '@/utils/eams';
import { fetchCourseList, fetchCurrentUser, fetchUserPage } from '@/service/api';
import type { SystemUser } from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';
import { useRouterPush } from '@/hooks/common/router';

defineOptions({ name: 'Home' });

const authStore = useAuthStore();
const { routerPushByKey } = useRouterPush();
const loading = ref(false);
const currentUser = shallowRef<SystemUser | null>(null);
const userCount = ref<number | null>(null);
const courseCount = ref<number | null>(null);

async function refresh() {
  if (loading.value) return;
  loading.value = true;
  try {
    const [user, users, courses] = await Promise.all([
      fetchCurrentUser(),
      fetchUserPage({ pageNum: 1, pageSize: 1 }),
      fetchCourseList()
    ]);
    currentUser.value = user.error === null ? user.data : null;
    userCount.value = users.error === null ? users.data.total : null;
    courseCount.value = courses.error === null ? courses.data.length : null;
  } finally {
    loading.value = false;
  }
}

function goUsers() {
  void routerPushByKey('user');
}

function goCourses() {
  void routerPushByKey('course');
}

onMounted(refresh);
</script>

<template>
  <div class="flex-col-stretch gap-16px">
    <NCard :bordered="false" class="card-wrapper">
      <div class="flex flex-wrap items-center justify-between gap-16px">
        <div>
          <h2 class="text-24px font-600">你好，{{ currentUser?.realName || authStore.userInfo.userName }}</h2>
          <p class="mt-8px text-gray-500">欢迎进入 EAMS 教培管理系统</p>
        </div>
        <NButton :loading="loading" @click="refresh">刷新工作台</NButton>
      </div>
    </NCard>
    <NGrid cols="1 s:2" responsive="screen" :x-gap="16" :y-gap="16">
      <NGi>
        <NCard title="用户管理" :bordered="false" class="card-wrapper">
          <NStatistic label="用户总数" :value="userCount ?? '—'" />
          <p class="my-16px text-gray-500">查询用户、维护资料与账号信息。</p>
          <NButton type="primary" secondary @click="goUsers">进入用户管理</NButton>
        </NCard>
      </NGi>
      <NGi>
        <NCard title="课程管理" :bordered="false" class="card-wrapper">
          <NStatistic label="课程总数" :value="courseCount ?? '—'" />
          <p class="my-16px text-gray-500">查看课程列表，创建课程草稿。</p>
          <NButton type="primary" secondary @click="goCourses">进入课程管理</NButton>
        </NCard>
      </NGi>
    </NGrid>
    <NCard title="当前账号" :bordered="false" class="card-wrapper">
      <NSpin :show="loading">
        <NDescriptions v-if="currentUser" label-placement="left" :column="2" bordered>
          <NDescriptionsItem label="用户 ID">{{ currentUser.id }}</NDescriptionsItem>
          <NDescriptionsItem label="用户名">{{ currentUser.username }}</NDescriptionsItem>
          <NDescriptionsItem label="昵称">{{ displayText(currentUser.nickname) }}</NDescriptionsItem>
          <NDescriptionsItem label="真实姓名">{{ displayText(currentUser.realName) }}</NDescriptionsItem>
          <NDescriptionsItem label="手机号">{{ displayText(currentUser.phone) }}</NDescriptionsItem>
          <NDescriptionsItem label="邮箱">{{ displayText(currentUser.email) }}</NDescriptionsItem>
          <NDescriptionsItem label="用户类型">{{ displayText(currentUser.userType) }}</NDescriptionsItem>
          <NDescriptionsItem label="机构 ID">{{ displayText(currentUser.institutionId) }}</NDescriptionsItem>
          <NDescriptionsItem label="状态">{{ userStatusText(currentUser.status) }}</NDescriptionsItem>
          <NDescriptionsItem label="创建时间">{{ formatDateTime(currentUser.createTime) }}</NDescriptionsItem>
        </NDescriptions>
        <NEmpty v-else description="暂时无法加载账号信息，请稍后刷新" />
      </NSpin>
    </NCard>
  </div>
</template>
