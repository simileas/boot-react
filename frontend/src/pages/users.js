import React from 'react';
import { PageHeader } from 'antd';

import Users from '../components/users/Users';

export default class UsersPage extends React.Component {
  constructor() {
    super();
    this.state = {
      minHeight: 500,
    };
  }

  componentDidMount() {
    const minHeight = global.window.innerHeight - 64 - 18 - 64;
    this.setState({ minHeight });
  }

  render() {
    const { minHeight } = this.state;
    return (
      <div style={{ minHeight }}>
        <PageHeader
          title="用户管理"
          avatar={{ icon: 'user', style: { color: '#f56a00', backgroundColor: '#fde3cf' } }}
        />
        <Users />
      </div>
    );
  }
}
