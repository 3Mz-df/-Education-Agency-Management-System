<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInst, FormRules } from 'naive-ui';
import { useAuthStore } from '@/store/modules/auth';

defineOptions({ name: 'PwdLogin' });

const authStore = useAuthStore();
const formRef = ref<FormInst | null>(null);
const model = reactive({ username: '', password: '' });
const rules: FormRules = {
  username: { required: true, whitespace: true, message: '请输入用户名', trigger: ['input', 'blur'] },
  password: { required: true, message: '请输入密码', trigger: ['input', 'blur'] }
};

async function handleSubmit() {
  if (authStore.loginLoading) return;
  try {
    await formRef.value?.validate();
  } catch {
    return;
  }
  await authStore.login(model.username.trim(), model.password);
}
</script>

<template>
  <NForm ref="formRef" :model="model" :rules="rules" size="large" label-placement="top">
    <NFormItem label="用户名" path="username">
      <NInput
        v-model:value="model.username"
        placeholder="请输入 EAMS 用户名"
        autocomplete="username"
        @keyup.enter="handleSubmit"
      />
    </NFormItem>
    <NFormItem label="密码" path="password">
      <NInput
        v-model:value="model.password"
        type="password"
        show-password-on="click"
        placeholder="请输入密码"
        autocomplete="current-password"
        @keyup.enter="handleSubmit"
      />
    </NFormItem>
    <NButton
      class="login-submit"
      type="primary"
      size="large"
      block
      :loading="authStore.loginLoading"
      @click="handleSubmit"
    >
      登录
    </NButton>
  </NForm>
</template>
