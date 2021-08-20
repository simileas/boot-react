import React from 'react';

export default class Dashboard extends React.Component {
  constructor() {
    super();
    this.state = {
      minHeight: 500,
    };
  }

  componentDidMount() {
    const minHeight = global.window.innerHeight - 64 - 18 - 66;
    this.setState({ minHeight });
  }

  render() {
    const { minHeight } = this.state;
    return <div style={{ minHeight }} />;
  }
}
