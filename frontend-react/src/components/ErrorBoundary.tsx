import * as React from "react";
import { ErrorInfo } from "react";

type ErrorState =
  | {
      hasError: false;
    }
  | {
      hasError: true;
      error: Error;
    };

export default class ErrorBoundary extends React.Component<{}, ErrorState> {
  constructor(props: {}) {
    super(props);
    this.state = {
      hasError: false,
    };
  }

  static getDerivedStateFromError(error: Error) {
    // Update state so the next render will show the fallback UI.
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    // You can also log the error to an error reporting service
    console.error(error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      // You can render any custom fallback UI
      return (
        <>
          <h1>Something went wrong.</h1>
          <p>{this.state.error.message}</p>
        </>
      );
    }

    return this.props.children;
  }
}
