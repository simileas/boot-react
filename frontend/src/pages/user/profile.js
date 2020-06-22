import React from 'react';
import request from 'umi-request';
import {
  Input, Form, Upload, Button, Icon,
} from 'antd';

export default class Index extends React.Component {
  componentDidMount() {}

  render() {
    return (
      <div style={{ textAlign: 'left', width: 500, padding: '20px 10px' }}>
        <ProfileForm />
      </div>
    );
  }
}

const ProfileForm = Form.create({ name: '用户信息' })(
  class extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        fileList: [],
        uploading: false,
      };
      this.onSubmit = this.onSubmit.bind(this);
    }

    componentDidMount() {}

    onSubmit() {
      const { form } = this.props;
      const formValues = form.getFieldsValue();

      const formData = new global.FormData();
      Object.keys(formValues).forEach((key) => {
        global.console.log(key);
        if (key === 'customFile') {
          if (formValues[key]) {
            formData.append(key, formValues[key].file);
          }
        } else {
          formData.append(key, formValues[key]);
        }
      });

      global.console.log(formData.getAll('customFile'));

      request.post('/api/user/upload', {
        data: formData,
      }).then((response) => {
        global.console.log(response);
      }).catch((error) => {
        global.console.log(error);
      });
    }

    render() {
      const formItemLayout = {
        labelCol: { span: 7 },
        wrapperCol: { span: 13 },
      };
      const { form } = this.props;
      const { getFieldDecorator } = form;
      const { fileList } = this.state;

      const uploadProps = {
        onRemove: () => {
          this.setState({
            fileList: [],
          });
        },
        beforeUpload: (file) => {
          this.setState({
            fileList: [file],
          });
          return false;
        },
        fileList,
      };

      return (
        <Form {...formItemLayout}>
          <Form.Item label="姓名">
            {getFieldDecorator('name', {
              rules: [{ required: true, message: '不能为空.' }],
            })(<Input />)}
          </Form.Item>
          <Form.Item label="自定义文件">
            {getFieldDecorator('customFile')(
              <Upload name="upload" {...uploadProps}>
                <Button>
                  <Icon type="upload" />
                  点击上传文件
                </Button>
              </Upload>,
            )}
          </Form.Item>
          <Form.Item wrapperCol={{ offset: 7 }}>
            <Button type="primary" onClick={this.onSubmit}>
              提交
            </Button>
          </Form.Item>
        </Form>
      );
    }
  },
);
