
    // Initialize the Web SDK at the onLoad event of the script element so it is called when the library is loaded.
    window.init = () => {
      window.GrafanaFaroWebSdk.initializeFaro({
        // Mandatory, the URL of the Grafana Cloud collector with embedded application key.
        // Copy from the configuration page of your application in Grafana.
        url: 'http://localhost:12347/collect',

        // Mandatory, the identification label(s) of your application
        app: {
          name: 'my-faro',
          version: '1.0.0', // Optional, but recommended
        },
//        transports: [new window.GrafanaFaroWebSdk.ConsoleTransport()],
      });
    };

    // Dynamically add the tracing instrumentation when the tracing bundle loads
    window.addTracing = () => {
        window.GrafanaFaroWebSdk.faro.instrumentations.add(new window.GrafanaFaroWebTracing.TracingInstrumentation());
    };

