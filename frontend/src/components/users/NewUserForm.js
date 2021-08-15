import React from 'react';

import {
  Form, Modal, Input, Select,
} from 'antd';

class InternelNewUserForm extends React.Component {
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
      visible, onCancel, onCreate, form, confirmLoading,
    } = this.props;
    const { getFieldDecorator } = form;
    return (
      <Modal
        visible={visible}
        confirmLoading={confirmLoading}
        title="添加用户"
        okText="创建"
        onCancel={onCancel}
        onOk={onCreate}
        width={400}
      >
        <Form layout="vertical">
          <Form.Item label="用户名">
            {getFieldDecorator('username', {
              rules: [{ required: true, message: '用户名不能为空.' }],
            })(<Input autoComplete="username" autoFocus />)}
          </Form.Item>
          <Form.Item label="密码">
            {getFieldDecorator('password', {
              rules: [
                { required: true, message: '密码不能为空.' },
                { validator: this.validateToNextPassword },
              ],
            })(<Input type="password" autoComplete="new-password" />)}
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
                onPressEnter={onCreate}
                onBlur={this.handleConfirmBlur}
              />,
            )}
          </Form.Item>
          <Form.Item label="角色">
            {getFieldDecorator('role', { initialValue: 'USER' })(
              <Select>
                <Select.Option value="ADMIN">管理员</Select.Option>
                <Select.Option value="USER">普通用户</Select.Option>
              </Select>,
            )}
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

const NewUserForm = Form.create()(InternelNewUserForm);
export default NewUserForm;
