import React from 'react';

import { Form, Input, Button } from 'antd';

class InnerSearchForm extends React.Component {
  constructor() {
    super();
    this.handleReset = this.handleReset.bind(this);
  }

  handleReset() {
    const { form, onSearch } = this.props;
    form.resetFields();
    onSearch();
  }

  render() {
    const { form, onSearch, onClickCreate } = this.props;
    const { getFieldDecorator } = form;

    return (
      <Form layout="inline">
        <div style={{
          paddingTop: 4, paddingBottom: 2,
        }}
        >
          <Form.Item>
            {getFieldDecorator('username', { initialValue: '' })(
              <Input.Search placeholder="搜索用户名" style={{ width: 250 }} onSearch={onSearch} icon="search" />,
            )}
          </Form.Item>
          <Button type="primary" icon="plus-circle" onClick={onClickCreate} style={{ float: 'right' }}>
            新建用户
          </Button>
        </div>
      </Form>
    );
  }
}

const SearchFrom = Form.create()(InnerSearchForm);
export default SearchFrom;
