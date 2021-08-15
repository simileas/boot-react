import React from 'react';

import {
  Form, Modal, Input,
} from 'antd';

class InternelUpdatePasswordForm extends React.Component {
  constructor() {
    super();
    this.handleConfirmBlur = this.handleConfirmBlur.bind(this);
    this.compareToFirstPassword = this.compareToFirstPassword.bind(this);
    this.validateToNextPassword = this.validateToNextPassword.bind(this);
  }

  handleConfirmBlur(e) {
    const { value } = e.target;
    this.confirmDirty = this.confirmDirty || !!value;
  }

  compareToFirstPassword(rule, value, callback) {
    const { form } = this.props;
    if (value && value !== form.getFieldValue('password')) {
      callback('两次输入的密码不一致');
    } else {
      callback();
    }
  }

  validateToNextPassword(rule, value, callback) {
    const { form } = this.props;
    if (value && this.confirmDirty) {
      form.validateFields(['confirm'], { force: true });
    }
    callback();
  }

  render() {
    const {
      visible, onCancel, onOk, form, confirmLoading, username,
    } = this.props;
    const { getFieldDecorator } = form;
    return (
      <Modal
        visible={visible}
        confirmLoading={confirmLoading}
        title="设置密码"
        okText="提交"
        onCancel={onCancel}
        onOk={onOk}
        width={400}
      >
        <Form layout="vertical">
          <Form.Item label="用户名">
            {getFieldDecorator('username', {
              rules: [{ required: true }],
              initialValue: username,
            })(<Input disabled />)}
          </Form.Item>
          <Form.Item label="密码">
            {getFieldDecorator('password', {
              rules: [
                { required: true, message: '密码不能为空.' },
                { validator: this.validateToNextPassword },
              ],
            })(<Input type="password" autoComplete="new-password" autoFocus />)}
          </Form.Item>
          <Form.Item label="确认密码">
            {getFieldDecorator('confirm', {
              rules: [
                { required: true, message: '请确认密码.' },
                { validator: this.compareToFirstPassword },
              ],
            })(
              <Input
                type="password"
                autoComplete="new-password"
                onPressEnter={onOk}
                onBlur={this.handleConfirmBlur}
              />,
            )}
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

const UpdatePasswordForm = Form.create()(InternelUpdatePasswordForm);
export default UpdatePasswordForm;
