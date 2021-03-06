import React from 'react';

import router from 'umi/router';
import request from 'umi-request';
import PropTypes from 'prop-types';

import Link from 'umi/link';
import {
  Avatar, Layout, Menu, Icon, Dropdown, Spin, ConfigProvider,
} from 'antd';

import zhCN from 'antd/es/locale/zh_CN';
import styles from './index.css';

import UserContext from '../components/common/UserContext';

const {
  Header, Content, Footer, Sider,
} = Layout;

export default class BasicLayout extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      currentMenu: '/',
      currentUser: {},
      showOverlay: true,
    };

    this.linkTo = this.linkTo.bind(this);
    this.logout = this.logout.bind(this);
  }

  componentDidMount() {
    const { location, history } = this.props;

    request.interceptors.response.use((response) => {
      if (history.location.pathname !== '/login') {
        if (response.status === 401) {
          history.push('/login');
        }
      }
      return response;
    });

    // 1. 进入判断是否登录，如果 pathname 不是 "/login"，页面是 loading 状态，如果没有登录，就跳转到 "/login"
    if (location.pathname !== '/login') {
      request
        .get('/api/user/current')
        .then((response) => {
          if (response.object) {
            this.setState({ showOverlay: false, currentUser: response.object });
          } else {
            router.push('/login');
          }
        })
        .catch((error) => {
          global.console.log(error.response);
        });
    }
    this.setState({ currentMenu: location.pathname });

    // 2. 一般首页不是 "/"，需要重定向一下，例如重定向到 "/dashboard", 在此处写明
    if (location.pathname === '/') {
      const defaultIndexPath = '/dashboard';
      router.push(defaultIndexPath);
      this.setState({ currentMenu: defaultIndexPath });
    }
  }

  linkTo(item) {
    if (item.key.indexOf('http:') > -1) {
      this.window.location.href = item.key;
    } else {
      router.push(item.key);
      this.setState({ currentMenu: item.key });
    }
  }

  logout() {
    const { history } = this.props;
    request
      .get('/api/user/logout')
      .then(() => {
        history.push('/login');
      })
      .catch((error) => {
        global.console.log(error.response);
      });
  }

  render() {
    const { currentMenu, currentUser, showOverlay } = this.state;
    const { children, location } = this.props;
    const menu = (
      <Menu>
        <Menu.Item>
          <Link to="/user/set-password">设置密码</Link>
        </Menu.Item>
        <Menu.Divider />
        <Menu.Item onClick={this.logout}>登出</Menu.Item>
      </Menu>
    );

    if (location.pathname === '/login') {
      return <>{children}</>;
    }

    return (
      <UserContext.Provider value={currentUser}>
        <ConfigProvider locale={zhCN}>
          {showOverlay ? (
            <div className={styles.overlay}>
              <Spin size="large" />
            </div>
          ) : (
            <Layout>
              <Sider style={{
                overflow: 'auto', height: '100vh', position: 'fixed', left: 0,
              }}
              >
                <a href="/">
                  <div className={styles.logo} />
                </a>
                <Menu theme="dark" mode="inline" onClick={this.linkTo} selectedKeys={currentMenu}>
                  <Menu.Item key="/dashboard">
                    <Icon type="dashboard" theme="filled" />
                    <span className="nav-text">仪表板</span>
                  </Menu.Item>
                  <Menu.Item key="/users">
                    <Icon type="setting" />
                    <span className="nav-text">用户管理</span>
                  </Menu.Item>
                </Menu>
              </Sider>
              <Layout style={{ marginLeft: 200 }}>
                <Header style={{ background: '#fff', padding: 0 }}>
                  <div style={{ height: 64, float: 'right' }}>
                    <Dropdown overlay={menu} placement="bottomLeft">
                      <div className={[styles.clearfix, styles['user-dropdown']].join(' ')} style={{ height: 64 }}>
                        <div style={{ float: 'left', marginLeft: 12 }}>{currentUser.username}</div>
                        <Avatar icon="user" size={40} style={{ float: 'left', margin: '12px' }} />
                      </div>
                    </Dropdown>
                  </div>
                </Header>
                <Content style={{ margin: '18px 16px 0', overflow: 'initial' }}>
                  <div style={{ background: '#fff' }}>{children}</div>
                </Content>
                <Footer style={{ textAlign: 'center' }}>© Web Dev. 2019-2021</Footer>
              </Layout>
            </Layout>
          )}
        </ConfigProvider>
      </UserContext.Provider>
    );
  }
}

BasicLayout.propTypes = {
  children: PropTypes.element.isRequired,
  location: PropTypes.object.isRequired,
};
