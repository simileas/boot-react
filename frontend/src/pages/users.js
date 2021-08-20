import React from 'react';

import {
  Tag, Table, Divider, Button, message,
} from 'antd';
import request from 'umi-request';

import NewUserForm from '../components/users/NewUserForm';
import UpdatePasswordForm from '../components/users/UpdatePasswordForm';

export default class Users extends React.Component {
  constructor() {
    super();
    this.state = {
      loading: false,
      data: [],
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10,
      },
      showNewUserForm: false,
      newUserConfirmLoading: false,
      showUpdatePasswordForm: false,
      updatePasswordConfirmLoading: false,
      selectedUsername: '',
    };
    this.pagination = {
      total: 0,
      current: 1,
      pageSize: 10,
    };
    this.loadData = this.loadData.bind(this);
    this.onSearch = this.onSearch.bind(this);
    this.handlePageChange = this.handlePageChange.bind(this);
    this.handleCreateUser = this.handleCreateUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
    this.updateUserStatus = this.updateUserStatus.bind(this);
    this.handleUpdatePassword = this.handleUpdatePassword.bind(this);
    this.updateUserAuthority = this.updateUserAuthority.bind(this);
  }

  componentDidMount() {
    this.loadData();
  }

  onSearch() {
    this.pagination = {
      total: 0,
      current: 1,
      pageSize: 10,
    };
    this.loadData();
  }

  loadData() {
    const { current, pageSize } = this.pagination;
    const params = {
      current,
      pageSize,
    };
    this.setState({ loading: true });
    request
      .get('/api/users', {
        params,
      })
      .then((response) => {
        this.setState({
          pagination: {
            total: response.total,
            current,
            pageSize,
          },
          data: response.list,
          loading: false,
        });
      })
      .catch((error) => {
        this.setState({ loading: false });
        global.console.log(error.response);
      });
  }

  handlePageChange(pagination) {
    this.pagination = pagination;
    this.loadData();
  }

  handleCreateUser() {
    this.setState({ newUserConfirmLoading: true });
    const { props } = this.newUserFormRef;
    props.form.validateFields((validateError, value) => {
      if (!validateError) {
        request
          .put('/api/users', {
            data: {
              username: value.username,
              password: value.password,
              role: value.role,
            },
          })
          .then(() => {
            this.setState({ newUserConfirmLoading: false, showNewUserForm: false });
            this.loadData();
            props.form.resetFields();
          })
          .catch((error) => {
            this.setState({ newUserConfirmLoading: false });
            if (error.response.status === 400) {
              global.console.log(error.data);
              message.warn('用户名已存在');
            } else {
              global.console.log(error.response);
            }
          });
      }
    });
  }

  deleteUser(username) {
    this.setState({ loading: true });
    request
      .delete(`/api/users/${username}`)
      .then(() => {
        this.setState({ loading: false });
        message.info(`用户 ${username} 已删除`);
        this.loadData();
      })
      .catch((error) => {
        this.setState({ loading: false });
        global.console.log(error.response);
      });
  }

  updateUserStatus(username, enabled) {
    this.setState({ loading: true });
    request
      .post('/api/users/state', {
        params: {
          username,
          enabled,
        },
      })
      .then(() => {
        this.setState({ loading: false });
        message.info(`用户 ${username} 已${enabled ? '启用' : '禁用'}`);
        this.loadData();
      })
      .catch((error) => {
        this.setState({ loading: false });
        global.console.log(error.response);
      });
  }

  handleUpdatePassword() {
    this.setState({ updatePasswordConfirmLoading: true });
    const { props } = this.updatePasswordForm;
    props.form.validateFields((validateError, value) => {
      if (!validateError) {
        request
          .post('/api/users/password', {
            params: {
              username: value.username,
              password: value.password,
            },
          })
          .then(() => {
            this.setState({ updatePasswordConfirmLoading: false, showUpdatePasswordForm: false });
            props.form.resetFields();
            message.info('设置密码成功');
          })
          .catch(() => {
            this.setState({ updatePasswordConfirmLoading: false });
            message.warn('设置密码失败');
          });
      }
    });
  }

  updateUserAuthority(username, authority) {
    this.setState({ loading: true });
    const role = authority === 'ADMIN' ? 'USER' : 'ADMIN';
    request
      .post('/api/users/role', {
        params: {
          username,
          role,
        },
      })
      .then(() => {
        this.setState({ loading: false });
        message.info(`用户 ${username} 已设为${authority === 'ADMIN' ? '普通用户' : '管理员'}`);
        this.loadData();
      })
      .catch((error) => {
        this.setState({ loading: false });
        global.console.log(error.response);
      });
  }

  render() {
    const {
      loading, data, pagination, showNewUserForm, newUserConfirmLoading,
      showUpdatePasswordForm, updatePasswordConfirmLoading, selectedUsername,
    } = this.state;
    const roles = {
      ADMIN: '管理员',
      USER: '普通用户',
    };
    const columns = [
      {
        title: '用户名',
        dataIndex: 'username',
        width: 80,
      },
      {
        title: '用户状态',
        dataIndex: 'enabled',
        width: 80,
        render: cell => (
          <Tag
            key={cell}
            color={
              cell ? 'green' : 'red'
            }
          >
            { cell ? '可用' : '禁用'}
          </Tag>
        ),
      },
      {
        title: '角色',
        dataIndex: 'authorities',
        width: 80,
        render: cell => (
          <span>
            {
              cell.map(entity => (
                <Tag key={entity.authority} color={entity.authority === 'ADMIN' ? 'geekblue' : 'cyan'}>
                  {roles[entity.authority]}
                </Tag>
              ))
            }
          </span>
        ),
      },
      {
        title: '操作',
        width: 120,
        render: record => (
          <span>
            <a href="#set-password" onClick={() => { this.setState({ showUpdatePasswordForm: true, selectedUsername: record.username }); }}>设置密码</a>
            {
              record.username === 'admin' ? null : (
                <>
                  <Divider type="vertical" />
                  <a href="#disable" onClick={() => this.updateUserStatus(record.username, !record.enabled)}>
                    {record.enabled ? '禁用用户' : '启用用户'}
                  </a>
                </>
              )
            }
            {
              record.username === 'admin' ? null : (
                <>
                  <Divider type="vertical" />
                  <a href="#delete" onClick={() => this.deleteUser(record.username)}>删除</a>

                </>
              )
            }
            {
              record.username === 'admin' ? null : (
                <>
                  <Divider type="vertical" />
                  <a href="#disable" onClick={() => this.updateUserAuthority(record.username, record.authorities[0].authority)}>
                    {record.authorities[0].authority === 'ADMIN' ? '设为普通用户' : '设为管理员'}
                  </a>
                </>
              )
            }
          </span>
        ),
      },
    ];
    return (
      <div style={{ padding: '20px 16px' }}>
        <div
          style={{
            borderTop: '1px solid #ddd',
            borderBottom: '1px solid #ddd',
            paddingTop: 14,
            paddingBottom: 14,
          }}
        >
          <Button type="primary" icon="plus" onClick={() => { this.setState({ showNewUserForm: true }); }}>
            添加用户
          </Button>
        </div>
        <Table
          style={{ paddingTop: '14px' }}
          rowKey="id"
          columns={columns}
          dataSource={data}
          pagination={pagination}
          loading={loading}
          onChange={this.handleTableChange}
        />
        <NewUserForm
          visible={showNewUserForm}
          onCancel={() => { this.setState({ showNewUserForm: false }); }}
          onCreate={this.handleCreateUser}
          wrappedComponentRef={(inst) => { this.newUserFormRef = inst; }}
          confirmLoading={newUserConfirmLoading}
        />
        <UpdatePasswordForm
          username={selectedUsername}
          visible={showUpdatePasswordForm}
          onCancel={() => { this.setState({ showUpdatePasswordForm: false }); }}
          onOk={this.handleUpdatePassword}
          wrappedComponentRef={(inst) => { this.updatePasswordForm = inst; }}
          confirmLoading={updatePasswordConfirmLoading}
        />
      </div>
    );
  }
}
