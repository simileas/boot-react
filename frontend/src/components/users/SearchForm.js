import React from 'react';

import {
  Form, Button,
} from 'antd';

class InnerSearchForm extends React.Component {
  constructor() {
    super();
    this.handleReset = this.handleReset.bind(this);
    this.handleSearch = this.handleSearch.bind(this);
  }

  handleReset() {
    const { form, onSearch } = this.props;
    form.resetFields();
    onSearch();
  }

  handleSearch() {
    const { onSearch } = this.props;
    onSearch();
  }

  render() {
    return (
      <Form layout="inline">
        <Button type="primary" shape="circle-outline" icon="sync" onClick={this.handleSearch} />
        <Button type="primary" shape="circle-outline" icon="plus" onClick={this.handleSearch} style={{ float: 'right' }} />
      </Form>
    );
  }
}

const SearchFrom = Form.create()(InnerSearchForm);
export default SearchFrom;
