import React from 'react';
import { PageHeader } from 'antd';

import SetPassword from '../../components/user/SetPassword';

export default class SetPasswordPage extends React.Component {
  componentDidMount() {}

  render() {
    return (
      <>
        <PageHeader
          title="设置密码"
          avatar={{ icon: 'lock', style: { color: '#f56a00', backgroundColor: '#fde3cf' } }}
        />
        <SetPassword />
      </>
    );
  }
}
