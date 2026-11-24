import { EAMS_ACCENT } from './eams';

/** 默认主题设置 */
export const themeSettings: App.Theme.ThemeSetting = {
  themeScheme: 'dark',
  grayscale: false,
  colourWeakness: false,
  recommendColor: false,
  themeColor: EAMS_ACCENT,
  themeRadius: 5,
  otherColor: {
    info: '#91a49b',
    success: '#8fa58a',
    warning: '#c2aa76',
    error: '#cc7d79'
  },
  isInfoFollowPrimary: false,
  layout: {
    mode: 'vertical',
    scrollMode: 'content'
  },
  page: {
    animate: true,
    animateMode: 'fade-slide'
  },
  header: {
    height: 56,
    breadcrumb: {
      visible: true,
      showIcon: true
    },
    multilingual: {
      visible: true
    },
    globalSearch: {
      visible: true
    }
  },
  tab: {
    visible: true,
    cache: true,
    height: 44,
    mode: 'button',
    closeTabByMiddleClick: false
  },
  fixedHeaderAndTab: true,
  sider: {
    inverted: false,
    width: 220,
    collapsedWidth: 64,
    mixWidth: 90,
    mixCollapsedWidth: 64,
    mixChildMenuWidth: 200,
    autoSelectFirstMenu: false
  },
  footer: {
    visible: true,
    fixed: false,
    height: 48,
    right: true
  },
  watermark: {
    visible: false,
    text: 'SoybeanAdmin',
    enableUserName: false,
    enableTime: false,
    timeFormat: 'YYYY-MM-DD HH:mm'
  },
  tokens: {
    light: {
      colors: {
        container: 'rgb(250, 249, 244)',
        layout: 'rgb(234, 236, 227)',
        inverted: 'rgb(0, 20, 40)',
        'base-text': 'rgb(48, 58, 48)'
      },
      boxShadow: {
        header: '0 1px 2px rgb(0, 21, 41, 0.08)',
        sider: '2px 0 8px 0 rgb(29, 35, 41, 0.05)',
        tab: '0 1px 2px rgb(0, 21, 41, 0.08)'
      }
    },
    dark: {
      colors: {
        container: 'rgb(34, 37, 35)',
        layout: 'rgb(23, 28, 25)',
        'base-text': 'rgb(225, 229, 218)'
      }
    }
  }
};

/**
 * Override theme settings
 *
 * If publish new version, use `overrideThemeSettings` to override certain theme settings
 */
export const overrideThemeSettings: Partial<App.Theme.ThemeSetting> = {
  themeColor: themeSettings.themeColor,
  themeRadius: themeSettings.themeRadius,
  otherColor: themeSettings.otherColor,
  isInfoFollowPrimary: themeSettings.isInfoFollowPrimary,
  tokens: themeSettings.tokens
};
