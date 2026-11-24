<script setup lang="ts">
import { computed } from 'vue';
import { $t } from '@/locales';

defineOptions({
  name: 'GlobalLogo'
});

interface Props {
  /** Whether to show the title */
  showTitle?: boolean;
}

withDefaults(defineProps<Props>(), {
  showTitle: true
});

const brandTitle = computed(() => $t('system.title').replace(/^EAMS\s*/i, ''));
</script>

<template>
  <RouterLink
    to="/"
    class="brand-lockup"
    :class="{ 'brand-lockup--compact': !showTitle }"
    :aria-label="$t('system.title')"
  >
    <SystemLogo class="brand-lockup__mark" />
    <div v-show="showTitle" class="brand-lockup__type">
      <span>EAMS</span>
      <h2>{{ brandTitle }}</h2>
    </div>
  </RouterLink>
</template>

<style scoped>
.brand-lockup {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  padding: 0 20px;
  overflow: hidden;
  white-space: nowrap;
}

.brand-lockup--compact {
  justify-content: center;
  padding: 0;
}

.brand-lockup__mark {
  flex-shrink: 0;
  width: 34px;
  height: 34px;
}

.brand-lockup__type > span {
  color: var(--eams-heading);
  font-family: Georgia, 'Times New Roman', serif;
  font-size: 19px;
  font-weight: 500;
  letter-spacing: 0.2em;
  line-height: 1.2;
}

.brand-lockup__type h2 {
  margin-top: 3px;
  color: var(--eams-muted);
  font-family: var(--eams-display-font);
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.16em;
  line-height: 1.3;
}
</style>
