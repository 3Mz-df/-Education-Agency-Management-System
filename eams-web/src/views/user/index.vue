<script setup lang="ts">
import { h, onMounted, reactive, ref, shallowRef } from 'vue';
import { NButton, NSpace, NTag } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import { displayText, formatDateTime, userStatusText } from '@/utils/eams';
import { fetchDeleteUser, fetchUserDetail, fetchUserPage } from '@/service/api';
import type { SystemUser } from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';
import UserDetail from './modules/user-detail.vue';
import UserEditor from './modules/user-editor.vue';

defineOptions({ name: 'User' });

const authStore = useAuthStore();
const loading = ref(false);
const loadError = ref('');
const busyId = ref<number | null>(null);
const users = shallowRef<SystemUser[]>([]);
const filters = reactive({ keyword: '', userType: null as number | null, status: null as number | null });
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50] });
const editorOpen = ref(false);
const editingUser = shallowRef<SystemUser | null>(null);
const detailUser = shallowRef<SystemUser | null>(null);
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
];
let requestVersion = 0;

const columns: DataTableColumns<SystemUser> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '用户名', key: 'username', width: 130, ellipsis: { tooltip: true } },
  { title: '真实姓名', key: 'realName', width: 120, render: row => displayText(row.realName) },
  { title: '手机号', key: 'phone', width: 150, render: row => displayText(row.phone) },
  { title: '用户类型', key: 'userType', width: 100, render: row => displayText(row.userType) },
  {
    title: '状态',
    key: 'status',
    width: 90,
    render: row =>
      h(
        NTag,
        { size: 'small', bordered: false, type: row.status === 1 ? 'success' : 'default' },
        { default: () => userStatusText(row.status) }
      )
  },
  { title: '创建时间', key: 'createTime', width: 180, render: row => formatDateTime(row.createTime) },
  {
    title: '操作',
    key: 'actions',
    width: 210,
    fixed: 'right',
    render: row =>
      h(
        NSpace,
        { size: 4, wrap: false },
        {
          default: () => [
            h(
              NButton,
              {
                size: 'small',
                quaternary: true,
                type: 'primary',
                disabled: busyId.value !== null,
                onClick: () => openUser(row.id, 'detail')
              },
              { default: () => '详情' }
            ),
            h(
              NButton,
              {
                size: 'small',
                quaternary: true,
                type: 'primary',
                disabled: busyId.value !== null,
                onClick: () => openUser(row.id, 'edit')
              },
              { default: () => '编辑' }
            ),
            h(
              NButton,
              {
                size: 'small',
                quaternary: true,
                type: 'error',
                disabled: busyId.value !== null || String(row.id) === authStore.userInfo.userId,
                onClick: () => confirmDelete(row)
              },
              { default: () => '删除' }
            )
          ]
        }
      )
  }
];

function rowKey(row: SystemUser) {
  return row.id;
}

async function loadUsers() {
  const version = ++requestVersion;
  loading.value = true;
  try {
    const result = await fetchUserPage({
      pageNum: pagination.page,
      pageSize: pagination.pageSize,
      keyword: filters.keyword.trim() || undefined,
      userType: filters.userType ?? undefined,
      status: filters.status ?? undefined
    });
    // 快速翻页时忽略较早请求的返回，防止旧数据覆盖新页面。
    if (version !== requestVersion) return;
    if (result.error !== null) {
      loadError.value = result.error;
      return;
    }
    loadError.value = '';
    pagination.itemCount = result.data.total;
    const lastPage = Math.max(1, Math.ceil(result.data.total / pagination.pageSize));
    if (pagination.page > lastPage) {
      pagination.page = lastPage;
      await loadUsers();
      return;
    }
    users.value = result.data.list;
  } finally {
    if (version === requestVersion) loading.value = false;
  }
}

function search() {
  pagination.page = 1;
  void loadUsers();
}

function resetFilters() {
  Object.assign(filters, { keyword: '', userType: null, status: null });
  search();
}

function changePage(page: number) {
  pagination.page = page;
  void loadUsers();
}

function changePageSize(size: number) {
  pagination.pageSize = size;
  search();
}

function addUser() {
  if (busyId.value !== null) return;
  editingUser.value = null;
  editorOpen.value = true;
}

function closeEditor() {
  editorOpen.value = false;
}

function closeDetail() {
  detailUser.value = null;
}

async function openUser(id: number, mode: 'edit' | 'detail') {
  if (busyId.value !== null) return;
  busyId.value = id;
  try {
    const result = await fetchUserDetail(id);
    if (result.error !== null) return;
    if (!result.data) {
      window.$message?.warning('用户已不存在，请刷新列表');
      return;
    }
    if (mode === 'edit') {
      editingUser.value = result.data;
      editorOpen.value = true;
    } else {
      detailUser.value = result.data;
    }
  } finally {
    busyId.value = null;
  }
}

async function handleSaved() {
  closeEditor();
  if (editingUser.value && String(editingUser.value.id) === authStore.userInfo.userId) {
    await authStore.initUserInfo();
  }
  await loadUsers();
}

function confirmDelete(user: SystemUser) {
  window.$dialog?.warning({
    title: '删除用户',
    content: `确定删除用户「${user.username}」（ID：${user.id}）吗？`,
    positiveText: '确认删除',
    negativeText: '取消',
    async onPositiveClick() {
      busyId.value = user.id;
      try {
        const result = await fetchDeleteUser(user.id);
        if (result.error !== null) return false;
        window.$message?.success('用户已删除');
        await loadUsers();
        return true;
      } finally {
        busyId.value = null;
      }
    }
  });
}

onMounted(loadUsers);
</script>

<template>
  <div class="flex-col-stretch gap-16px">
    <NCard title="用户管理" :bordered="false" class="card-wrapper">
      <template #header-extra>
        <NButton type="primary" :disabled="busyId !== null" @click="addUser">新增用户</NButton>
      </template>
      <div class="mb-16px flex flex-wrap items-center gap-12px">
        <NInput
          v-model:value="filters.keyword"
          placeholder="搜索用户名 / 手机号"
          clearable
          class="w-240px"
          @keyup.enter="search"
        />
        <NInputNumber
          v-model:value="filters.userType"
          :precision="0"
          clearable
          placeholder="用户类型（数值）"
          class="w-180px"
        />
        <NSelect
          v-model:value="filters.status"
          :options="statusOptions"
          clearable
          placeholder="全部状态"
          class="w-140px"
        />
        <NButton type="primary" :loading="loading" @click="search">查询</NButton>
        <NButton :disabled="loading" @click="resetFilters">重置</NButton>
        <NButton :disabled="loading" @click="loadUsers">刷新</NButton>
      </div>
      <NAlert v-if="loadError" type="error" :show-icon="false" class="mb-16px">{{ loadError }}</NAlert>
      <NDataTable
        remote
        :columns="columns"
        :data="users"
        :loading="loading"
        :row-key="rowKey"
        :pagination="pagination"
        :scroll-x="1160"
        @update:page="changePage"
        @update:page-size="changePageSize"
      />
    </NCard>
    <UserEditor
      v-if="editorOpen"
      :key="editingUser?.id ?? 'new'"
      :user="editingUser"
      @close="closeEditor"
      @saved="handleSaved"
    />
    <UserDetail v-if="detailUser" :user="detailUser" @close="closeDetail" />
  </div>
</template>
