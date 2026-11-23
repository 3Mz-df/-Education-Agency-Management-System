<script setup lang="ts">
import { displayText, formatDateTime, userStatusText } from '@/utils/eams';
import type { SystemUser } from '@/service/api';

defineOptions({ name: 'UserDetail' });

defineProps<{ user: SystemUser }>();
const emit = defineEmits<{ close: [] }>();

function handleVisible(show: boolean) {
  if (!show) emit('close');
}
</script>

<template>
  <NModal :show="true" preset="card" title="用户详情" class="w-620px max-w-95vw" @update:show="handleVisible">
    <NDescriptions bordered :column="1" label-placement="left">
      <NDescriptionsItem label="用户 ID">{{ user.id }}</NDescriptionsItem>
      <NDescriptionsItem label="用户名">{{ user.username }}</NDescriptionsItem>
      <NDescriptionsItem label="昵称">{{ displayText(user.nickname) }}</NDescriptionsItem>
      <NDescriptionsItem label="真实姓名">{{ displayText(user.realName) }}</NDescriptionsItem>
      <NDescriptionsItem label="用户类型">{{ displayText(user.userType) }}</NDescriptionsItem>
      <NDescriptionsItem label="机构 ID">{{ displayText(user.institutionId) }}</NDescriptionsItem>
      <NDescriptionsItem label="手机号">{{ displayText(user.phone) }}</NDescriptionsItem>
      <NDescriptionsItem label="邮箱">{{ displayText(user.email) }}</NDescriptionsItem>
      <NDescriptionsItem label="头像地址">
        <span class="break-all">{{ displayText(user.avatar) }}</span>
      </NDescriptionsItem>
      <NDescriptionsItem label="状态">{{ userStatusText(user.status) }}</NDescriptionsItem>
      <NDescriptionsItem label="创建时间">{{ formatDateTime(user.createTime) }}</NDescriptionsItem>
    </NDescriptions>
  </NModal>
</template>
