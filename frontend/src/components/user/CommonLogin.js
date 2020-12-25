import React from 'react';

import request from 'umi-request';
import PropTypes from 'prop-types';
import {
  Form, Icon, Input, Button, Checkbox, Modal, message,
} from 'antd';

import CommonStacktraceContent from '../common/CommonStacktraceContent';

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
        .post('/api/user/login', {
          data: {
            username: values.username,
            password: values.password,
          },
          headers: {
            'Content-Type': 'application/json',
          },
        })
        .then((response) => {
          if (response.success) {
            global.window.location.href = '/';
          } else {
            message.error(response.message);
          }
          this.setState({ loginLoading: false });
        })
        .catch((error) => {
          this.setState({ loginLoading: false });
          Modal.info({
            title: `后端错误：${error.data.message}`,
            content: React.createElement(CommonStacktraceContent, { content: error.data.object }),
            style: { top: 20 },
            width: 900,
          });
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
                  ref={input => input && input.focus()}
                  prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                  placeholder="用户名"
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
                  placeholder="密 码"
                />,
              )}
            </Form.Item>
            <Form.Item>
              {getFieldDecorator('remember', {
                valuePropName: 'checked',
                initialValue: true,
              })(<Checkbox disabled={false}>保持登录</Checkbox>)}
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

InlineCommonLogin.propTypes = {
  form: PropTypes.isRequired,
};

const CommonLogin = Form.create({ name: 'user-login' })(InlineCommonLogin);
export default CommonLogin;
