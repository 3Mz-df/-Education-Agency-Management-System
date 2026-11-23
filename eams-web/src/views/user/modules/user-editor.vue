<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInst, FormRules } from 'naive-ui';
import { fetchAddUser, fetchUpdateUser } from '@/service/api';
import type { SystemUser, UserInput } from '@/service/api';

defineOptions({ name: 'UserEditor' });

const props = defineProps<{ user: SystemUser | null }>();
const emit = defineEmits<{ close: []; saved: [] }>();
const formRef = ref<FormInst | null>(null);
const saving = ref(false);
const form = reactive({
  username: props.user?.username ?? '',
  password: '',
  nickname: props.user?.nickname ?? '',
  realName: props.user?.realName ?? '',
  userType: props.user?.userType ?? null,
  institutionId: props.user?.institutionId ?? null,
  phone: props.user?.phone ?? '',
  email: props.user?.email ?? '',
  avatar: props.user?.avatar ?? '',
  status: props.user ? props.user.status : 1
});
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
];
const rules: FormRules = {
  username: [
    { required: true, whitespace: true, message: '请输入用户名', trigger: ['input', 'blur'] },
    { min: 2, max: 20, message: '用户名长度需在 2～20 个字符之间', trigger: 'blur' }
  ],
  password: {
    validator: (_rule, value: string) => (props.user || value.trim() ? true : new Error('新增用户必须填写密码')),
    trigger: ['input', 'blur']
  },
  email: { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
  institutionId: {
    validator: (_rule, value: number | null) => {
      if (value === null && props.user?.institutionId != null) return new Error('已设置的机构 ID 暂不支持清空');
      return value === null || (Number.isSafeInteger(value) && value > 0)
        ? true
        : new Error('机构 ID 必须为有效的正整数');
    },
    trigger: ['change', 'blur']
  },
  userType: {
    validator: (_rule, value: number | null) => {
      if (value === null && props.user?.userType != null) return new Error('已设置的用户类型暂不支持清空');
      return value === null || (Number.isInteger(value) && value >= -2147483648 && value <= 2147483647)
        ? true
        : new Error('用户类型必须为有效整数');
    },
    trigger: ['change', 'blur']
  },
  status: {
    validator: (_rule, value: number | null) => value !== null || props.user?.status == null,
    message: '已设置的状态暂不支持清空',
    trigger: 'change'
  }
};

function close() {
  if (!saving.value) emit('close');
}

function handleVisible(show: boolean) {
  if (!show) close();
}

async function save() {
  if (saving.value) return;
  form.username = form.username.trim();
  form.email = form.email.trim();
  try {
    await formRef.value?.validate();
  } catch {
    return;
  }
  saving.value = true;
  try {
    // 编辑时密码留空就完全不传该字段，保留数据库中的原密码。
    const data: UserInput = {
      username: form.username,
      nickname: form.nickname.trim(),
      realName: form.realName.trim(),
      userType: form.userType,
      institutionId: form.institutionId,
      phone: form.phone.trim(),
      email: form.email,
      avatar: form.avatar.trim(),
      status: form.status,
      ...(form.password ? { password: form.password } : {})
    };
    const result = props.user ? await fetchUpdateUser({ ...data, id: props.user.id }) : await fetchAddUser(data);
    if (result.error !== null) return;
    window.$message?.success(props.user ? '用户修改成功' : `用户创建成功，ID：${result.data}`);
    emit('saved');
  } finally {
    saving.value = false;
  }
}
</script>

<template>
  <NModal
    :show="true"
    preset="card"
    :title="user ? '编辑用户' : '新增用户'"
    class="w-680px max-w-95vw"
    :mask-closable="!saving"
    :close-on-esc="!saving"
    :closable="!saving"
    @update:show="handleVisible"
  >
    <NForm ref="formRef" :model="form" :rules="rules" label-placement="top">
      <NGrid cols="1 s:2" responsive="screen" :x-gap="16">
        <NFormItemGi label="用户名" path="username">
          <NInput v-model:value="form.username" placeholder="2～20 个字符" :maxlength="20" />
        </NFormItemGi>
        <NFormItemGi :label="user ? '新密码（留空保留原密码）' : '密码'" path="password">
          <NInput
            v-model:value="form.password"
            type="password"
            show-password-on="click"
            autocomplete="new-password"
            placeholder="请输入密码"
          />
        </NFormItemGi>
        <NFormItemGi label="昵称" path="nickname">
          <NInput v-model:value="form.nickname" :maxlength="30" placeholder="请输入昵称" />
        </NFormItemGi>
        <NFormItemGi label="真实姓名" path="realName">
          <NInput v-model:value="form.realName" :maxlength="50" placeholder="请输入真实姓名" />
        </NFormItemGi>
        <NFormItemGi label="手机号" path="phone">
          <NInput v-model:value="form.phone" :maxlength="20" placeholder="请输入手机号" />
        </NFormItemGi>
        <NFormItemGi label="邮箱" path="email">
          <NInput v-model:value="form.email" :maxlength="100" placeholder="请输入邮箱" />
        </NFormItemGi>
        <NFormItemGi label="用户类型（数值）" path="userType">
          <NInputNumber
            v-model:value="form.userType"
            :precision="0"
            clearable
            placeholder="选填类型数值"
            class="w-full"
          />
        </NFormItemGi>
        <NFormItemGi label="机构 ID" path="institutionId">
          <NInputNumber
            v-model:value="form.institutionId"
            :precision="0"
            :min="1"
            clearable
            placeholder="选填机构 ID"
            class="w-full"
          />
        </NFormItemGi>
        <NFormItemGi label="状态" path="status">
          <NSelect v-model:value="form.status" :options="statusOptions" clearable placeholder="请选择状态" />
        </NFormItemGi>
        <NFormItemGi label="头像地址" path="avatar">
          <NInput v-model:value="form.avatar" placeholder="请输入图片 URL" />
        </NFormItemGi>
      </NGrid>
    </NForm>
    <template #footer>
      <NSpace justify="end">
        <NButton :disabled="saving" @click="close">取消</NButton>
        <NButton type="primary" :loading="saving" @click="save">保存</NButton>
      </NSpace>
    </template>
  </NModal>
</template>
