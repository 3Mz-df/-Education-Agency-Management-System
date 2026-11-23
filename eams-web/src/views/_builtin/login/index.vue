<script setup lang="ts">
import { computed } from 'vue';
import type { GlobalThemeOverrides } from 'naive-ui';
import { $t } from '@/locales';
import { useAppStore } from '@/store/modules/app';
import { useThemeStore } from '@/store/modules/theme';
import loginArtwork from '@/assets/imgs/eams-login-art.svg';
import PwdLogin from './modules/pwd-login.vue';

defineOptions({ name: 'Login', inheritAttrs: false });

const appStore = useAppStore();
const themeStore = useThemeStore();

// 登录表单的配色只作用于当前页面，继续支持手动切换主题。
const loginThemeOverrides = computed<GlobalThemeOverrides>(() => {
  const dark = themeStore.darkMode;
  return {
    Input: {
      borderRadius: '10px',
      heightLarge: '50px',
      fontSizeLarge: '14px',
      color: dark ? '#111218' : '#ffffff',
      colorFocus: dark ? '#151620' : '#ffffff',
      textColor: dark ? '#eef0f7' : '#232638',
      placeholderColor: dark ? '#676b7d' : '#969aab',
      caretColor: '#a89bf7',
      border: dark ? '1px solid #30313d' : '1px solid #d9dce6',
      borderHover: '1px solid #7168b8',
      borderFocus: '1px solid #a395ed',
      boxShadowFocus: '0 0 0 3px rgba(150, 131, 236, 0.12)'
    },
    Button: {
      heightLarge: '50px',
      borderRadiusLarge: '10px',
      fontSizeLarge: '14px',
      fontWeight: '600',
      colorPrimary: '#8573e7',
      colorHoverPrimary: '#9584f2',
      colorPressedPrimary: '#7260cf',
      colorFocusPrimary: '#8573e7',
      borderPrimary: '1px solid #9a89f4',
      borderHoverPrimary: '1px solid #b2a2ff',
      borderPressedPrimary: '1px solid #8573e7',
      borderFocusPrimary: '1px solid #9a89f4',
      textColorPrimary: '#ffffff',
      textColorHoverPrimary: '#ffffff',
      textColorPressedPrimary: '#ffffff',
      textColorFocusPrimary: '#ffffff'
    },
    Form: {
      labelTextColor: dark ? '#cbd0df' : '#42475c'
    }
  };
});
</script>

<template>
  <div class="login-screen" :class="{ 'login-screen--light': !themeStore.darkMode }">
    <img :src="loginArtwork" class="login-artwork" alt="" aria-hidden="true" fetchpriority="high" />
    <div class="login-vignette" aria-hidden="true"></div>

    <header class="login-topbar">
      <div class="login-wordmark">
        <SystemLogo class="size-28px" />
        <span>EAMS</span>
      </div>
      <div class="login-tools">
        <ThemeSchemaSwitch
          :theme-schema="themeStore.themeScheme"
          :show-tooltip="true"
          aria-label="切换明暗主题"
          @switch="themeStore.toggleThemeScheme"
        />
        <LangSwitch
          v-if="themeStore.header.multilingual.visible"
          :lang="appStore.locale"
          :lang-options="appStore.localeOptions"
          :show-tooltip="true"
          aria-label="切换语言"
          @change-lang="appStore.changeLocale"
        />
      </div>
    </header>

    <main class="login-layout">
      <section class="login-brand" aria-label="EAMS 教培管理系统">
        <p class="brand-eyebrow">EDUCATION · MANAGEMENT</p>
        <div class="brand-caption">
          <span class="brand-rule" aria-hidden="true"></span>
          <div>
            <p>让教学与管理有序连接</p>
            <span>专注教学，从容管理。</span>
          </div>
        </div>
      </section>

      <section class="login-panel" aria-labelledby="login-title">
        <div class="panel-brand">
          <SystemLogo class="size-40px" />
          <div>
            <p class="panel-eyebrow">EAMS CONSOLE</p>
            <h1>{{ $t('system.title') }}</h1>
          </div>
        </div>

        <div class="panel-heading">
          <span class="panel-heading-line" aria-hidden="true"></span>
          <h2 id="login-title">欢迎登录</h2>
          <p>请输入账号信息，进入管理工作台</p>
        </div>

        <NConfigProvider :theme-overrides="loginThemeOverrides">
          <PwdLogin />
        </NConfigProvider>

        <div class="panel-footer">
          <span class="footer-rule"></span>
          <span>账号密码登录</span>
          <span class="footer-rule"></span>
        </div>
      </section>
    </main>

    <footer class="login-footer">
      <span>EAMS</span>
      <span class="footer-divider"></span>
      <span>教培管理系统</span>
    </footer>
  </div>
