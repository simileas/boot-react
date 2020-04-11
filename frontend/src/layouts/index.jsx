import React from 'react';

import router from 'umi/router';
import request from 'umi-request';

import Link from 'umi/link';
import { Avatar, Layout, Menu, Icon, Dropdown, Spin } from 'antd';
import { ConfigProvider } from 'antd';

import zhCN from 'antd/es/locale/zh_CN';

import styles from './index.css';

const { Header, Content, Footer, Sider } = Layout;

export default class BasicLayout extends React.Component {

  state = {
    currentMenu: '/',
    showOverlay: true,
  };

  componentDidMount = () => {

    // 1. 进入判断是否登录，如果 pathname 不是 "/login"，页面是 loading 状态，如果没有登录，就跳转到 "/login"
    if (this.props.location.pathname !== '/login') {
      request
        .get('/api/user/current')
        .then(response => {
          if (response.success && response.object) {
            this.setState({ showOverlay: false });
          } else {
            router.push('/login');
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
    this.setState({ currentMenu: this.props.location.pathname });

    // 2. 一般首页不是 "/"，需要重定向一下，例如重定向到 "/dashboard", 在此处写明
    if (this.props.location.pathname === '/') {
      let defaultIndexPath = "/dashboard";
      router.push(defaultIndexPath);
      this.setState({ currentMenu: defaultIndexPath });
    }
  };

  linkTo = item => {
    if (item.key.indexOf('http:') > -1) {
      window.location.href = item.key;
    } else {
      router.push(item.key);
      this.setState({ currentMenu: item.key });
    }
  };

  logout = e => {
    request
      .get('/api/user/logout')
      .then(response => {
        if (response.success) {
          router.push('/login');
        }
      })
      .catch(error => {
        console.log(error);
      });
  };

  render() {
    const menu = (
      <Menu>
        <Menu.Item>
          <Link to="/user/profile">用户设置</Link>
        </Menu.Item>
        <Menu.Divider />
        <Menu.Item onClick={this.logout}>登出</Menu.Item>
      </Menu>
    );

    if (this.props.location.pathname === '/login') {
      return <React.Fragment>{this.props.children}</React.Fragment>;
    }

    return (
      <ConfigProvider locale={zhCN}>
        {this.state.showOverlay ? (
          <div className={styles.overlay}>
            <Spin size="large" />
          </div>
        ) : (
          <Layout>
            <Sider style={{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0 }}>
              <a href="/">
                <div className={styles.logo} />
              </a>
              <Menu theme="dark" mode="inline" onClick={this.linkTo} selectedKeys={[this.state.currentMenu]}>
                <Menu.Item key="/dashboard">
                  <Icon type="dashboard" theme="filled" />
                  <span className="nav-text">仪表板</span>
                </Menu.Item>
                <Menu.Item key="/admin-portal">
                  <Icon type="setting" theme="filled" />
                  <span className="nav-text">管理员区域</span>
                </Menu.Item>
              </Menu>
            </Sider>
            <Layout style={{ marginLeft: 200 }}>
              <Header style={{ background: '#fff', padding: 0 }}>
                <div style={{ height: 64, float: 'right' }}>
                  <Dropdown overlay={menu} placement="bottomLeft">
                    <div className={[styles.clearfix, styles['user-dropdown']].join(' ')} style={{ height: 64 }}>
                      <div style={{ float: 'left', marginLeft: 12 }}>Administrator</div>
                      <Avatar icon="user" size={40} style={{ float: 'left', margin: '12px' }} />
                    </div>
                  </Dropdown>
                </div>
              </Header>
              <Content style={{ margin: '24px 16px 0', overflow: 'initial' }}>
                <div style={{ background: '#fff', textAlign: 'center' }}>{this.props.children}</div>
              </Content>
              <Footer style={{ textAlign: 'center' }}>© 2019</Footer>
            </Layout>
          </Layout>
        )}
      </ConfigProvider>
    );
  }
}
