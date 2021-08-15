import primaryTheme from '@ant-design/aliyun-theme';
export default {
  theme: primaryTheme,
  treeShaking: true,
  hash: true,
  routes: [
    {
      path: '/',
      component: '../layouts/index',
      routes: [
        {
          path: '/dashboard',
          component: './dashboard',
        },
        {
          path: '/admin-portal',
          component: './admin-portal',
        },
        {
          path: '/users',
          component: './users',
        },
        {
          path: '/login',
          component: './login',
        },
        {
          path: '/user/profile',
          component: './user/profile',
        },
        {
          path: '/',
          component: './index',
        },
      ],
    },
  ],
  plugins: [
    [
      'umi-plugin-react',
      {
        antd: true,
        dva: false,
        dynamicImport: false,
        title: 'Custom Title',
        dll: false,
        locale: {
          enable: true,
          default: 'en-US',
        },
        routes: {
          exclude: [/components\//],
        },
      },
    ],
  ],
  targets: {
    chrome: 49,
    firefox: 45,
    safari: 10,
    edge: 13,
    ios: 10,
    ie: 9,
  },
  proxy: {
    '/api': {
      target: 'http://127.0.0.1:8080',
    },
  },
};