</template>

<style scoped>
.login-screen {
  --login-panel: rgba(23, 24, 32, 0.94);
  --login-border: rgba(212, 213, 235, 0.12);
  --login-text: #f0f1f6;
  --login-secondary: #868a9c;
  --login-input: #111218;

  position: relative;
  isolation: isolate;
  display: flex;
  flex-direction: column;
  min-height: 100dvh;
  width: 100%;
  height: 100%;
  overflow: auto;
  background: #07080b;
  color: var(--login-text);
  font-family: 'Segoe UI', 'Microsoft YaHei', sans-serif;
}

.login-artwork,
.login-vignette {
  position: fixed;
  z-index: -2;
  inset: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.login-artwork {
  object-fit: cover;
  object-position: center;
}

.login-vignette {
  z-index: -1;
  background:
    linear-gradient(90deg, transparent 35%, rgba(7, 8, 11, 0.25) 64%, rgba(7, 8, 11, 0.55)),
    radial-gradient(ellipse at 50% 0%, transparent 20%, rgba(0, 0, 0, 0.22) 100%);
}

.login-topbar {
  position: absolute;
  top: 34px;
  right: 48px;
  left: 48px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.login-wordmark {
  display: flex;
  align-items: center;
  gap: 12px;
}

.login-wordmark > span {
  color: #d9dbe6;
  font-size: 15px;
  font-weight: 650;
  letter-spacing: 0.24em;
}

.login-tools {
  display: flex;
  gap: 8px;
  align-items: center;
  color: #a1a4b4;
}

.login-tools :deep(.n-button) {
  color: #a1a4b4;
}

.login-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 432px;
  align-items: center;
  gap: 96px;
  flex: 1 0 auto;
  min-height: calc(100dvh - 40px);
  width: min(1280px, calc(100% - 128px));
  margin: 0 auto;
  padding: 92px 0 24px;
}

.login-brand {
  display: flex;
  min-height: 476px;
  flex-direction: column;
  justify-content: space-between;
  padding: 30px 0 22px 8px;
}

.brand-eyebrow {
  color: #727482;
  font-size: 11px;
  font-weight: 500;
  letter-spacing: 0.28em;
}

.brand-caption {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.brand-rule {
  width: 28px;
  height: 1px;
  margin-top: 11px;
  background: #676073;
}

.brand-caption p {
  color: #c0c1cc;
  font-size: 17px;
  font-weight: 400;
  letter-spacing: 0.1em;
}

.brand-caption div > span {
  display: block;
  margin-top: 10px;
  color: #606473;
  font-size: 12px;
  letter-spacing: 0.08em;
}

.login-panel {
  position: relative;
  padding: 38px 36px 26px;
  border: 1px solid var(--login-border);
  border-radius: 22px;
  background: var(--login-panel);
  box-shadow:
    0 32px 100px rgba(0, 0, 0, 0.42),
    0 0 0 1px rgba(0, 0, 0, 0.12),
    inset 0 1px 0 rgba(255, 255, 255, 0.035);
  backdrop-filter: blur(28px);
  animation: panel-enter 500ms ease-out both;
}

.login-panel::before {
  position: absolute;
  top: -1px;
  right: 48px;
  left: 48px;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(192, 179, 255, 0.45), transparent);
  content: '';
}

.panel-brand {
  display: flex;
  align-items: center;
  gap: 13px;
}

.panel-eyebrow {
  margin-bottom: 5px;
  color: #85809b;
  font-size: 9px;
  font-weight: 600;
  letter-spacing: 0.22em;
}

.panel-brand h1 {
  color: var(--login-text);
  font-size: 18px;
  font-weight: 550;
  letter-spacing: 0.01em;
}

.panel-heading {
  position: relative;
  margin: 35px 0 28px;
}

.panel-heading-line {
  display: block;
  width: 24px;
  height: 2px;
  margin-bottom: 17px;
  border-radius: 2px;
  background: #9988ec;
}

.panel-heading h2 {
  font-size: 25px;
  font-weight: 550;
  letter-spacing: 0.05em;
  line-height: 1.4;
}

.panel-heading p {
  margin-top: 8px;
  color: var(--login-secondary);
  font-size: 12px;
  letter-spacing: 0.035em;
}

.login-panel :deep(.n-form-item-label) {
  padding-bottom: 9px;
  font-size: 13px;
}

.login-panel :deep(.n-input__input-el:-webkit-autofill) {
  -webkit-box-shadow: 0 0 0 1000px var(--login-input) inset;
  -webkit-text-fill-color: var(--login-text);
  caret-color: #a89bf7;
}

.login-panel :deep(.login-submit) {
  margin-top: 4px;
  letter-spacing: 0.3em;
  box-shadow: 0 5px 22px rgba(110, 87, 206, 0.13);
}

.panel-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 14px;
  margin-top: 26px;
  color: #646879;
  font-size: 10px;
  letter-spacing: 0.08em;
}

