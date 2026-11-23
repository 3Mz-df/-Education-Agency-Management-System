<script setup lang="ts">
import { h, onMounted, reactive, ref, shallowRef } from 'vue';
import { NTag } from 'naive-ui';
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui';
import { displayText, formatDateTime } from '@/utils/eams';
import { fetchAddCourse, fetchCourseList } from '@/service/api';
import type { Course } from '@/service/api';

defineOptions({ name: 'Course' });

const courses = shallowRef<Course[]>([]);
const loading = ref(false);
const loadError = ref('');
const showAdd = ref(false);
const saving = ref(false);
const formRef = ref<FormInst | null>(null);
const form = reactive({ courseName: '', description: '' });
let requestVersion = 0;
// 列表接口返回全部课程，当前表格分页在浏览器中完成。
const pagination = reactive({ page: 1, pageSize: 10, showSizePicker: true, pageSizes: [10, 20, 50] });
const rules: FormRules = {
  courseName: [
    { required: true, whitespace: true, message: '请输入课程名称', trigger: ['input', 'blur'] },
    { max: 100, message: '课程名称不能超过 100 个字符', trigger: 'blur' }
  ],
  description: { max: 1000, message: '课程简介不能超过 1000 个字符', trigger: 'blur' }
};
const columns: DataTableColumns<Course> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '课程名称', key: 'courseName', width: 220, ellipsis: { tooltip: true } },
  {
    title: '课程简介',
    key: 'description',
    minWidth: 240,
    ellipsis: { tooltip: true },
    render: row => displayText(row.description)
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: row =>
      h(
        NTag,
        { size: 'small', bordered: false, type: row.status === 1 ? 'success' : 'default' },
        { default: () => courseStatus(row.status) }
      )
  },
  { title: '创建时间', key: 'createTime', width: 180, render: row => formatDateTime(row.createTime) },
  { title: '更新时间', key: 'updateTime', width: 180, render: row => formatDateTime(row.updateTime) }
];

function courseStatus(status: number) {
  return ({ 0: '草稿', 1: '上架', 2: '下架' } as Record<number, string>)[status] ?? String(status);
}

function rowKey(row: Course) {
  return row.id;
}

async function loadCourses() {
  const version = ++requestVersion;
  loading.value = true;
  try {
    const result = await fetchCourseList();
    // 新增后的刷新优先，较早发出的列表请求不能覆盖最新结果。
    if (version !== requestVersion) return;
    if (result.error !== null) {
      loadError.value = result.error;
      return;
    }
    loadError.value = '';
    courses.value = result.data;
  } finally {
    if (version === requestVersion) loading.value = false;
  }
}

function openAdd() {
  Object.assign(form, { courseName: '', description: '' });
  formRef.value?.restoreValidation();
  showAdd.value = true;
}

function closeAdd() {
  if (!saving.value) showAdd.value = false;
}

function handleModalChange(show: boolean) {
  if (!show) closeAdd();
}

function changePage(page: number) {
  pagination.page = page;
}

function changePageSize(size: number) {
  pagination.pageSize = size;
  pagination.page = 1;
}

async function addCourse() {
  if (saving.value) return;
  form.courseName = form.courseName.trim();
  try {
    await formRef.value?.validate();
  } catch {
    return;
  }
  saving.value = true;
  try {
    const result = await fetchAddCourse({ courseName: form.courseName, description: form.description.trim() });
    if (result.error !== null) return;
    window.$message?.success(`课程创建成功，ID：${result.data}`);
    showAdd.value = false;
    pagination.page = 1;
    await loadCourses();
  } finally {
    saving.value = false;
  }
}

onMounted(loadCourses);
</script>

<template>
  <div class="flex-col-stretch gap-16px">
    <NCard title="课程管理" :bordered="false" class="card-wrapper">
      <template #header-extra>
        <NSpace>
          <NButton :loading="loading" @click="loadCourses">刷新</NButton>
          <NButton type="primary" @click="openAdd">新增课程</NButton>
        </NSpace>
      </template>
      <p class="mb-16px text-14px text-gray-500">共 {{ courses.length }} 门课程，新建课程默认保存为草稿。</p>
      <NAlert v-if="loadError" type="error" :show-icon="false" class="mb-16px">{{ loadError }}</NAlert>
      <NDataTable
        :columns="columns"
        :data="courses"
        :loading="loading"
        :row-key="rowKey"
        :pagination="pagination"
        :scroll-x="1000"
        @update:page="changePage"
        @update:page-size="changePageSize"
      />
    </NCard>
    <NModal
      :show="showAdd"
      preset="card"
      title="新增课程"
      class="w-580px max-w-95vw"
      :mask-closable="!saving"
      :close-on-esc="!saving"
      :closable="!saving"
      @update:show="handleModalChange"
    >
      <NForm ref="formRef" :model="form" :rules="rules" label-placement="top">
        <NFormItem label="课程名称" path="courseName">
          <NInput v-model:value="form.courseName" placeholder="请输入课程名称" :maxlength="100" show-count />
        </NFormItem>
        <NFormItem label="课程简介" path="description">
          <NInput
            v-model:value="form.description"
            type="textarea"
            placeholder="请输入课程简介（选填）"
            :maxlength="1000"
            show-count
            :autosize="{ minRows: 4, maxRows: 8 }"
          />
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace justify="end">
          <NButton :disabled="saving" @click="closeAdd">取消</NButton>
          <NButton type="primary" :loading="saving" @click="addCourse">保存草稿</NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>
