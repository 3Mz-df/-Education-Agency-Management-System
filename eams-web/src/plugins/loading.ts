// @unocss-include
import { getRgb } from '@sa/color';
import { DARK_CLASS } from '@/constants/app';
import { toggleHtmlClass } from '@/utils/common';
import { localStg } from '@/utils/storage';
import { $t } from '@/locales';
import logoUrl from '@/assets/svg-icon/logo.svg?url';
import { themeSettings } from '@/theme/settings';

export function setupLoading() {
  const themeColor = localStg.get('themeColor') || themeSettings.themeColor;
  const defaultDarkMode = themeSettings.themeScheme === 'dark';
  // 与主题初始化保持一致：开发环境用默认值，正式环境优先使用个人选择。
  const darkMode = import.meta.env.PROD ? (localStg.get('darkMode') ?? defaultDarkMode) : defaultDarkMode;
  const { r, g, b } = getRgb(themeColor);
  const cssVars = `--primary-color: ${r} ${g} ${b}`;

  if (darkMode) {
    toggleHtmlClass(DARK_CLASS).add();
  }

  const loadingClasses = [
    'left-0 top-0',
    'left-0 bottom-0 animate-delay-500',
    'right-0 top-0 animate-delay-1000',
    'right-0 bottom-0 animate-delay-1500'
  ];

  const dot = loadingClasses
    .map(item => {
      return `<div class="absolute w-16px h-16px bg-primary rounded-8px animate-pulse ${item}"></div>`;
    })
    .join('\n');

  // 启动画面复用正式 Logo，避免与登录页和菜单图标不一致。
  const loading = `
<div class="fixed-center flex-col bg-layout" style="${cssVars}">
  <img src="${logoUrl}" alt="EAMS" class="w-128px h-128px" />
  <div class="w-56px h-56px my-36px">
    <div class="relative h-full animate-spin">
      ${dot}
    </div>
  </div>
  <h2 class="text-28px font-500 text-primary">${$t('system.title')}</h2>
</div>`;

  const app = document.getElementById('app');

  if (app) {
    app.innerHTML = loading;
  }
}
