import React from 'react';
import PropTypes from 'prop-types';

export default class CommonStacktraceContent extends React.Component {
  componentDidMount() {}

  render() {
    const { content } = this.props;
    return (
      <div style={{
        minHeight: 100, maxHeight: 500, marginRight: 30, overflow: 'auto',
      }}
      >
        <pre style={{
          width: 1920, fontSize: 12, whiteSpace: 'pre-wrap', fontFamily: 'Consolas, monaco, monospace',
        }}
        >
          {content}
        </pre>
      </div>
    );
  }
}

CommonStacktraceContent.propTypes = {
  content: PropTypes.node.isRequired,
};
