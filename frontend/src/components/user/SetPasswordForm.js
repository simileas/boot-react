import React from 'react';
import {
  Form, Input, Button, message,
} from 'antd';
import request from 'umi-request';

const SetPasswordForm = Form.create()(
  class extends React.Component {
    constructor() {
      super();
      this.state = ({ loading: false });
      this.handleSubmit = this.handleSubmit.bind(this);
      this.handleConfirmBlur = this.handleConfirmBlur.bind(this);
      this.compareToFirstPassword = this.compareToFirstPassword.bind(this);
      this.validateToNextPassword = this.validateToNextPassword.bind(this);
    }

    componentDidMount() {

    }

    handleSubmit(e) {
      e.preventDefault();
      this.setState({ loading: true });
      const { form } = this.props;
      form.validateFields((validateError, value) => {
        if (!validateError) {
          request
            .post('/api/user/set-password', {
              params: value,
            }).then(() => {
              message.info('密码设置成功');
            }).catch((error) => {
              message.warn(`密码设置失败: ${error.data.message}`);
            }).finally(() => {
              this.setState({ loading: false });
            });
        }
      });
    }

    handleConfirmBlur(e) {
      const { value } = e.target;
      this.confirmDirty = this.confirmDirty || !!value;
    }

    compareToFirstPassword(rule, value, callback) {
      const { form } = this.props;
      if (value && value !== form.getFieldValue('newPassword')) {
        callback('两次输入的密码不一致');
      } else {
        callback();
      }
    }

    validateToNextPassword(rule, value, callback) {
      const { form } = this.props;
      if (value && this.confirmDirty) {
        form.validateFields(['confirmPassword'], { force: true });
      }
      callback();
    }

    render() {
      const { loading } = this.state;
      const { form, user } = this.props;
      const { getFieldDecorator } = form;
      const formItemLayout = {
        labelCol: {
          sm: { span: 10 }, lg: { span: 8 }, xl: { span: 6 }, xxl: { span: 4 },
        },
        wrapperCol: {
          sm: { span: 14 }, lg: { span: 16 }, xl: { span: 18 }, xxl: { span: 20 },
        },
      };
      const tailFormItemLayout = {
        wrapperCol: {
          sm: { offset: 10 }, lg: { offset: 8 }, xl: { offset: 6 }, xxl: { offset: 4 },
        },
      };

      return (
        <Form
          {...formItemLayout}
          onSubmit={this.handleSubmit}
          style={{
            backgroundColor: '#fff',
            padding: '18px 0 6px 0',
          }}
        >
          <div style={{ width: 400 }}>
            {getFieldDecorator('username', { initialValue: user.username })(<Input type="hidden" autoComplete="username" />)}
            <Form.Item label="旧密码">
              {getFieldDecorator('oldPassword', {
                initialValue: '',
                rules: [
                  { required: true, message: '请输入旧密码' },

                ],
              })(<Input type="password" style={{ width: 260 }} autoFocus autoComplete="current-password" />)}
            </Form.Item>
            <Form.Item label="新密码">
              {getFieldDecorator('newPassword', {
                rules: [
                  { required: true, message: '请输入新密码' },
                  { validator: this.validateToNextPassword },

                ],
              })(<Input type="password" style={{ width: 260 }} autoComplete="new-password" />)}
            </Form.Item>
            <Form.Item label="确认密码">
              {getFieldDecorator('confirmPassword', {
                rules: [
                  { required: true, message: '请输入新密码' },
                  { validator: this.compareToFirstPassword },
                ],
              })(<Input type="password" style={{ width: 260 }} onBlur={this.handleConfirmBlur} autoComplete="new-password" />)}
            </Form.Item>
            <Form.Item {...tailFormItemLayout}>
              <Button type="primary" loading={loading} htmlType="submit">
                提交
              </Button>
            </Form.Item>
          </div>
        </Form>
      );
    }
  },
);

export default SetPasswordForm;