.footer-rule {
  width: 30px;
  height: 1px;
  background: rgba(136, 138, 161, 0.14);
}

.login-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  gap: 12px;
  min-height: 40px;
  padding: 0 24px 24px;
  color: #555966;
  font-size: 10px;
  letter-spacing: 0.12em;
}

.footer-divider {
  width: 1px;
  height: 10px;
  background: #33353f;
}

.login-screen--light {
  --login-panel: rgba(244, 245, 250, 0.97);
  --login-border: rgba(255, 255, 255, 0.7);
  --login-text: #242738;
  --login-secondary: #777c91;
  --login-input: #ffffff;
}

@keyframes panel-enter {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (min-width: 1600px) {
  .login-brand {
    padding-left: 0;
  }
}

@media (max-width: 1000px) {
  .login-layout {
    grid-template-columns: minmax(0, 1fr) 400px;
    gap: 40px;
    width: calc(100% - 80px);
  }

  .login-panel {
    padding-right: 30px;
    padding-left: 30px;
  }

  .brand-caption p {
    font-size: 14px;
  }
}

@media (max-width: 780px) {
  .login-topbar {
    top: 25px;
    right: 24px;
    left: 24px;
  }

  .login-layout {
    grid-template-columns: minmax(0, 432px);
    justify-content: center;
    width: calc(100% - 48px);
    padding: 110px 0 36px;
  }

  .login-brand {
    display: none;
  }

  .login-artwork {
    object-position: 20% center;
    opacity: 0.5;
  }

  .login-vignette {
    background: rgba(7, 8, 11, 0.4);
  }
}

@media (max-width: 420px) {
  .login-layout {
    width: calc(100% - 32px);
  }

  .login-panel {
    padding: 30px 24px 24px;
    border-radius: 18px;
  }

  .panel-brand h1 {
    font-size: 16px;
  }

  .panel-heading {
    margin-top: 28px;
  }
}

@media (max-aspect-ratio: 6/5) {
  .login-layout {
    grid-template-columns: minmax(0, 432px);
    justify-content: center;
    width: calc(100% - 48px);
    padding: 110px 0 36px;
  }

  .login-brand {
    display: none;
  }

  .login-artwork {
    object-fit: contain;
    object-position: center top;
    opacity: 0.7;
  }

  .login-vignette {
    background: linear-gradient(180deg, transparent, rgba(7, 8, 11, 0.2) 60%);
  }
}

@media (prefers-reduced-motion: reduce) {
  .login-panel {
    animation: none;
  }
}
</style>
