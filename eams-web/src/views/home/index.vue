<script setup lang="ts">
import { computed, onMounted, ref, shallowRef } from 'vue';
import { displayText, formatDateTime, userStatusText } from '@/utils/eams';
import { fetchCoursePage, fetchCurrentUser, fetchUserPage } from '@/service/api';
import type { SystemUser } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useAuthStore } from '@/store/modules/auth';
import { useRouterPush } from '@/hooks/common/router';
import inkLandscape from '@/assets/imgs/eams-ink-landscape.png';

defineOptions({ name: 'Home' });

const authStore = useAuthStore();
const appStore = useAppStore();
const { routerPushByKey } = useRouterPush();
const loading = ref(false);
const currentUser = shallowRef<SystemUser | null>(null);
const userCount = ref<number | null>(null);
const courseCount = ref<number | null>(null);
const displayName = computed(() => currentUser.value?.realName || authStore.userInfo.userName);
const avatarLetter = computed(() => displayName.value.slice(0, 1).toUpperCase());

async function refresh() {
  if (loading.value) return;
  loading.value = true;
  try {
    const [user, users, courses] = await Promise.all([
      fetchCurrentUser(),
      fetchUserPage({ pageNum: 1, pageSize: 1 }),
      fetchCoursePage({ pageNum: 1, pageSize: 1 })
    ]);
    currentUser.value = user.error === null ? user.data : null;
    userCount.value = users.error === null ? users.data.total : null;
    courseCount.value = courses.error === null ? courses.data.total : null;
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
  <div class="workspace">
    <div class="workspace-heading">
      <div>
        <p class="workspace-eyebrow">EAMS · 教培管理</p>
        <h1>教务工作台</h1>
      </div>
      <NButton :loading="loading" @click="refresh">
        <template #icon><SvgIcon icon="lucide:refresh-cw" /></template>
        刷新工作台
      </NButton>
    </div>

    <section class="welcome-panel" aria-label="欢迎进入教培管理工作台">
      <img :src="inkLandscape" class="welcome-landscape" alt="" aria-hidden="true" />
      <div class="welcome-content">
        <p class="welcome-kicker">
          <span></span>
          一方书院 · 有序教务
        </p>
        <h2>你好，{{ displayName }}</h2>
        <p class="welcome-description">让教学有序，让管理从容。</p>
      </div>
      <div class="welcome-signature" aria-hidden="true">
        <span>以学为本</span>
        <span class="welcome-seal">教务</span>
      </div>
    </section>

    <div class="overview-grid">
      <NCard :bordered="false" class="overview-card card-wrapper">
        <div class="overview-topline">
          <div class="overview-icon"><SvgIcon icon="lucide:users-round" /></div>
          <span class="overview-category">用户管理</span>
        </div>
        <div class="overview-stat">
          <span class="overview-number">{{ userCount ?? '—' }}</span>
          <span class="overview-unit">位用户</span>
        </div>
        <p class="overview-description">统一维护用户资料与账号信息。</p>
        <NButton text type="primary" @click="goUsers">
          进入用户管理
          <template #icon><SvgIcon icon="lucide:arrow-up-right" /></template>
        </NButton>
      </NCard>
      <NCard :bordered="false" class="overview-card card-wrapper">
        <div class="overview-topline">
          <div class="overview-icon overview-icon--course"><SvgIcon icon="lucide:book-open" /></div>
          <span class="overview-category">课程管理</span>
        </div>
        <div class="overview-stat">
          <span class="overview-number">{{ courseCount ?? '—' }}</span>
          <span class="overview-unit">门课程</span>
        </div>
        <p class="overview-description">整理课程资料，维护课程草稿。</p>
        <NButton text type="primary" @click="goCourses">
          进入课程管理
          <template #icon><SvgIcon icon="lucide:arrow-up-right" /></template>
        </NButton>
      </NCard>
    </div>

    <NCard :bordered="false" class="account-card card-wrapper">
      <template #header>
        <div class="account-heading">
          <div class="account-avatar">{{ avatarLetter }}</div>
          <div>
            <h2>当前账号</h2>
            <p>你的账户资料</p>
          </div>
        </div>
      </template>
      <template #header-extra>
        <NTag
          v-if="currentUser"
          size="small"
          round
          :bordered="false"
          :type="currentUser.status === 1 ? 'success' : 'default'"
        >
          {{ userStatusText(currentUser.status) }}
        </NTag>
      </template>
      <NSpin :show="loading">
        <NDescriptions v-if="currentUser" label-placement="left" :column="appStore.isMobile ? 1 : 2" bordered>
          <NDescriptionsItem label="用户 ID">{{ currentUser.id }}</NDescriptionsItem>
          <NDescriptionsItem label="用户名">{{ currentUser.username }}</NDescriptionsItem>
          <NDescriptionsItem label="昵称">{{ displayText(currentUser.nickname) }}</NDescriptionsItem>
          <NDescriptionsItem label="真实姓名">{{ displayText(currentUser.realName) }}</NDescriptionsItem>
          <NDescriptionsItem label="手机号">{{ displayText(currentUser.phone) }}</NDescriptionsItem>
          <NDescriptionsItem label="邮箱">
            <span class="break-all">{{ displayText(currentUser.email) }}</span>
          </NDescriptionsItem>
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

<style scoped>
.workspace {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.workspace-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 4px 2px 0;
}

.workspace-eyebrow {
  margin-bottom: 6px;
  color: var(--eams-muted);
  font-size: 10px;
  font-weight: 500;
  letter-spacing: 0.2em;
}

.workspace-heading h1 {
  color: var(--eams-heading);
  font-family: var(--eams-display-font);
  font-size: 28px;
  font-weight: 600;
  letter-spacing: 0.04em;
  line-height: 1.4;
}

.welcome-panel {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 184px;
  padding: 28px 34px;
  overflow: hidden;
  border: 1px solid var(--eams-border);
  border-radius: 8px;
  background: var(--eams-surface);
}

.welcome-panel::before {
  position: absolute;
  z-index: 1;
  inset: 0;
  pointer-events: none;
  opacity: 0.16;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 160 160' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='p'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='.8' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Cpath fill='%23858c7b' opacity='.23' filter='url(%23p)' d='M0 0h160v160H0z'/%3E%3C/svg%3E");
  content: '';
}

.welcome-landscape {
  position: absolute;
  right: 0;
  bottom: -15%;
  width: 72%;
  height: 140%;
  object-fit: cover;
  opacity: 0.5;
  pointer-events: none;
  mask-image: linear-gradient(90deg, transparent, #000 24%);
}

:global(html.dark .welcome-landscape) {
  filter: invert(0.85) sepia(0.12);
  opacity: 0.2;
}

.welcome-content {
  position: relative;
  z-index: 1;
}

.welcome-kicker {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgb(var(--primary-color));
  font-size: 11px;
  letter-spacing: 0.1em;
}

.welcome-kicker > span {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: currentColor;
}

.welcome-content h2 {
  margin-top: 14px;
  color: var(--eams-heading);
  font-family: var(--eams-display-font);
  font-size: 34px;
  font-weight: 500;
  letter-spacing: 0.04em;
  line-height: 1.4;
}

.welcome-description {
  margin-top: 8px;
  color: var(--eams-muted);
  font-size: 13px;
  letter-spacing: 0.05em;
}

.welcome-signature {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding-right: 8px;
}

.welcome-signature > span {
  color: var(--eams-heading);
  font-family: var(--eams-display-font);
  font-size: 23px;
  font-weight: 400;
  letter-spacing: 0.16em;
  writing-mode: vertical-rl;
}

.welcome-signature .welcome-seal {
  margin-top: 32px;
  padding: 5px 3px;
  border: 1px solid currentColor;
  color: rgb(var(--primary-color));
  font-size: 12px;
  letter-spacing: 0.12em;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.overview-topline {
  display: flex;
  align-items: center;
  gap: 12px;
}

.overview-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: 1px solid var(--eams-border);
  border-radius: 5px;
  background: var(--eams-inset);
  color: var(--eams-heading);
  font-size: 19px;
}

.overview-icon--course {
  color: rgb(var(--success-color));
}

.overview-category {
  color: var(--eams-heading);
  font-size: 14px;
  font-weight: 500;
}

.overview-stat {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin: 20px 0 12px;
}

.overview-number {
  color: var(--eams-heading);
  font-size: 40px;
  font-weight: 550;
  font-variant-numeric: tabular-nums;
  line-height: 1;
  letter-spacing: -0.03em;
}

.overview-unit,
.overview-description {
  color: var(--eams-muted);
  font-size: 12px;
}

.overview-description {
  margin-bottom: 22px;
}

.account-heading {
  display: flex;
  align-items: center;
  gap: 12px;
}

.account-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 38px;
  height: 38px;
  border: 1px solid var(--eams-border);
  border-radius: 5px;
  background: var(--eams-inset);
  color: rgb(var(--primary-color));
  font-size: 16px;
  font-weight: 500;
}

.account-heading h2 {
  color: var(--eams-heading);
  font-size: 15px;
  font-weight: 600;
}

.account-heading p {
  margin-top: 2px;
  color: var(--eams-muted);
  font-size: 11px;
  font-weight: 400;
}

@media (max-width: 600px) {
  .workspace {
    gap: 16px;
  }

  .welcome-panel {
    min-height: 160px;
    padding: 24px;
  }

  .welcome-content h2 {
    font-size: 28px;
  }

  .welcome-signature {
    display: none;
  }

  .overview-grid {
    grid-template-columns: minmax(0, 1fr);
    gap: 16px;
  }
}
</style>
