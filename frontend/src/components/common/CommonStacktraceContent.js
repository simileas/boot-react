import React from 'react';

export default class CommonStacktraceContent extends React.Component {
  render() {
    return (
      <div style={{ minHeight: 100, maxHeight: 500, marginRight: 30, overflow: 'auto' }}>
        <pre style={{ width: 1920, fontSize: 12, whiteSpace: 'pre-wrap', fontFamily: 'Consolas, monaco, monospace' }}>
          {this.props.content}
        </pre>
      </div>
    );
  }
}
