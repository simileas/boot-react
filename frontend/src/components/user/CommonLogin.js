import React from 'react';

import request from 'umi-request';
import {
  Form, Icon, Input, Button, Checkbox, message,
} from 'antd';

import styles from './CommonLogin.css';

class InlineCommonLogin extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loginLoading: false,
    };

    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(e) {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }
      this.setState({ loginLoading: true });
      request
        .get('/api/user/login', {
          params: {
            'remember-me': values['remember-me'],
          },
          headers: {
            Authorization: `Basic ${global.btoa(`${values.username}:${values.password}`)}`,
          },
        })
        .then(() => {
          this.setState({ loginLoading: false });
          global.window.location.href = '/';
        })
        .catch((error) => {
          this.setState({ loginLoading: false });
          if (error.response.status === 401) {
            message.error('用户名或密码错误');
          } else if (error.response.status === 403) {
            message.error('用户被禁用');
          } else {
            global.console.log(error.response);
          }
        });
    });
  }

  render() {
    const { loginLoading } = this.state;
    const { form } = this.props;
    const { getFieldDecorator } = form;
    return (
      <div className={styles['login-bg']}>
        <div id="components-normal-login" className={styles['login-box']}>
          <div className={[styles['login-box-header'], styles.clearfix].join(' ')}>
            <div className={styles['login-logo']}>&nbsp;</div>
          </div>
          <Form onSubmit={this.handleSubmit} className={styles['login-form']}>
            <Form.Item>
              {getFieldDecorator('username', {
                rules: [{ required: true, message: '请输入用户名' }],
              })(
                <Input
                  prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                  autoFocus
                  placeholder="用户名"
                  autoComplete="username"
                />,
              )}
            </Form.Item>
            <Form.Item>
              {getFieldDecorator('password', {
                rules: [{ required: true, message: '请输入密码' }],
              })(
                <Input
                  prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                  type="password"
                  autoComplete="current-password"
                  placeholder="密 码"
                />,
              )}
            </Form.Item>
            <Form.Item>
              {getFieldDecorator('remember-me', {
                valuePropName: 'checked',
                initialValue: false,
              })(<Checkbox>保持登录</Checkbox>)}
              <Button
                type="primary"
                htmlType="submit"
                loading={loginLoading}
                className={styles['login-form-button']}
              >
                登录
              </Button>
            </Form.Item>
          </Form>
        </div>
      </div>
    );
  }
}

const CommonLogin = Form.create({ name: 'user-login' })(InlineCommonLogin);
export default CommonLogin;
