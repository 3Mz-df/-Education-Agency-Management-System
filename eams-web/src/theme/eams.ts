import type { GlobalThemeOverrides } from 'naive-ui';
import { addColorAlpha } from '@sa/color';

export const EAMS_ACCENT = '#c58a7b';
const LIGHT_ACCENT = '#995748';

// 朱砂色随深浅模式调整，保证文字辨识度，同时保留自定义主题色。
export function resolveEamsPrimary(color: string, dark: boolean): string {
  if (color !== EAMS_ACCENT && color !== LIGHT_ACCENT) return color;
  return dark ? EAMS_ACCENT : LIGHT_ACCENT;
}

export function createEamsThemeOverrides(dark: boolean, primary: string): GlobalThemeOverrides {
  const surface = dark ? '#222523' : '#faf9f4';
  const inset = dark ? '#292d29' : '#eeeee6';
  const border = dark ? 'rgba(211, 214, 196, 0.14)' : 'rgba(56, 67, 56, 0.14)';
  const muted = dark ? '#a3ada2' : '#737c6f';
  const primaryText = dark ? '#1a1e1a' : '#fffaf2';

  return {
    common: {
      fontFamily: '"Segoe UI", "PingFang SC", "Microsoft YaHei", sans-serif',
      fontWeightStrong: '600',
      bodyColor: dark ? '#171c19' : '#eaece3',
      cardColor: surface,
      modalColor: surface,
      popoverColor: dark ? '#2b302b' : '#faf9f4',
      tableColor: surface,
      inputColor: dark ? '#1c211d' : '#fdfcf8',
      actionColor: inset,
      hoverColor: dark ? '#30362f' : '#e6e9dd',
      borderColor: border,
      dividerColor: border,
      textColorBase: dark ? '#e1e5da' : '#303a30',
      textColor1: dark ? '#eef0e7' : '#2b352b',
      textColor2: dark ? '#c1c9bb' : '#566150',
      textColor3: muted
    },
    Button: {
      fontWeight: '500',
      textColorPrimary: primaryText,
      textColorHoverPrimary: primaryText,
      textColorPressedPrimary: primaryText,
      textColorFocusPrimary: primaryText
    },
    Card: {
      borderRadius: '8px',
      titleFontSizeMedium: '16px',
      titleFontWeight: '600',
      borderColor: border
    },
    Menu: {
      borderRadius: '5px',
      itemHeight: '44px',
      itemTextColor: dark ? '#b8c0b3' : '#5c6857',
      itemIconColor: dark ? '#94a08d' : '#75816d',
      itemColorActive: addColorAlpha(primary, 0.11),
      itemColorActiveHover: addColorAlpha(primary, 0.16),
      itemColorActiveCollapsed: addColorAlpha(primary, 0.11),
      itemTextColorActive: primary,
      itemTextColorActiveHover: primary,
      itemIconColorActive: primary,
      itemIconColorActiveHover: primary
    },
    DataTable: {
      borderRadius: '5px',
      borderColor: border,
      thColor: inset,
      thTextColor: muted,
      thFontWeight: '500',
      tdColor: surface,
      tdColorHover: dark ? '#2c332b' : '#f0f1e8'
    },
    Descriptions: {
      borderRadius: '5px',
      borderColor: border,
      thColor: inset,
      thTextColor: muted,
      tdColor: surface
    }
  };
}
