import React from 'react';

import SetPasswordForm from './SetPasswordForm';
import UserContext from '../common/UserContext';

export default class SetPassword extends React.Component {
  constructor() {
    super();
    this.state = {
      minHeight: 500,
    };
  }

  componentDidMount() {
    const minHeight = global.window.innerHeight - 64 - 18 - 64 - 66;
    this.setState({ minHeight });
  }

  render() {
    const { minHeight } = this.state;
    return (
      <div style={{ padding: '0 16px 18px', minHeight }}>
        <div
          style={{
            borderTop: '1px solid #ddd',
            paddingTop: 10,
            paddingBottom: 12,
          }}
        >
          <SetPasswordForm user={this.context} />
        </div>
      </div>
    );
  }
}

SetPassword.contextType = UserContext;
